package oracle.kv.impl.tif.esclient.restClient;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class RestRequest {

    private final String method;
    private final String endpoint;
    private final HttpEntity entity;
    private final Map<String, String> params;

    public RestRequest(String method,
            String endpoint,
            HttpEntity entity,
            Map<String, String> params) {
        this.method = method;
        this.endpoint = endpoint;
        this.entity = entity;
        this.params = params;
    }

    public String method() {
		return method;
	}

	public String endpoint() {
		return endpoint;
	}

	public HttpEntity entity() {
		return entity;
	}

	public Map<String, String> params() {
		return params;
	}

	@Override
    public String toString() {
        String s = "Request{" + "method='" + method + '\'' + ", endpoint='"
                + endpoint + '\'' + ", params=" + params;

        if (entity != null) {
            try {
                s += EntityUtils.toString(entity, "UTF-8");
            } catch (Exception e) {
                
            }
        }
        s += '}';

        return s;
    }

}
