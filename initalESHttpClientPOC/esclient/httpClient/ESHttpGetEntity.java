package oracle.kv.impl.tif.esclient.httpClient;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;

public class ESHttpGetEntity extends HttpEntityEnclosingRequestBase{
	ESHttpGetEntity(final URI uri) {
        setURI(uri);
    }

	@Override
	public String getMethod() {
		return HttpGet.METHOD_NAME;
	}
}


