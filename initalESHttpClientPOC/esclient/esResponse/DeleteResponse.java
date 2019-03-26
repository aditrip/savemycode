package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class DeleteResponse extends ESWriteResponse implements JsonResponseObjectMapper<DeleteResponse>{

	/*
	 * It is mostly ESWriteResponse but for the field "found".
	 */
	 private static final String FOUND = "found";
	 
	 /*
	  * Delete Response is determined by found.
	  * If true, it means document was deleted.
	  * However, result field can also be checked.
	  */
	 private boolean found;
	 
	 
	public boolean isFound() {
		return found;
	}

	public DeleteResponse found(boolean found) {
		this.found = found;
		return this;
	}

	@Override
	public DeleteResponse buildFromJson(JsonParser parser) throws IOException {
		JsonToken token = parser.nextToken();
		JsonUtil.validateToken(JsonToken.START_OBJECT, token, parser);

        while (parser.nextToken() != JsonToken.END_OBJECT) {
             String currentFieldName = parser.getCurrentName();

             if (FOUND.equals(currentFieldName)) {
                 if (token.isScalarValue()) {
                     found(parser.getBooleanValue());
                 }
             } else {
            	     ESWriteResponse.buildFromJson(parser, this);
             }
        }
        parsed(true);
        return this;
	}

	@Override
	public DeleteResponse buildErrorReponse(ESException e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteResponse buildFromRestResponse(RestResponse restResp) {
		// TODO Auto-generated method stub
		return null;
	}

}
