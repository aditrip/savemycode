package oracle.kv.impl.tif.esclient.esRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.http.client.methods.HttpGet;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

/**
 * NoSQL FTS does not support search and this class is meant only for unit
 * tests.
 * 
 * This is an URI Search class with QueryStringQuery query specified in URL
 * with parameter "q".
 * 
 *
 */
public class SearchRequest extends ESRequest<SearchRequest> implements
        ESRestRequestGenerator {

    /* Parameters */
    private List<String> storedFields = Arrays.asList("id");
    private boolean _source = false; // Mostly search results only need id.
    private String sort = "id:asc"; // Typically search results should be
                                    // sorted by score.
    private int pageSize = 100; // page size for search results.
    private int fromIndex = 0; // Get results from index 0.
    private String queryString; // The query parameter. Html Encoded String.
    private String routing;  // Search subset of shards with routing param.

    public SearchRequest() {
        
    }
    
    public SearchRequest(String index, String type, String queryString) {
        super(index, type);
        Objects.requireNonNull(queryString);
        this.queryString = queryString;
    }

    public List<String> storedFields() {
        return storedFields;
    }

    public SearchRequest storedFields(List<String> storedFields) {
        this.storedFields = storedFields;
        return this;
    }

    public boolean is_source() {
        return _source;
    }

    public SearchRequest source(boolean _source) {
        this._source = _source;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public SearchRequest sort(String sort) {
        this.sort = sort;
        return this;
    }

    public int pageSize() {
        return pageSize;
    }

    public SearchRequest pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int from() {
        return fromIndex;
    }

    public SearchRequest from(int from) {
        this.fromIndex = from;
        return this;
    }

    public String queryString() {
        return queryString;
    }

    public SearchRequest queryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public InvalidRequestException validate() {
        InvalidRequestException exception = null;
        if (index == null || index.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index name is not provided");
        }
        if (type == null || type.length() <= 0) {
            exception = new InvalidRequestException(
                                                    "index type is not provided");
        }
        return exception;
    }

    @Override
    public RestRequest generateRestRequest() {
        validate();
        String endpoint = endpoint(index(), type(), "_search");

        RequestParams params = new RequestParams();
        params.queryString(queryString);
        params.routing(routing);
        params.storedFields(storedFields);
        params.fetchSource(_source);
        params.pageSize(pageSize);
        params.fromIndex(fromIndex);
        params.sort(sort);


        return new RestRequest(HttpGet.METHOD_NAME, endpoint, null, params.getParams());
    }

    @Override
    public RequestType requestType() {
        return RequestType.SEARCH;
    }

}
