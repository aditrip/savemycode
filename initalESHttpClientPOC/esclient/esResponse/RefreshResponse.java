package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;

import com.fasterxml.jackson.core.JsonParser;

public class RefreshResponse extends ESResponse implements JsonResponseObjectMapper<RefreshResponse>{

	private boolean success = false;
	@Override
	public RefreshResponse buildFromJson(JsonParser parser)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefreshResponse buildErrorReponse(ESException e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefreshResponse buildFromRestResponse(RestResponse restResp)
			throws IOException {
		 if (restResp.statusLine().getStatusCode() == 200 ) {
	           this.success = true;
	        }
	        parsed(true);
	        return this;
	}

	public boolean isSuccess() {
		return success;
	}


}
