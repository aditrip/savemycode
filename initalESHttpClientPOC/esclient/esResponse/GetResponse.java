package oracle.kv.impl.tif.esclient.esResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.JsonResponseObjectMapper;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class GetResponse extends ESResponse implements
        JsonResponseObjectMapper<GetResponse> {

    private static final String FOUND = "found";
    private static final String FIELDS = "fields";

    private String index;
    private String type;
    private String id;
    private long version;
    private boolean found;
    private Map<String,SourceField> sourceFields = new HashMap<String,SourceField>();
    private Map<String, Object> sourceAsMap;
    private byte[] source;
    
    public GetResponse() {
        
    }
    
    
   public GetResponse(String index, String type, String id, boolean found, byte[] source, long version) {
        
        this.index = index;
        this.id = id;
        this.type = type;
        this.found = found;
        this.source = source;
        this.version = version;
        
    }


    public String index() {
        return index;
    }


    public GetResponse index(String index) {
        this.index = index;
        return this;
    }


    public String type() {
        return type;
    }


    public GetResponse type(String type) {
        this.type = type;
        return this;
    }
    
    public String id() {
        return index;
    }


    public GetResponse id(String id) {
        this.id = id;
        return this;
    }


    public boolean isFound() {
        return found;
    }


    public GetResponse found(boolean found) {
        this.found = found;
        return this;
    }


    public long version() {
        return version;
    }


    public GetResponse version(long version) {
        this.version = version;
        return this;
    }


    public Map<String, SourceField> getSourceFields() {
        return sourceFields;
    }


    public GetResponse sourceFields(Map<String, SourceField> sourceFields) {
        this.sourceFields = sourceFields;
        return this;
    }


    public Map<String, Object> sourceAsMap() {
        return sourceAsMap;
    }


    public GetResponse sourceAsMap(Map<String, Object> sourceAsMap) {
        this.sourceAsMap = sourceAsMap;
        return this;
    }


    public byte[] source() {
        return source;
    }


    public GetResponse source(byte[] source) {
        this.source = source;
        return this;
    }
    /**
     * Get Response with and without _source is given below.
     * 
     * parser is positioned right before the start curly braces of
     * Get Response structure.
     * 
     * { "_index" : "twitter", "_type" : "type1", "_id" : "1", "_version" : 1,
     * "found" : true, "_source" : { "user" : "aditya", "post_date" :
     * "2009-11-15T14:12:12", "message" : "trying out Elasticsearch" } }
     * 
     * with sourceField user. Note that _source is not there in response. And
     * field value is an array.
     * 
     * 
     * { "_index" : "twitter", "_type" : "type1", "_id" : "1", "_version" : 1,
     * "found" : true, "fields" : { "user" : [ "aditya" ] }
     */
    @Override
    public GetResponse buildFromJson(JsonParser parser) throws IOException {
        JsonToken token = parser.nextToken();
        JsonUtil.validateToken(JsonToken.START_OBJECT, token, parser);
        token = parser.nextToken();
        JsonUtil.validateToken(JsonToken.FIELD_NAME, token, parser);
        String currentFieldName = parser.getCurrentName();
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                currentFieldName = parser.getCurrentName();
            } else if (token.isScalarValue()) {
                if (_INDEX.equals(currentFieldName)) {
                    index = parser.getText();
                } else if (_TYPE.equals(currentFieldName)) {
                    type = parser.getText();
                } else if (_ID.equals(currentFieldName)) {
                    id = parser.getText();
                } else if (_VERSION.equals(currentFieldName)) {
                    version = parser.getLongValue();
                } else if (FOUND.equals(currentFieldName)) {
                    found(parser.getBooleanValue());
                } else {
                    sourceFields.put(currentFieldName, new SourceField(currentFieldName,
                                                     Collections.singletonList(parser.getText())));
                }
            } else if (token == JsonToken.START_OBJECT) {
                if ("_source".equals(currentFieldName)) {
                    ByteArrayOutputStream srcByteStream = new ByteArrayOutputStream();
                    JsonGenerator jsonGen = JsonUtil.createGenerator(srcByteStream);
                    try {
                        jsonGen.copyCurrentStructure(parser);
                        jsonGen.flush();
                        source = srcByteStream.toByteArray();
                        sourceAsMap = JsonUtil.convertToMap(source);
                    } finally {
                        jsonGen.close();  // TODO: JsonFactory configuration for autoclose?
                    }
                } else if (FIELDS.equals(currentFieldName)) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        JsonUtil.validateToken(JsonToken.FIELD_NAME,parser.currentToken(),parser);
                        JsonUtil.validateToken(JsonToken.START_ARRAY, parser.nextToken(), parser);
                        SourceField srcField = new SourceField(parser.getCurrentName(), new ArrayList<Object>());
                        sourceFields.put(parser.getCurrentName(), srcField);
                        while(parser.nextToken() != JsonToken.END_ARRAY) {
                            srcField.values().add(JsonUtil.objectValue(parser));
                        }
                    }
                } 
            }
        }
        parsed(true);
        return this;
    }
    
    public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("GetResponse{");
            builder.append("found=").append(isFound());
            builder.append(",index=").append(index());
            builder.append(",type=").append(type());
            builder.append(",id=").append(id());
            builder.append(",version=").append(version());
            return builder.append("}").toString();
        
    }


    @Override
    public GetResponse buildErrorReponse(ESException e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetResponse buildFromRestResponse(RestResponse restResp) {
        // TODO Auto-generated method stub
        return null;
    }


}
