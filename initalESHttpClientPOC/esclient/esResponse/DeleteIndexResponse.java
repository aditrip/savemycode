package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonParser;

public class DeleteIndexResponse extends AcknowledgeResponse implements
JsonResponseObjectMapper<DeleteIndexResponse>{

    public DeleteIndexResponse() {

    }
    
    public DeleteIndexResponse(boolean acknowledged) {
        super(acknowledged);
    }
    

    public DeleteIndexResponse acknowledged(boolean acknowledged) {
        super.acknowledged(acknowledged);
        return this;
    }

    /**
     * Builds the acknowlege response.
     * The parser should be positioned before the start of curly braces.
     */
    @Override
    public DeleteIndexResponse buildFromJson(JsonParser parser)
        throws IOException {
        super.buildAcknowledgeResponse(parser);
        return this;
    }

    @Override
    public DeleteIndexResponse buildErrorReponse(ESException e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeleteIndexResponse buildFromRestResponse(RestResponse restResp) {
        // TODO Auto-generated method stub
        return null;
    }

    
    

}
