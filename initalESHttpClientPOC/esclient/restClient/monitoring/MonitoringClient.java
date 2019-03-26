package oracle.kv.impl.tif.esclient.restClient.monitoring;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;

import oracle.kv.impl.tif.esclient.esRequest.ESRequest;
import oracle.kv.impl.tif.esclient.esRequest.ESRestRequestGenerator;
import oracle.kv.impl.tif.esclient.esRequest.GetHttpNodesRequest;
import oracle.kv.impl.tif.esclient.esResponse.ESResponse;
import oracle.kv.impl.tif.esclient.esResponse.GetHttpNodesResponse;
import oracle.kv.impl.tif.esclient.esResponse.InvalidResponseException;
import oracle.kv.impl.tif.esclient.esResponse.TimeOrderedResponse;
import oracle.kv.impl.tif.esclient.httpClient.ESHttpClient;
import oracle.kv.impl.tif.esclient.httpClient.ESResponseListener;
import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestRequest;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import oracle.kv.impl.tif.esclient.restClient.utils.ESLatestResponse;
import oracle.kv.impl.tif.esclient.restClient.utils.ESRestClientUtil;
import com.fasterxml.jackson.core.JsonParser;

/**
 * Monitoring APIs are executed asynchronously. They typically do not have any
 * HttpEntity in the request.
 * 
 * Monitoring Client work over a different low level ESHttpClient, than the
 * ESRestClient.
 * 
 * It is preferable to use a different MonitorinClient for a different
 * monitoring request type.
 * 
 * Currently Async responses are not queued anywhere for consumption by other thread.
 * The current use case is only to store the latest state of available ES Nodes.
 * This class only specializes to this particular requirement.
 * 
 * In future, when more use cases arise, this class will be generalized accordingly.
 * 
 */
public class MonitoringClient {

    ESHttpClient httpClient;

    /*
     * A priority Queue which enqueues asynchronous response and dequeues
     * the latest response based on the time the response was received by
     * the client.
     * 
     * TimeOrderedResponse will wrap a RestResponse of GetHttpNodesResponse. 
     * And the natural ordering for TimeOrderedResponse is based on most 
     * recent response.
     * 
     * private final PriorityBlockingQueue<TimeOrderedResponse> responseQueue;
     * private final int waitOnQueueMillis = 3000;
     */
    private final ESLatestResponse latestAvailableNodes;


    public MonitoringClient(ESHttpClient httpClient,
                              ESLatestResponse latestAvailableNodes) {
        this.httpClient = httpClient;
        this.latestAvailableNodes = latestAvailableNodes;
    }

    /*
     * This is used for scheduled monitoring API, and is executed
     * asynchronously.
     */
    public GetHttpNodesResponse getHttpNodesResponse(GetHttpNodesRequest req)
        throws IOException {
        GetHttpNodesResponse resp = new GetHttpNodesResponse();
        executeAsync(req, resp);
        RestResponse restResponse = null;
        JsonParser parser = null;
        try {
            restResponse = latestAvailableNodes.get(1000,TimeUnit.MILLISECONDS).getResponse();
            if (restResponse != null) {
                
                parser = ESRestClientUtil.initParser(restResponse);
                resp.buildFromJson(parser);
            } 
        } catch (InvalidResponseException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            if(parser != null) {
            parser.close();
            }
        }
        
        return resp;
    }

    public <R extends ESRequest<R>, S extends ESResponse> void executeAsync(R req,
                                                                            S resp) {

        @SuppressWarnings("unchecked")
        JsonResponseObjectMapper<S> respMapper = (JsonResponseObjectMapper<S>) resp;
        ESRestRequestGenerator reqGenerator = (ESRestRequestGenerator) req;
        executeAsyncLatestResponse(req, resp, reqGenerator, respMapper,
                     this.latestAvailableNodes);

    }

    private <R extends ESRequest<R>, S extends ESResponse> void executeAsyncLatestResponse(R req,
                                                                             S resp,
                                                                             ESRestRequestGenerator reqGenerator,
                                                                             JsonResponseObjectMapper<S> respMapper,
                                                                             ESLatestResponse latestResponse)
     {

        RestRequest restReq = reqGenerator.generateRestRequest();

        ESResponseListener responseListener = new ESResponseListener() {

            @Override
            public void onSuccess(RestResponse response) {
                TimeOrderedResponse nowResponse = new TimeOrderedResponse(
                                                                          response,
                                                                          System.nanoTime());

                latestResponse.setIfLatest(nowResponse);

            }

            @Override
            public void onFailure(Exception exception) {
                System.out.println("Monitoring Request Failed with exception:"
                        + exception);

            }

            @Override
            public void onRetry(Exception exception) {
                System.out.println("Retries are done by ESHttpClient");

            }

        };

        httpClient.executeAsync(restReq.method(), restReq.endpoint(),
                                restReq.params(), responseListener);

    }

    public List<HttpHost> getAvailableNodes() {
        return httpClient.getAvailableNodes();
    }

    /**
     * set availableNode to all the esHttpClients registered with this monitoring client.
     * 
     * @param availableNodes - List of HttpHosts of ES Cluster which are up and running. 
     * @param esHttpClients - List of ESHttpClients on which httpHosts are set.
     */
    /*
     * This is synchronized because this method is called from different threads,
     * containing different value for availableNodes. 
     * 
     * This method needs to be an atomic operation.
     */
    public synchronized void setAvailableNodes(List<HttpHost> availableNodes, List<ESHttpClient> esHttpClients) {
        System.out.println("Setting availableNodes to: " + availableNodes);
        for(ESHttpClient esHttpClient: esHttpClients) {
           esHttpClient.setAvailableNodes(availableNodes);
        }
    }

    public void close() {
        httpClient.close();
    }

    public void compareAndSet(List<HttpHost> availableNodes, List<ESHttpClient> esHttpClients) {
        if (availableNodes == null 
                || availableNodes.size() == 0
                || esHttpClients == null 
                || esHttpClients.size() == 0) {
            return;
        }
        List<HttpHost> current = getAvailableNodes();
        current.sort(ESHttpClient.httpHostComparator);
        availableNodes.sort(ESHttpClient.httpHostComparator);
        if (current.size() != availableNodes.size()) {
            setAvailableNodes(availableNodes,esHttpClients);
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
            setAvailableNodes(availableNodes,esHttpClients);
        }

    }

    List<HttpHost> getAllESHttpNodes() {
        return httpClient.getAllESHttpNodes();
    }

}
