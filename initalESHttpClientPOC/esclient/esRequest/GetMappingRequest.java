package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpGet;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class GetMappingRequest extends ESRequest<GetMappingRequest> implements ESRestRequestGenerator {
    
    public GetMappingRequest() {
        
    }
    
    public GetMappingRequest(String index, String type) {
        super(index,type);
    }
    
    
    public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (index == null || index.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index name is not provided");
        }
        if (type == null || type.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index type is not provided");
        }
        
        return exception;
    }
    
    @Override
    public RestRequest generateRestRequest() {
        validate();
        String method = HttpGet.METHOD_NAME;
        String endpoint = endpoint(index(),"_mapping",type());        
        RequestParams parameters = new RequestParams();
        //System.out.println("REST REQUEST IS:"
        //        + new RestRequest(method, endpoint, entity, parameters.getParams()));
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }


    @Override
    public RequestType requestType() {
        return RequestType.GET_MAPPING;
    }
    
    

}
