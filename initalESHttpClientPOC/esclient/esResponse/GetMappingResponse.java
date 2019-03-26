package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import oracle.kv.impl.tif.esclient.restClient.RestStatus;
import com.fasterxml.jackson.core.JsonParser;

public class GetMappingResponse extends ESResponse implements JsonResponseObjectMapper<GetMappingResponse>{
    
    private String mapping;
    private boolean found = false;
    
    
    public boolean isFound() {
        return found;
    }

    public GetMappingResponse found(boolean exists) {
        this.found = exists;
        return this;
    }

    public GetMappingResponse() {
        
    }
    
    public String mapping() {
        return mapping;
    }
    
    public GetMappingResponse mapping(String mappingSpec) {
        this.mapping = mappingSpec;
        return this;
    }
    @Override
    public GetMappingResponse buildFromJson(JsonParser parser)
        throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetMappingResponse buildErrorReponse(ESException e) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Get Mapping Response is built using the http response.
     */
    @Override
    public GetMappingResponse buildFromRestResponse(RestResponse restResp) throws IOException{
        statusCode(restResp.statusLine().getStatusCode());
        if(statusCode() == RestStatus.OK.getStatus()) {
            found = true;
        }
        HttpEntity entity = restResp.getEntity();

        if (entity != null) {
            if (entity.isRepeatable() == false) {
                entity = new BufferedHttpEntity(entity);    
                restResp.httpResponse().setEntity(entity);
            }
            this.mapping = EntityUtils.toString(entity, Charset.forName("UTF-8"));
        }
        parsed(true);
        return this;
    }
    
    public String toString() {
        return "GetResponse:[" +
               "statusCode:" + statusCode() +
               "  Mapping:" + mapping()
               +" ]";
    }

}
