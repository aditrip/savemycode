package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonParser;

public class PutMappingResponse extends AcknowledgeResponse implements JsonResponseObjectMapper<PutMappingResponse>{

    
    public PutMappingResponse() {
        
    }
    
    public PutMappingResponse(boolean acknowledged) {
        super(acknowledged);
    }
    
    public PutMappingResponse acknowledged(boolean acknowledged) {
        super.acknowledged(acknowledged);
        return this;
    }
    
    @Override
    public PutMappingResponse buildFromJson(JsonParser parser)
        throws IOException {
        super.buildAcknowledgeResponse(parser);
        return this;
    }

    @Override
    public PutMappingResponse buildErrorReponse(ESException e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PutMappingResponse buildFromRestResponse(RestResponse restResp) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
    

}
