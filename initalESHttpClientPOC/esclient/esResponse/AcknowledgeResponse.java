package oracle.kv.impl.tif.esclient.esResponse;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class AcknowledgeResponse extends ESResponse {
    
    private static final String ACKNOWLEDGED = "acknowledged";

    private boolean acknowledged = false;
    
    public AcknowledgeResponse() {

    }
    
    public AcknowledgeResponse(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
    
    
    public boolean isAcknowledged() {
        return acknowledged;
    }
    
    public AcknowledgeResponse acknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
        return this;
    }
    
    /**
     * Builds the acknowlege response.
     * The parser should be positioned before the start of curly braces.
     * 
     */
    public void buildAcknowledgeResponse(JsonParser parser)
            throws IOException {
            JsonToken token = parser.nextToken();
            JsonUtil.validateToken(JsonToken.START_OBJECT, token, parser);
            String currentFieldName = parser.getCurrentName();
            while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
                if(token == JsonToken.FIELD_NAME) {
                     currentFieldName = parser.getCurrentName();
                     continue;
                }

                if (ACKNOWLEDGED.equals(currentFieldName)) {
                    if (token.isScalarValue()) {
                        this.acknowledged = parser.getBooleanValue();
                    } 
                } else if (token.isStructStart()){
                    parser.skipChildren();
                }
            }
            
            parsed(true);
        }


}
