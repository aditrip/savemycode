package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class CATResponse extends ESResponse implements JsonResponseObjectMapper<CATResponse> {

	private List<Map<String, Object>> responseMaps = new ArrayList<Map<String, Object>>();

	public CATResponse() {
		
	}
	
	public List<Map<String, Object>> getResponseMaps() {
		return responseMaps;
	}
	
	public Set<String> getIndices() {
		Set<String> indices = new HashSet<String>();
        for(Map<String,Object> map: responseMaps) {
       	 indices.add((String) map.get("index"));
        }
        return indices;
	}
	
	
	@Override
	public CATResponse buildFromJson(JsonParser parser) throws IOException {
		JsonToken token = parser.nextToken();
		JsonUtil.validateToken(JsonToken.START_ARRAY, token, parser);

		while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
			if (token == JsonToken.START_OBJECT) {
				Map<String, Object> map = JsonUtil.parseAsMap(parser);
				responseMaps.add(map);
			}

		}
		
		parsed(true);
		return this;

	}

	@Override
	public CATResponse buildErrorReponse(ESException e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CATResponse buildFromRestResponse(RestResponse restResp) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return responseMaps.toString();
	}

}
