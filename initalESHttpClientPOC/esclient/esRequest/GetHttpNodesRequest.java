package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpGet;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class GetHttpNodesRequest extends ESRequest<GetHttpNodesRequest> implements ESRestRequestGenerator{

    @Override
    public RestRequest generateRestRequest() {
        String method = HttpGet.METHOD_NAME;
        String endpoint = endpoint("_nodes","http");        
        RequestParams parameters = new RequestParams();
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.GET_NODES;
    }
    
}