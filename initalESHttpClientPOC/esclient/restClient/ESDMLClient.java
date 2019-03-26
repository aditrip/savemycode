package oracle.kv.impl.tif.esclient.restClient;

import java.io.IOException;

import oracle.kv.impl.tif.esclient.esRequest.BulkRequest;
import oracle.kv.impl.tif.esclient.esRequest.DeleteRequest;
import oracle.kv.impl.tif.esclient.esRequest.GetRequest;
import oracle.kv.impl.tif.esclient.esRequest.IndexDocumentRequest;
import oracle.kv.impl.tif.esclient.esRequest.RefreshRequest;
import oracle.kv.impl.tif.esclient.esRequest.SearchRequest;
import oracle.kv.impl.tif.esclient.esResponse.BulkResponse;
import oracle.kv.impl.tif.esclient.esResponse.DeleteResponse;
import oracle.kv.impl.tif.esclient.esResponse.GetResponse;
import oracle.kv.impl.tif.esclient.esResponse.IndexDocumentResponse;
import oracle.kv.impl.tif.esclient.esResponse.RefreshResponse;
import oracle.kv.impl.tif.esclient.esResponse.SearchResponse;

public class ESDMLClient {

	private final ESRestClient esRestClient;
	
    ESDMLClient(ESRestClient esRestClient) {
        this.esRestClient = esRestClient;
    }

    public IndexDocumentResponse index(IndexDocumentRequest req)
        throws IOException {
        IndexDocumentResponse resp = new IndexDocumentResponse();
        return esRestClient.executeSync(req, resp);
    }

    public DeleteResponse delete(DeleteRequest req)
        throws IOException {
        DeleteResponse resp = new DeleteResponse();
        return esRestClient.executeSync(req, resp);
    }

    public GetResponse get(GetRequest req) throws IOException {
        GetResponse resp = new GetResponse();
        return esRestClient.executeSync(req, resp);
    }

    public SearchResponse search(SearchRequest req) throws IOException {
        SearchResponse resp = new SearchResponse();
        return esRestClient.executeSync(req, resp);
    }

    public BulkResponse bulk(BulkRequest req) throws IOException {
        BulkResponse resp = new BulkResponse();
        return esRestClient.executeSync(req, resp);
    }
    
    public boolean refresh(String... indices) throws IOException {
    	RefreshRequest req = new RefreshRequest(indices);
    	RefreshResponse resp = new RefreshResponse();
        esRestClient.executeSync(req, resp);
        return resp.isSuccess();
    }

}
