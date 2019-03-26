package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpHead;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class IndexExistRequest extends ESRequest<IndexExistRequest> implements ESRestRequestGenerator{
    
    
    public IndexExistRequest(String index) {
        super(index,null);
    }

    public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (index == null || index.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index name is not provided");
        }
        
        return exception;
    }
    
    @Override
    public RestRequest generateRestRequest() {
        validate();
        String method = HttpHead.METHOD_NAME;
        String endpoint = endpoint(index());        
        RequestParams parameters = new RequestParams();
        //System.out.println("REST REQUEST IS:"
        //        + new RestRequest(method, endpoint, entity, parameters.getParams()));
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.EXIST_INDEX;
    }

}
