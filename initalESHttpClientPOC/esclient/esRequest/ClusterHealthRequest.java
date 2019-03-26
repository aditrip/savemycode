package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpGet;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class ClusterHealthRequest extends ESRequest<ClusterHealthRequest> implements ESRestRequestGenerator{

    public ClusterHealthRequest() {
        
    }
    
    public ClusterHealthRequest(String index) {
        super(index,null);
    }
    
    @Override
    public RestRequest generateRestRequest() {
        String method = HttpGet.METHOD_NAME;

        String endpoint = endpoint("_cluster","health",index());
        
        RequestParams parameters = new RequestParams();
        parameters.clusterHealthLevel("cluster");
        System.out.println("REST REQUEST IS:"
                + new RestRequest(method, endpoint, null, parameters.getParams()));
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.CLUSTER_HEALTH;
    }

}
