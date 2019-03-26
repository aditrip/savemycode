package oracle.kv.impl.tif.esclient.esRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.RestRequest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class CreateIndexRequest extends ESRequest<CreateIndexRequest>
        implements ESRestRequestGenerator {

    private byte[] source;

    public CreateIndexRequest() {

    }

    public CreateIndexRequest(String index, byte[] source) {
        this.index = index;
        this.source = source;
    }
    
    public CreateIndexRequest(String index) {
        this.index = index;
    }

    /**
     * Create Index Post Body in UTF-8 Json.
     */
    public CreateIndexRequest source(byte[] source1) {
        this.source = Objects.requireNonNull(source1);
        return this;
    }

    public CreateIndexRequest source(String jsonString) throws IOException {
        Objects.requireNonNull(jsonString);
        JsonParser parser = null;
        JsonGenerator jsonGen = null;
        try {
            parser = JsonUtil.createParser(jsonString);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jsonGen = JsonUtil.createGenerator(baos);
            jsonGen.copyCurrentStructure(parser);
            jsonGen.flush();
            this.source = baos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            if (parser != null) {
                parser.close();
            }
            if (jsonGen != null) {
                jsonGen.close();
            }
        }

        return this;
    }
    
    public CreateIndexRequest settings(Map<String,String> settings) throws IOException {
        Objects.requireNonNull(settings);
        JsonGenerator jsonGen = null;
         try {
            jsonGen = JsonUtil.map(settings);
            this.source = ((ByteArrayOutputStream)jsonGen.getOutputTarget()).toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            if (jsonGen != null) {
                jsonGen.close();
            }
        }

        return this;
    }

    public byte[] source() {
        return source;
    }

    public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (index == null || index.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index name is not provided");
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

        String endpoint = endpoint(index());

        ContentType contentType = ContentType.create("application/json; charset=UTF-8");
        HttpEntity entity = new ByteArrayEntity(source, 0, source.length,
                                                contentType);
        
        RequestParams parameters = new RequestParams();
        System.out.println("REST REQUEST IS:"
                + new RestRequest(method, endpoint, entity, parameters.getParams()));
        return new RestRequest(method, endpoint, entity, parameters.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.CREATE_INDEX;
    }

}
