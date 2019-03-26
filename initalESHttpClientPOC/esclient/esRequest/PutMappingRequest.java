package oracle.kv.impl.tif.esclient.esRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * 
 * Puts a new mapping for an index. 
 * This does not merge any existing old mapping which exists for this index and type.
 * This does not take care of reserved words in mapping spec.
 * So Caller should take care that mapping spec does not have reserved words.
 * 
 * However, adding new fields is possible by just passing the new fields schema.
 * 
 * Uses indexType as part of the endpoint.
 * 
 * PUT twitter/_mapping/user
 * {
 * 
 *   "properties": {
 *   
 *       "name" : {
 *       
 *          "type" : "text"
 *          
 *       }
 *    
 *    }
 *    
 * }
 * 
 * This class depends on this Json to be passed as JsonGenerator.
 * 
 * 
 *
 */

public class PutMappingRequest extends ESRequest<PutMappingRequest> implements ESRestRequestGenerator{

    private byte[] source;
    
    public PutMappingRequest() {
        
    }
    
    public PutMappingRequest(String index, String type, byte[] source) {
        super(index,type);
        this.source = source;
    }
    
    public PutMappingRequest(String index, String type, JsonGenerator mappingSpec) throws IOException {
        super(index,type);
        mappingSpec.flush();
        this.source = ((ByteArrayOutputStream)mappingSpec.getOutputTarget()).toByteArray();
    }
    
    byte[] source() {
        return source;
    }

    public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (index == null || index.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index name is not provided");
        }
        if (type == null || type.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index type is not provided");
        }
        if (source == null || source.length <= 0) {
            exception = new InvalidRequestException(
                                                    "Index Settings are not provided");
        }
        return exception;
    }
    
    @Override
    public RestRequest generateRestRequest() {
        validate();
        String method = HttpPut.METHOD_NAME;

        String endpoint = endpoint(index(),"_mapping",type());

        byte[] source = source();
        ContentType contentType = ContentType.create("application/json; charset=UTF-8");
        HttpEntity entity = new ByteArrayEntity(source, 0, source.length,
                                                contentType);
        
        RequestParams parameters = new RequestParams();
        //System.out.println("REST REQUEST IS:"
        //        + new RestRequest(method, endpoint, entity, parameters.getParams()));
        return new RestRequest(method, endpoint, entity, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
       return RequestType.PUT_MAPPING;
    }
    
}
