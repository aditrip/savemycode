package oracle.kv.impl.tif.esclient.httpClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;


public class ESHttpClientBuilder {
    
    /*
     * It is possible to set the timeouts for different kind
     * of clients. 
     * 
     * However, for now only default timeouts are used in all clients.
     * 
     */

    public static final int CONNECTION_TIMEOUT_MILLIS = 2000; // 1000;
    public static final int SOCKET_TIMEOUT_MILLIS = 2000; // 30000;
    public static final int MAX_RETRY_TIMEOUT_MILLIS = 6000;


    private static final Header[] EMPTY_HEADERS = new Header[0];

    private final List<HttpHost> hosts;
    private int maxRetryTimeout = MAX_RETRY_TIMEOUT_MILLIS;
    private int connectionTimeout = CONNECTION_TIMEOUT_MILLIS;
    private int socketTimeout = SOCKET_TIMEOUT_MILLIS;
    private Header[] defaultHeaders = EMPTY_HEADERS;
    private ESHttpClient.FailureListener failureListener;


    private SecurityConfigCallback securityConfigCallback;

    private String pathPrefix;

    public ESHttpClientBuilder(HttpHost... hosts) {
        if (hosts == null || hosts.length == 0) {
            throw new IllegalArgumentException("no hosts provided");
        }
        for (HttpHost host : hosts) {
            if (host == null) {
                throw new IllegalArgumentException("host cannot be null");
            }
        }
        this.hosts = Arrays.asList(hosts);
    }


    public ESHttpClientBuilder setSecurityConfigCallback(SecurityConfigCallback securityConfigCallback) {
        Objects.requireNonNull(securityConfigCallback,
                               "securityConfigCallback must not be null");
        this.securityConfigCallback = securityConfigCallback;
        return this;
    }

    public ESHttpClientBuilder setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        if (connectionTimeoutMillis <= 0) {
            throw new IllegalArgumentException(
                                               "connectionTimeoutMillis must be greater than 0");
        }
        this.connectionTimeout = connectionTimeoutMillis;
        return this;
    }
    
    public ESHttpClientBuilder setSocketTimeoutMillis(int socketTimeoutMillis) {
        if (socketTimeoutMillis <= 0) {
            throw new IllegalArgumentException(
                                               "socketTimeoutMillis must be greater than 0");
        }
        this.socketTimeout = socketTimeoutMillis;
        return this;
    }
    
    public ESHttpClientBuilder setMaxRetryTimeoutMillis(int maxRetryTimeoutMillis) {
        if (maxRetryTimeoutMillis <= 0) {
            throw new IllegalArgumentException(
                                               "maxRetryTimeoutMillis must be greater than 0");
        }
        this.maxRetryTimeout = maxRetryTimeoutMillis;
        return this;
    }

    public ESHttpClientBuilder setFailureListener(ESHttpClient.FailureListener failureListener) {
        
       this.failureListener = failureListener;
       return this;
       
    }

    
    public ESHttpClientBuilder pathPrefix(String pathPrefix) {
        Objects.requireNonNull(pathPrefix, "pathPrefix must not be null");

        String prefix = pathPrefix;

        if (prefix.startsWith("/") == false) {
            prefix = "/" + prefix;
        }

        if (prefix.endsWith("/")) {
            prefix = prefix.substring(0,prefix.length() - 1);
        }
        if (prefix.isEmpty() || "/".equals(prefix)) {
            throw new IllegalArgumentException(
                                               "pathPrefix must not be empty or '/': ["
                                                       + pathPrefix + "]");
        }
        this.pathPrefix = prefix;
        return this;
    }
    
    public ESHttpClientBuilder setDefaultHeaders(Header[] defaultHeaders) {
        Objects.requireNonNull(defaultHeaders,
                               "defaultHeaders must not be null");
        for (Header defaultHeader : defaultHeaders) {
            Objects.requireNonNull(defaultHeader,
                                   "default header must not be null");
        }
        this.defaultHeaders = defaultHeaders;
        return this;
    }
    
   

    public ESHttpClient build() {

        CloseableHttpAsyncClient httpClient = createHttpClient();
        ESHttpClient esClient = new ESHttpClient(httpClient, maxRetryTimeout,
                                                 defaultHeaders, hosts,
                                                 pathPrefix, failureListener);
        httpClient.start();
        return esClient;
    }

    private CloseableHttpAsyncClient createHttpClient() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                                                                  .setConnectTimeout(connectionTimeout)
                                                                  .setSocketTimeout(socketTimeout)
                                                                  .setConnectionRequestTimeout(connectionTimeout);


        HttpAsyncClientBuilder httpClientBuilder = HttpAsyncClientBuilder.create()
                                                                         .setDefaultRequestConfig(requestConfigBuilder.build());
        if (securityConfigCallback != null) {
            httpClientBuilder = securityConfigCallback.addSecurityConfig(httpClientBuilder);
        }
        return httpClientBuilder.build();
    }


    /**
     * This callback is used to set up SSL Context for https over
     * the HttpAsyncClientBuilder.
     * Allows to add to the AsyncHttpClientBuilder by setting additional attributes
     * of the client like CredentialsProvider, SSLContext etc.
     */
    public interface SecurityConfigCallback {
   
        HttpAsyncClientBuilder addSecurityConfig(HttpAsyncClientBuilder httpClientBuilder);
    }

}
