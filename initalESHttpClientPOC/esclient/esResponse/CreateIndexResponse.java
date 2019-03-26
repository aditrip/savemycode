package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonParser;

public class CreateIndexResponse extends AcknowledgeResponse implements
        JsonResponseObjectMapper<CreateIndexResponse> {

    public CreateIndexResponse() {

    }
    
    public CreateIndexResponse(boolean acknowledged) {
        super(acknowledged);
    }
    

    public CreateIndexResponse acknowledged(boolean acknowledged) {
        super.acknowledged(acknowledged);
        return this;
    }

    /**
     * Builds the acknowlege response.
     * The parser should be positioned before the start of curly braces.
     */
    @Override
    public CreateIndexResponse buildFromJson(JsonParser parser)
        throws IOException {
        super.buildAcknowledgeResponse(parser);
        return this;
    }

    @Override
    public CreateIndexResponse buildErrorReponse(ESException e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CreateIndexResponse buildFromRestResponse(RestResponse restResp) {
        // TODO Auto-generated method stub
        return null;
    }

}
