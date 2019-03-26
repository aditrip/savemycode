package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpGet;
import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class VersionInfoRequest extends ESRequest<VersionInfoRequest> implements ESRestRequestGenerator{
    
    
    public VersionInfoRequest() {
        super();
    }

    
    @Override
    public RestRequest generateRestRequest() {
        String method = HttpGet.METHOD_NAME;
        String endpoint = endpoint();        
        RequestParams parameters = new RequestParams();
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.ES_VERSION;
    }
}