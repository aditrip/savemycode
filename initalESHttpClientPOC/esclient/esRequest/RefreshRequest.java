package oracle.kv.impl.tif.esclient.esRequest;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;
import org.apache.http.client.methods.HttpPost;

public class RefreshRequest extends ESRequest<RefreshRequest> implements ESRestRequestGenerator {
	
	private String[] indices;
	
	public RefreshRequest(String... indices) {
		this.indices = indices;
	}

	public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (indices == null || indices.length <= 0) {
            exception = new InvalidRequestException(
                                                    "index names are not provided");
        }
        
        return exception;
        
    }
      
    
    @Override
    public RestRequest generateRestRequest() {
        validate();
        String method = HttpPost.METHOD_NAME;
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<indices.length; i++) {
        	sb.append(indices[i]);
        	if(i < indices.length - 1)
        	sb.append(",");
        }
        

        String endpoint = endpoint(sb.toString(),"_refresh");
        
        RequestParams parameters = new RequestParams();
        System.out.println("REST REQUEST IS:"
                + new RestRequest(method, endpoint, null, parameters.getParams()));
        return new RestRequest(method, endpoint, null, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.REFRESH;
    }
}
