package oracle.kv.impl.tif.esclient.httpClient;

import java.net.URI;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class ESHttpDeleteEntity extends HttpEntityEnclosingRequestBase{
	ESHttpDeleteEntity(final URI uri) {
        setURI(uri);
    }

	@Override
	public String getMethod() {
		
		return HttpDelete.METHOD_NAME;
	}

}
