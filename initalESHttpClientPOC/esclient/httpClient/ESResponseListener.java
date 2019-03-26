package oracle.kv.impl.tif.esclient.httpClient;

import oracle.kv.impl.tif.esclient.restClient.RestResponse;



public interface ESResponseListener {

    /**
     * Method invoked if the request yielded a successful response
     */
    void onSuccess(RestResponse response);

    /**
     * Method invoked if the request failed. There are two main categories of failures: connection failures (usually
     * {@link java.io.IOException}s, or responses that were treated as errors based on their error response code
     * ({@link ResponseException}s).
     */
    void onFailure(Exception exception);
    
    /**
     * ESResponseListener also serves as tracking listener.
     * The exception which allows retries are added as suppressed exception.
     * @param exception
     */
    void onRetry(Exception exception);
}

