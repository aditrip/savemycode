package oracle.kv.impl.tif.esclient.restClient;

import java.io.IOException;
import java.util.Map;

import oracle.kv.impl.tif.esclient.esRequest.ClusterHealthRequest;
import oracle.kv.impl.tif.esclient.esRequest.CreateIndexRequest;
import oracle.kv.impl.tif.esclient.esRequest.DeleteIndexRequest;
import oracle.kv.impl.tif.esclient.esRequest.GetMappingRequest;
import oracle.kv.impl.tif.esclient.esRequest.IndexExistRequest;
import oracle.kv.impl.tif.esclient.esRequest.MappingExistRequest;
import oracle.kv.impl.tif.esclient.esRequest.PutMappingRequest;
import oracle.kv.impl.tif.esclient.esResponse.ClusterHealthResponse;
import oracle.kv.impl.tif.esclient.esResponse.CreateIndexResponse;
import oracle.kv.impl.tif.esclient.esResponse.DeleteIndexResponse;
import oracle.kv.impl.tif.esclient.esResponse.GetMappingResponse;
import oracle.kv.impl.tif.esclient.esResponse.IndexExistResponse;
import oracle.kv.impl.tif.esclient.esResponse.MappingExistResponse;
import oracle.kv.impl.tif.esclient.esResponse.PutMappingResponse;
import oracle.kv.impl.tif.esclient.esRequest.VersionInfoRequest;
import oracle.kv.impl.tif.esclient.esResponse.VersionInfoResponse;
import oracle.kv.impl.tif.esclient.esResponse.CATResponse;
import oracle.kv.impl.tif.esclient.esRequest.CATRequest;

public class ESAdminClient {
	private final ESRestClient esRestClient;

	private String esVersion = null;
	private String luceneVersion = null;
	private String clusterName = null;

	ESAdminClient(ESRestClient esRestClient) {
		this.esRestClient = esRestClient;
		try {
			VersionInfoResponse resp = getESVersionInfo(new VersionInfoRequest());
			esVersion = resp.version();
			luceneVersion = resp.luceneVersion();
			clusterName = resp.clusterName();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public CreateIndexResponse createIndex(CreateIndexRequest req)
			throws IOException {
		CreateIndexResponse resp = new CreateIndexResponse();
		return esRestClient.executeSync(req, resp);
	}

	public DeleteIndexResponse deleteIndex(DeleteIndexRequest req)
			throws IOException {
		DeleteIndexResponse resp = new DeleteIndexResponse();
		return esRestClient.executeSync(req, resp);
	}

	public PutMappingResponse createMapping(PutMappingRequest req)
			throws IOException {
		PutMappingResponse resp = new PutMappingResponse();
		return esRestClient.executeSync(req, resp);
	}

	/**
	 * If mapping exists it gets the mapping response as a string.
	 * 
	 * One of the usage of this function could be at places, where existsMapping
	 * API is issued first to first check if mapping exists, if yes then
	 * GetMapping is issued.
	 * 
	 * In case, the checks are only ensuring mapping exists, following pattern
	 * would be better as it saves on network I/O.
	 * 
	 * GetMappingResponse response = adminClient.getMapping(req); if
	 * (response.isFound()) String mappingSpec = response.mapping();
	 * 
	 * 
	 * @param req
	 *            - A GetMappingRequest
	 * @return A GetMappingResponse containing the mapping if it exists.
	 * @throws IOException
	 */
	public GetMappingResponse getMapping(GetMappingRequest req)
			throws IOException {
		GetMappingResponse resp = new GetMappingResponse();
		return esRestClient.executeSync(req, resp);
	}

	public MappingExistResponse mappingExists(MappingExistRequest req)
			throws IOException {
		MappingExistResponse resp = new MappingExistResponse();
		return esRestClient.executeSync(req, resp);
	}

	public IndexExistResponse indexExists(IndexExistRequest req)
			throws IOException {
		IndexExistResponse resp = new IndexExistResponse();
		return esRestClient.executeSync(req, resp);
	}

	public ClusterHealthResponse clusterHealth(ClusterHealthRequest req)
			throws IOException {
		ClusterHealthResponse resp = new ClusterHealthResponse();
		return esRestClient.executeSync(req, resp);
	}

	public CATResponse catInfo(CATRequest.API api, String index,
			String indexPrefix, String indexSuffix, Map<String, String> params)
			throws IOException {
		CATRequest req = new CATRequest(api);
		req.params(params).index(index).indexPrefix(indexPrefix)
				.indexSuffix(indexSuffix);
		CATResponse resp = new CATResponse();
		return esRestClient.executeSync(req, resp);

	}

	public VersionInfoResponse getESVersionInfo(VersionInfoRequest req)
			throws IOException {
		VersionInfoResponse resp = new VersionInfoResponse();
		return esRestClient.executeSync(req, resp);
	}

	public String getESVersion() {
		return esVersion;
	}

	public String getLuceneVersion() {
		return luceneVersion;
	}

	public String getClusterName() {
		return clusterName;
	}

}
