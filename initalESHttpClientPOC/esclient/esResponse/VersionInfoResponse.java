package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class VersionInfoResponse extends ESResponse implements
JsonResponseObjectMapper<VersionInfoResponse> {

	private static final String CLUSTER_NAME = "cluster_name";
	private static final String VERSION_STRUCT = "version";
	private static final String VERSION_NUMBER = "number";
	private static final String LUCENE_VERSION = "lucene_version";
	
	private String clusterName;
	private String version;
	private String luceneVersion;
	
	@Override
	public VersionInfoResponse buildFromJson(JsonParser parser) throws IOException {
		
		  JsonToken token = parser.nextToken();
	        JsonUtil.validateToken(JsonToken.START_OBJECT, token, parser);
	        String currentFieldName = null;
	        while((token = parser.nextToken()) != JsonToken.END_OBJECT) {

	            if(token == JsonToken.FIELD_NAME) {
	                currentFieldName = parser.getCurrentName();
	                continue;
	            } else if (token.isScalarValue()) {
	                
	                if(CLUSTER_NAME.equals(currentFieldName)) {
	                    clusterName = parser.getText();
	                } 
	                
	            } else if (token == JsonToken.START_OBJECT) {
	                if(VERSION_STRUCT.equals(currentFieldName)) {
	                	  while((token = parser.nextToken()) != JsonToken.END_OBJECT) {
	                		  if(token == JsonToken.FIELD_NAME) {
	          	                currentFieldName = parser.getCurrentName();
	          	                continue;
	          	            } else if (token.isScalarValue()) {
	          	                
	          	                if(VERSION_NUMBER.equals(currentFieldName)) {
	          	                    version = parser.getText();
	          	                } else if (LUCENE_VERSION.equals(currentFieldName)) {
	          	                	   luceneVersion = parser.getText();
	          	                }
	          	                
	          	            } else if (token.isStructStart()) {
	          	            	   parser.skipChildren();
	          	            }
	                	  }
	                }
	            }
	        }

	        parsed(true);
	        return this;
	}

	@Override
	public VersionInfoResponse buildErrorReponse(ESException e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionInfoResponse buildFromRestResponse(RestResponse restResp) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String clusterName() {
		return clusterName;
	}

	public VersionInfoResponse clusterName(String clusterName) {
		this.clusterName = clusterName;
		return this;
	}

	public String version() {
		return version;
	}

	public VersionInfoResponse version(String version) {
		this.version = version;
		return this;
	}

	public String luceneVersion() {
		return luceneVersion;
	}

	public VersionInfoResponse luceneVersion(String luceneVersion) {
		this.luceneVersion = luceneVersion;
		return this;
	}
	
	

}
