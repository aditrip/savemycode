package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import oracle.kv.impl.tif.esclient.restClient.RestStatus;
import com.fasterxml.jackson.core.JsonParser;

public class IndexExistResponse extends ESResponse implements JsonResponseObjectMapper<IndexExistResponse>{

    private boolean exists = false;
    private boolean error = false;
    
    public IndexExistResponse() {
        
    }
    
    public boolean exists() {
        return exists;
    }
    
    public IndexExistResponse exist(boolean exist) {
        this.exists = exist;
        return this;
    }
    public boolean isError() {
        return error;
    }

    public IndexExistResponse error(boolean error) {
        this.error = error;
        return this;
    }

    @Override
    public IndexExistResponse buildFromJson(JsonParser parser)
        throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexExistResponse buildErrorReponse(ESException e) {
         if(e != null) {
             if(e.errorStatus() == RestStatus.NOT_FOUND) {
                 exists = false;
             }
         } else {
             error = true;
         }
         
         return this;
    }

    @Override
    public IndexExistResponse buildFromRestResponse(RestResponse restResp)
        throws IOException {
        if (restResp.statusLine().getStatusCode() == 200 ) {
            exists = true;
        }
        parsed(true);
        return this;
    }

}
