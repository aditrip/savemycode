package oracle.kv.impl.tif.esclient.esRequest;


import oracle.kv.impl.tif.esclient.restClient.RestRequest;

import org.apache.http.client.methods.HttpHead;

public class MappingExistRequest extends ESRequest<MappingExistRequest> implements ESRestRequestGenerator{
    
    
    public MappingExistRequest(String index, String type) {
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
                                                    "type name is not provided");
        }
        
        return exception;
    }
    
    @Override
    public RestRequest generateRestRequest() {
        validate();
        String method = HttpHead.METHOD_NAME;
        String endpoint = endpoint(index(),"_mapping",type());        
        RequestParams parameters = new RequestParams();
        //System.out.println("REST REQUEST IS:"
        //        + new RestRequest(method, endpoint, entity, parameters.getParams()));
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.MAPPING_EXIST;
    }

}

