package oracle.kv.impl.tif.esclient.restClient;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpHost;

import com.fasterxml.jackson.core.JsonParser;

import oracle.kv.impl.tif.esclient.esRequest.ESRequest;
import oracle.kv.impl.tif.esclient.esRequest.ESRestRequestGenerator;
import oracle.kv.impl.tif.esclient.esRequest.ESRequest.RequestType;
import oracle.kv.impl.tif.esclient.esResponse.ESException;
import oracle.kv.impl.tif.esclient.esResponse.ESResponse;
import oracle.kv.impl.tif.esclient.esResponse.InvalidResponseException;
import oracle.kv.impl.tif.esclient.httpClient.ESHttpClient;
import oracle.kv.impl.tif.esclient.restClient.utils.ESRestClientUtil;

/**
 * A Rest Client for Elasticsearch which executes REST request and parses the
 * REST Response from ES.
 * 
 * Currently FTS works on a synchronous communication with ES. Most of the
 * requests are bulk requests which need to be synchronous to preserve event
 * ordering.
 * 
 * This client only provides a synchronous request execution. The monitoring
 * client which works independently provides asynchronous communication with
 * ES.
 * 
 */
public class ESRestClient {

    private final ESHttpClient httpClient;
    private ESAdminClient adminClient;
    private ESDMLClient dmlClient;
    private final ReentrantLock lock = new ReentrantLock();
    
    
    public ESRestClient(ESHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    /*
     * It is possible that first few admin APIs get
     * executed in different adminClient object. But that is fine.
     */
    public ESAdminClient admin() {
    	if(adminClient != null) {
    		return adminClient;
    	}
    	lock.lock();
    	try{
    	   adminClient = new ESAdminClient(this);
    	} finally {
    		lock.unlock();
    	}
    	return adminClient;
    }
    
    public ESDMLClient dml() {
    	if(dmlClient != null) {
    		return dmlClient;
    	}
    	lock.lock();
    	try{
    		dmlClient = new ESDMLClient(this);
    	} finally {
    		lock.unlock();
    	}
    	return dmlClient;
    }

    public <R extends ESRequest<R>, S extends ESResponse> S executeSync(R req,
                                                                        S resp)
        throws IOException {

        @SuppressWarnings("unchecked")
        JsonResponseObjectMapper<S> respMapper = (JsonResponseObjectMapper<S>) resp;
        ESRestRequestGenerator reqGenerator = (ESRestRequestGenerator) req;
        return executeSync(req, reqGenerator, respMapper);

    }

    public <R extends ESRequest<R>, S extends ESResponse> S executeSync(R req,
                                                                        ESRestRequestGenerator reqGenerator,
                                                                        JsonResponseObjectMapper<S> respMapper)
        throws IOException {

        RestRequest restReq = reqGenerator.generateRestRequest();
        RestResponse restResp = null;
        JsonParser parser = null;
        S esResponse = null;
        try {
            restResp = httpClient.executeSync(restReq.method(),
                                              restReq.endpoint(),
                                              restReq.params(), restReq.entity());
            // System.out.println(EntityUtils.toString(restResp.getEntity()));

            RequestType reqType = req.requestType();
            switch (reqType) {
                case GET_MAPPING:
                case EXIST_INDEX:
                case REFRESH:
                    esResponse = respMapper.buildFromRestResponse(restResp);
                    esResponse.statusCode(restResp.statusLine().getStatusCode());
                    break;
                default:
                    parser = ESRestClientUtil.initParser(restResp);
                    esResponse = respMapper.buildFromJson(parser);
                    esResponse.statusCode(restResp.statusLine().getStatusCode());
                    break;
            }

        } catch (ESException e) {
            // TODO: log this exception after making sure that
            // http entity is repeatable. ESException constructor
            // should convert it into a repeatable entity, if not.
            System.out.println("ESException Details:");
            System.out.println("errorType:" + e.errorType());
            System.out.println("reason:" + e.reason());
            System.out.println("cause:" + e);
            System.out.println("rootCauses:" + e.rootCauses());
            System.out.println(e);
            RequestType reqType = req.requestType();
            switch (reqType) {
                case EXIST_INDEX:
                    esResponse = respMapper.buildErrorReponse(e);
                    break;
                default:
                    throw new IOException(e);

            }

        } catch (InvalidResponseException ire) {
            if (restResp.isSuccess() || restResp.isRetriable()) {
                // TODO: log this exception, this must be a parsing exception.
                if (respMapper instanceof ESResponse) {
                    ((ESResponse) respMapper).statusCode(restResp.statusLine()
                                                                 .getStatusCode());
                }
                System.out.println(ire);
            } else {
                throw new IOException(ire);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            if (restResp != null && (restResp.isSuccess() || restResp.isRetriable())) {
                // TODO: log this exception, this must be a parsing exception.
                if (respMapper instanceof ESResponse) {
                    ((ESResponse) respMapper).statusCode(restResp.statusLine()
                                                                 .getStatusCode());

                }
                System.out.println(ie);
            } else {
                throw ie;
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
        if (esResponse != null) {
            return esResponse;
        } 
            return respMapper.buildFromRestResponse(restResp);
        
    }

    public List<HttpHost> getAvailableNodes() {
        return httpClient.getAvailableNodes();
    }

    public void setAvailableNodes(List<HttpHost> availableNodes) {
        System.out.println("Setting availableNodes to: " + availableNodes);
        httpClient.setAvailableNodes(availableNodes);
    }

    public void close() {
        httpClient.close();
    }

    public void compareAndSet(List<HttpHost> availableNodes) {
        if (availableNodes == null || availableNodes.size() == 0) {
            return;
        }
        List<HttpHost> current = getAvailableNodes();
        current.sort(ESHttpClient.httpHostComparator);
        availableNodes.sort(ESHttpClient.httpHostComparator);
        if (current.size() != availableNodes.size()) {
            setAvailableNodes(availableNodes);
            return;
        }

        boolean diff = false;
        Iterator<HttpHost> newList = availableNodes.iterator();
        for (HttpHost host : current) {
            if (!newList.next().equals(host)) {
                diff = true;
                break;
            }
        }
        if (diff) {
            setAvailableNodes(availableNodes);
        }

    }

    List<HttpHost> getAllESHttpNodes() {
        return httpClient.getAllESHttpNodes();
    }

}
