package oracle.kv.impl.tif.esclient.httpClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;


import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


public class AdiTransportClientDriver {
	
	public TransportClient tClient = null;
	public AdminClient esAdminClient = null;

	public static void main(String[] args) throws UnknownHostException {
		 AdiTransportClientDriver testClient = new AdiTransportClientDriver();
		 testClient.initClients();
		 testClient.createESIndex("aditestindex1");
		 testClient.close();

	}
	
	public void close() {
		tClient.close();
	}
	public void initClients() throws UnknownHostException {
		final Settings settings = Settings.builder().
	            put("cluster.name", "oracle_kv").
	           // put("client.transport.sniff", true).
	            build();

	        final TransportClient client =
	            new PreBuiltTransportClient(settings, Collections.emptyList());
	        client.addTransportAddress(new TransportAddress
                     (InetAddress.getLoopbackAddress(), 9300));
	        tClient = client;
	        esAdminClient = client.admin();
	}
	
	 
	 void createESIndex(String esIndexName)
		        throws IllegalStateException {

		        Settings.Builder sb = Settings.builder();
		            final String shards = "1";
		            final String replicas =  "1";
		                sb.put("number_of_shards", shards);
		                sb.put("number_of_replicas", replicas);

		        try {
		            CreateIndexResponse createResponse =
		                esAdminClient.indices().
		                create(new CreateIndexRequest(esIndexName, sb.build())).
		                actionGet();

		            if (!createResponse.isAcknowledged()) {
		                throw new IllegalStateException("Fail to create ES index " +
		                                                esIndexName);
		            }
		        } catch (Exception e) {
		            /*
		             * That is OK; multiple repnodes will all try to create the index
		             * at the same time, only one of them can win.
		             */
		        	System.out.println(e);
		        }

		        System.out.println("ES index " + esIndexName + " created");
		    }


}
