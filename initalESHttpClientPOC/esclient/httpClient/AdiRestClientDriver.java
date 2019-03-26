package oracle.kv.impl.tif.esclient.httpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import oracle.kv.impl.param.ParameterUtils;
import oracle.kv.impl.tif.esclient.esRequest.BulkRequest;
import oracle.kv.impl.tif.esclient.esRequest.CATRequest;
import oracle.kv.impl.tif.esclient.esRequest.ClusterHealthRequest;
import oracle.kv.impl.tif.esclient.esRequest.CreateIndexRequest;
import oracle.kv.impl.tif.esclient.esRequest.DeleteIndexRequest;
import oracle.kv.impl.tif.esclient.esRequest.DeleteRequest;
import oracle.kv.impl.tif.esclient.esRequest.GetHttpNodesRequest;
import oracle.kv.impl.tif.esclient.esRequest.GetMappingRequest;
import oracle.kv.impl.tif.esclient.esRequest.GetRequest;
import oracle.kv.impl.tif.esclient.esRequest.IndexDocumentRequest;
import oracle.kv.impl.tif.esclient.esRequest.IndexExistRequest;
import oracle.kv.impl.tif.esclient.esRequest.MappingExistRequest;
import oracle.kv.impl.tif.esclient.esRequest.PutMappingRequest;
import oracle.kv.impl.tif.esclient.esRequest.SearchRequest;
import oracle.kv.impl.tif.esclient.esResponse.BulkResponse;
import oracle.kv.impl.tif.esclient.esResponse.CATResponse;
import oracle.kv.impl.tif.esclient.esResponse.ClusterHealthResponse;
import oracle.kv.impl.tif.esclient.esResponse.CreateIndexResponse;
import oracle.kv.impl.tif.esclient.esResponse.DeleteIndexResponse;
import oracle.kv.impl.tif.esclient.esResponse.ESException;
import oracle.kv.impl.tif.esclient.esResponse.GetHttpNodesResponse;
import oracle.kv.impl.tif.esclient.esResponse.GetMappingResponse;
import oracle.kv.impl.tif.esclient.esResponse.IndexDocumentResponse;
import oracle.kv.impl.tif.esclient.esResponse.IndexExistResponse;
import oracle.kv.impl.tif.esclient.esResponse.MappingExistResponse;
import oracle.kv.impl.tif.esclient.esResponse.PutMappingResponse;
import oracle.kv.impl.tif.esclient.httpClient.ESHttpClient.Scheme;
import oracle.kv.impl.tif.esclient.jsonContent.ESJsonBuilder;
import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.ESAdminClient;
import oracle.kv.impl.tif.esclient.restClient.ESDMLClient;
import oracle.kv.impl.tif.esclient.restClient.ESRestClient;
import oracle.kv.impl.tif.esclient.restClient.monitoring.ESNodeMonitor;
import oracle.kv.impl.tif.esclient.restClient.monitoring.MonitoringClient;
import oracle.kv.impl.tif.esclient.restClient.utils.ESLatestResponse;
import oracle.kv.impl.tif.esclient.restClient.utils.ESRestClientUtil;

import com.fasterxml.jackson.core.JsonGenerator;

public class AdiRestClientDriver {

	public ESHttpClient esHttpClient = null;
	public ESRestClient esRestClient = null;
	public ESDMLClient dmlClient = null;
	public ESAdminClient adminClient = null;
	public MonitoringClient monitorClient = null;
	// public ESNodeMonitorOld esNodeMonitor = null;
	public ESNodeMonitor esNodeMonitor2 = null;

	public AdiRestClientDriver() {
		init();
	}

