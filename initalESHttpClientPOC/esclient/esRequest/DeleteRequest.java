package oracle.kv.impl.tif.esclient.esRequest;

import org.apache.http.client.methods.HttpDelete;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class DeleteRequest extends ESWriteRequest<DeleteRequest> {
    
        public DeleteRequest() {
            
        }

	public DeleteRequest(String index, String type, String id) {
		super(index, type, id);
	}

	@Override
	public String toString() {
		return "{ delete:" + index + " ,type:" + type + " ,id:" + id + " }";
	}

	@Override
	public RestRequest generateRestRequest() {
		validateWithId();
		String endpoint = endpoint(index(), type(), id());

		RequestParams params = new RequestParams();
		params.routing(routing());
		params.version(version());
		params.refreshType(refreshType());

		return new RestRequest(HttpDelete.METHOD_NAME, endpoint, null, params.getParams());
	}

	@Override
	public RequestType requestType() {
		return RequestType.DELETE;
	}

}