	public static void main(String[] args) throws IOException {

		AdiRestClientDriver client = new AdiRestClientDriver();
		try {
			//testCreateIndex(client);

		    //testPutMapping(client);

			// Thread.currentThread().sleep(7000);
			//testDeleteIndex(client);

			//testGetMapping(client);

			//testIndexExists(client);
			// Thread.currentThread().sleep(5000);
			//testClusterHealth(client);
			//testClusterHealthIndexLevel(client);

			testHttpNodes(client);


			//testTwitterIndexOps(client);
			//System.out.println("Refreshing twitter indices:" + client.dmlClient.refresh("twitter2","twitter4"));
			
			testCAT(client);
			
			testExistingMapping(client);


		} catch (Exception e) {
			if (e instanceof ESException) {
				System.out.println(((ESException) e).errorStatus());
			}
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste);
			}
		}

		finally {
			client.close();
		}

	}

	private static void testExistingMapping(AdiRestClientDriver client) throws IOException {
		JsonGenerator mappingSpec = client.createTwitterMappingSpec(client.adminClient.getESVersion());
		
		mappingSpec.flush();
		MappingExistRequest request = new MappingExistRequest("twitter",
				"type1");
		//MappingExistResponse response = client.adminClient.mappingExists(request);

		/* check if mapping exists in index */
		if (true /*response.exists()*/) {
			System.out.println(" MAPPING EXISTS FOR TWITTER/TYPE1");
			GetMappingRequest mappingReq = new GetMappingRequest("twitter", "type1");
			GetMappingResponse mappingResp = client.adminClient.getMapping(mappingReq);
			System.out.println("Existing Mapping:" + mappingResp.mapping());
			if(ESRestClientUtil.isMappingResponseEqual(
					mappingResp.mapping(), mappingSpec, "twitter", "type1")) {
				System.out.println(" TWITTER/TYPE1 HAS MAPPING EXISTING EXACTLY AS SAME AS TRYING TO CREATE NOW. NOT CREATING IT.");
			}
		}
	}

	private static void testCAT(AdiRestClientDriver client) throws IOException {
		System.out.println(" *********** CAT INDEX OPERATIONS  ************");
        CATResponse resp = client.adminClient.catInfo(CATRequest.API.INDICES, null, "twit", null, null);
        System.out.println(" CAT Resp statusCode: " + resp.statusCode());
        System.out.println(" CAT Resp: " + resp);
        List<String> indices = new ArrayList<String>();
        for(Map<String,Object> map: resp.getResponseMaps()) {
       	 indices.add((String) map.get("index"));
        }
        System.out.println("Indices starting with twit are:" + indices);
		
	}

	private static void testTwitterIndexOps(AdiRestClientDriver client) throws IOException {
		System.out.println(" *********** TWITTER INDEX OPERATIONS  ************");
		client.createTwitterIndex(client.adminClient.getESVersion());
		client.indexTweets();
		client.createTwitterMapping(client.adminClient.getESVersion());
		client.bulkIndexTweets();
		System.out.println("Refreshing twitter indices:" + client.dmlClient.refresh("twitter2","twitter4"));
		
	}

	private static void testHttpNodes(AdiRestClientDriver client) {
		try {
			GetHttpNodesRequest getNodes = new GetHttpNodesRequest();
			GetHttpNodesResponse nodesResp = client.monitorClient.getHttpNodesResponse(getNodes);
			System.out.println("Availables Nodes:" + nodesResp);
			System.out.println(" Availables Nodes statusCode: " + nodesResp.statusCode());
		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index not created due to:" + e);
			}
		}
		
	}

	private static void testIndexExists(AdiRestClientDriver client) {
		try {
			IndexExistRequest existsReq = new IndexExistRequest("adiclienttest5");
			IndexExistResponse existsResp = client.adminClient.indexExists(existsReq);
			System.out.println("adiclienttest5 exists:" + existsResp.exists());

			IndexExistRequest noexistsReq = new IndexExistRequest("adiclienttest9");
			IndexExistResponse noexistsResp = client.adminClient.indexExists(noexistsReq);
			System.out.println("adiclienttest9 exists:" + noexistsResp.exists());
			System.out.println(" adiclienttest9 exists statusCode: " + noexistsResp.statusCode());

		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index exists req failed due to:" + e);
			}
		}
		
	}

	private static void testGetMapping(AdiRestClientDriver client) {
		try {
			GetMappingRequest mapping = new GetMappingRequest("adiclienttest5", "type1");
			GetMappingResponse mappingResp = client.adminClient.getMapping(mapping);
			System.out.println("Mapping Response:" + mappingResp);
			System.out.println("Mapping Response status code:" + mappingResp.statusCode());
		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index not deleted due to:" + e);
			}
		}
		
	}

	private static void testDeleteIndex(AdiRestClientDriver client) {
		try {
			DeleteIndexRequest deleteIndex = new DeleteIndexRequest("adiclienttest3");
			DeleteIndexResponse dresp = client.adminClient.deleteIndex(deleteIndex);
			System.out.println("Index Deleted:" + dresp.isAcknowledged());
		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index not deleted due to:" + e);
			}
		}
		
	}
	
	

	private static void testPutMapping(AdiRestClientDriver client) {
		try {
			JsonGenerator jsonGen = createIndexMapping(false, client.adminClient.getESVersion());
			jsonGen.flush();
			String jsonString = new String(((ByteArrayOutputStream)jsonGen.getOutputTarget()).toByteArray(), "UTF-8");
			System.out.println(" ######################################################################################");
			System.out.println("jsonString of mapping:" + jsonString);
			System.out.println(" ######################################################################################");
			PutMappingRequest mappingReq = new PutMappingRequest("adiclienttest5", "type1",
					createIndexMapping(false, client.adminClient.getESVersion()));
			PutMappingResponse mresp = client.adminClient.createMapping(mappingReq);

			System.out.println("Mapping Created:" + mresp.isAcknowledged());
		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Mapping not created due to:" + e);
			}
		}
		
	}

	private static void testCreateIndex(AdiRestClientDriver client) {
		Map<String, String> indexSettings = new LinkedHashMap<String, String>();
		indexSettings.put("number_of_shards", "2");
		indexSettings.put("number_of_replicas", "3");

		// Thread.currentThread().sleep(6000);
		try {
			CreateIndexRequest createIndex = new CreateIndexRequest("adiclienttest5").settings(indexSettings);
			CreateIndexResponse cresp = client.adminClient.createIndex(createIndex);
			System.out.println("Index Created:" + cresp.isAcknowledged());
		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index not created due to:" + e);
			}
		}
		
	}
	
	

	private static void testClusterHealthIndexLevel(AdiRestClientDriver client) {
		try {
			ClusterHealthRequest req = new ClusterHealthRequest("twitter");
			ClusterHealthResponse resp = client.adminClient.clusterHealth(req);
			System.out.println("adiclienttest5 clusterHealth:" + resp.getClusterHealthStatus());
			System.out.println(" adiclienttest5 clusterHealthstatusCode: " + resp.statusCode());
			//ClusterHealthRequest req1 = new ClusterHealthRequest("ondb.kvlightstore._checkpoint");
			//ClusterHealthResponse resp1 = client.adminClient.clusterHealth(req1);
			//System.out.println("kvlightstore._checkpoint clusterHealth:" + resp1.getClusterHealthStatus());

		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index exists req failed due to:" + e);
			}
		}
	}
	
	private static void testClusterHealth(AdiRestClientDriver client) {
		try {
			ClusterHealthRequest req = new ClusterHealthRequest();
			ClusterHealthResponse resp = client.adminClient.clusterHealth(req);
			System.out.println("adiclienttest5 clusterHealth:" + resp.getClusterHealthStatus());
			System.out.println(" adiclienttest5 clusterHealthstatusCode: " + resp.statusCode());
			//ClusterHealthRequest req1 = new ClusterHealthRequest("ondb.kvlightstore._checkpoint");
			//ClusterHealthResponse resp1 = client.adminClient.clusterHealth(req1);
			//System.out.println("kvlightstore._checkpoint clusterHealth:" + resp1.getClusterHealthStatus());

		} catch (Exception e) {
			if (e instanceof ESException || e instanceof IOException) {
				System.out.println("Index exists req failed due to:" + e);
			}
		}
	}

	public IndexDocumentResponse index(String index, String type, String id, Map<String, String> source)
			throws IOException {
		IndexDocumentRequest ir = new IndexDocumentRequest();
		ir.id(id);
		ir.index(index);
		ir.type(type);
		byte[] sourceBytes = ((ByteArrayOutputStream) (JsonUtil.map(source).getOutputTarget())).toByteArray();
		ir.source(sourceBytes);
		return dmlClient.index(ir);

	}

	public void createTwitterIndex(String esVersion) throws IOException {
		Map<String, String> indexSettings = new LinkedHashMap<String, String>();
		indexSettings.put("number_of_shards", "2");
		indexSettings.put("number_of_replicas", "1");
		CreateIndexResponse cresp = null;
		try{
		CreateIndexRequest createIndex = new CreateIndexRequest("twitter4").settings(indexSettings);
		  cresp = adminClient.createIndex(createIndex);
		} catch(Exception e) {
			System.out.println(" Twitter Index Creation Exception..........");
			if (e.getCause() instanceof ESException) {
				
				if(((ESException)e.getCause()).errorType().contains("already_exist")) {
					System.out.println("Twitter Index Already exist, move ahead");
					return;
				} 
			} 
				throw e;
			
		}
		System.out.println(" Twitter Index created:" + cresp.isAcknowledged());
       
	}
	
	public void createTwitterMapping(String esVersion) throws IOException {
		 JsonGenerator jsonGen = createTwitterMappingSpec(esVersion);

			PutMappingRequest mappingReq = new PutMappingRequest("twitter4", "type1", jsonGen);
			PutMappingResponse mresp = adminClient.createMapping(mappingReq);

			System.out.println("Twitter Mapping Created:" + mresp.isAcknowledged());
	}

	private JsonGenerator createTwitterMappingSpec(String esVersion)
			throws IOException {
		String[] keyFields = {"id"};
			String[] fields = { "name", "user_name", "message" };
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ESJsonBuilder jsonBuilder = new ESJsonBuilder(baos);
			JsonGenerator jsonGen = jsonBuilder.jsonGenarator();

			jsonGen.writeStartObject(); // Mapping Start
			jsonGen.writeBooleanField("dynamic", false);
			jsonBuilder.startStructure("properties");
			for (String keyField : keyFields) {
				System.out.println("ES Version:" + esVersion);
				if (esVersion.compareTo("5") > 0) {
					jsonBuilder.field(keyField, getMappingInfoType("keyword"));
				} else {
					jsonBuilder.field(keyField, getMappingInfoType("string"));
				}
			}
			for (String indexField : fields) {
				System.out.println("ES Version:" + esVersion);
				if (esVersion.compareTo("5") > 0) {
					jsonBuilder.field(indexField, getMappingInfoType("text"));
				} else {
					jsonBuilder.field(indexField, getMappingInfoType("string"));
				}
			}

			jsonGen.writeEndObject(); // Proerties structure end
			jsonGen.writeEndObject(); // mapping structure end
		return jsonGen;
	}

	public void indexTweets() throws IOException {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("id", "one");
		source.put("name", "adione");
		String[] phones = { "1234", "5678" };
		source.put("phones", phones);
		source.put("city", "Blr");
		IndexDocumentRequest ir = new IndexDocumentRequest("twitter4", "type1", "1").source(source);
		IndexDocumentResponse resp = dmlClient.index(ir);
		System.out.println("tweet index doc resp:" + resp);

		GetRequest getRequest1 = new GetRequest("twitter4", "type1", "1");
		System.out.println("Tweet Document 1" + dmlClient.get(getRequest1));
	}

	public void bulkIndexTweets() throws IOException {
		BulkRequest br = new BulkRequest();
		// Form BulkRequest
		for (Integer i = 2; i < 10; i++) {

			Map<String, Object> indexsource = new HashMap<String, Object>();
			indexsource.put("id", i.toString());
			indexsource.put("name", "adi" + i.toString());
			String[] phones = { "1234-" + i, "5678-" + i };
			indexsource.put("phones", phones);
			indexsource.put("city", "Blr");
			IndexDocumentRequest ir = new IndexDocumentRequest();
			ir.id(i.toString());
			ir.index("twitter4");
			ir.type("type1");
			ir.source(indexsource);
			br.add(ir);
		}

		for (Integer i = 5; i < 7; i++) {
			DeleteRequest dr = new DeleteRequest();
			br.add(dr.index("twitter").type("type1").id(i.toString()));

		}

		BulkResponse bulkResponse = dmlClient.bulk(br);
		System.out.println(bulkResponse);

		GetRequest getRequest1 = new GetRequest("twitter4", "type1", "4");
		GetRequest getRequest2 = new GetRequest("twitter4", "type1", "9");

		System.out.println("Get Response 1" + dmlClient.get(getRequest1));
		System.out.println("Get Response 2" + dmlClient.get(getRequest2));

		System.out.println("Refreshing twitter4 index:" + dmlClient.refresh("twitter4"));
		String searchQuery = "*";
		SearchRequest sr = new SearchRequest("twitter4", "type1", searchQuery);
		sr.source(true);
		sr.storedFields(null);
		System.out.println("Search Response" + dmlClient.search(sr));

	}

	public void close() {
		esNodeMonitor2.stop();
		esHttpClient.close();
		monitorClient.close();

	}

	public static JsonGenerator createIndexMapping(boolean emptyMap, String esVersion) throws IOException {

		String[] keyFields = { "id", "name" };
		String[] indexFields = { "user_name", "message" };
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ESJsonBuilder jsonBuilder = new ESJsonBuilder(baos);
		JsonGenerator jsonGen = jsonBuilder.jsonGenarator();

		if (emptyMap) {
			jsonGen.writeStartObject();
			jsonGen.writeEndObject();
			return jsonGen;
		}

		jsonGen.writeStartObject(); // Mapping Start
		jsonGen.writeBooleanField("dynamic", false);
		jsonBuilder.startStructure("properties").startStructure("_pkey");
		jsonGen.writeBooleanField("enabled", false);
		jsonBuilder.startStructure("properties").startStructure("_table");
		if (esVersion.compareTo("5") > 0) {
			jsonGen.writeStringField("type", "keyword");
		} else {
			jsonGen.writeStringField("type", "string");
		}
		jsonGen.writeEndObject(); // end _table structure
		for (String keyField : keyFields) {
			System.out.println("ES Version:" + esVersion);
			if (esVersion.compareTo("5") > 0) {
				jsonBuilder.field(keyField, getMappingInfoType("keyword"));
			} else {
				jsonBuilder.field(keyField, getMappingInfoType("string"));
			}
		}

		jsonGen.writeEndObject(); // end pkey properties
		jsonGen.writeEndObject(); // end pkey

		for (String indexField : indexFields) {
			if (esVersion.compareTo("5") > 0) {
				jsonBuilder.field(indexField, getMappingInfoType("text"));
			} else {
				jsonBuilder.field(indexField, getMappingInfoType("string"));
			}
		}

		jsonGen.writeEndObject(); // top leve properties end
		jsonGen.writeEndObject(); // mapping structure end

		return jsonGen;

	}

	public static Map<String, Object> getMappingInfoType(String type) {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("type", type);
		return map;
	}

	/*
	 * void createESIndex(String esIndexName) throws IllegalStateException,
	 * ParseException, IOException {
	 * 
	 * Map<String,String> index_settings = new HashMap<String,String>();
	 * index_settings.put("number_of_shards", "2");
	 * index_settings.put("number_of_replicas", "1");
	 * 
	 * String json = new ObjectMapper().writeValueAsString(index_settings);
	 * System.out.println(json);
	 * 
	 * HttpEntity settings = new NStringEntity(json,
	 * ContentType.APPLICATION_JSON);
	 * 
	 * Response indexResponse = restClient.performRequest("PUT",
	 * "/"+esIndexName, Collections.EMPTY_MAP, settings);
	 * 
	 * System.out.println(EntityUtils.toString(indexResponse.getEntity()));
	 * 
	 * System.out.println("ES index " + esIndexName + " created"); }
	 */

	public void init() {
		esHttpClient = new ESHttpClientBuilder(new HttpHost("127.0.0.1", 7200, "http")).setMaxRetryTimeoutMillis(12000)
				.build();
        esRestClient = new ESRestClient(esHttpClient);
		dmlClient = esRestClient.dml();
		adminClient = esRestClient.admin();
		// esNodeMonitor = new ESNodeMonitorOld(adminClient,1000L);
		// restClient.setFailureListener(esNodeMonitor);
		// esNodeMonitor.start();
		ESHttpClient httpMonitorClient = new ESHttpClientBuilder(new HttpHost("127.0.0.1", 7200, "http"))
				.setMaxRetryTimeoutMillis(12000).build();
		monitorClient = new MonitoringClient(httpMonitorClient, new ESLatestResponse());
		List<ESHttpClient> registeredESHttpClients = new ArrayList<ESHttpClient>();
		registeredESHttpClients.add(httpMonitorClient);
		registeredESHttpClients.add(esHttpClient);
		esNodeMonitor2 = new ESNodeMonitor(monitorClient, 1000L, registeredESHttpClients);
		httpMonitorClient.setFailureListener(esNodeMonitor2);
		esHttpClient.setFailureListener(esNodeMonitor2);

		esNodeMonitor2.start();

	}

}
