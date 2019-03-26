package oracle.kv.impl.tif.esclient.esRequest;

import java.util.List;

import org.apache.http.client.methods.HttpGet;

import oracle.kv.impl.tif.esclient.restClient.RestRequest;

public class GetRequest extends ESRequest<GetRequest> implements ESRestRequestGenerator {
	private String id;
	private List<String> storedFields;
	private String routing;
	private boolean refresh = false;

	public GetRequest(String index, String id) {
		super(index, "_all");
		this.id = id;
	}

	public GetRequest(String index, String type, String id) {
		super(index, type);
		this.id = id;
	}

	public GetRequest id(String id) {
		this.id = id;
		return this;
	}

	public String id() {
		return id;
	}

	public GetRequest routing(String routing) {
		this.routing = routing;
		return this;
	}

	public String routing() {
		return routing;
	}

	/**
	 * By default refresh is false. Set it to true if a refresh should be
	 * executed before Get This is to get the latest values.
	 * 
	 * Mainly meant for unit test currently.
	 */
	public GetRequest refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}

	public boolean refresh() {
		return this.refresh;
	}

	/**
	 * By default _source field is returned. It can be overridden by specifying
	 * explicitly the stored fields to be returned.
	 */
	public GetRequest storedFields(List<String> storedFields) {
		this.storedFields = storedFields;
		return this;
	}

	public List<String> storedFields() {
		return this.storedFields;
	}

	public String toString() {
		return "{ Get:" + index + " ,type:" + type + " ,id:" + id + " }";
	}

	public InvalidRequestException validate() {
		InvalidRequestException exception = null;
		if (index == null || index.length() <= 0) {
			exception = new InvalidRequestException("index name is not provided");
		}
		if (type == null || type.length() <= 0) {
			exception = new InvalidRequestException("index type is not provided");
		}
		if (id == null || id.length() <= 0) {
			exception = new InvalidRequestException("id is required for deletion.");
		}

		return exception;
	}

	@Override
	public RestRequest generateRestRequest() {
		validate();
		String endpoint = endpoint(index(), type(), "_search");

		RequestParams params = new RequestParams();
		params.routing(routing);
		params.storedFields(storedFields);
		params.refresh(refresh());
		Boolean fetchSource = Boolean.TRUE;
		if (storedFields != null && storedFields.size() > 0) {
			fetchSource = Boolean.FALSE;
		}
		params.fetchSource(fetchSource);

		return new RestRequest(HttpGet.METHOD_NAME, endpoint, null, params.getParams());
	}

    @Override
    public RequestType requestType() {
        // TODO Auto-generated method stub
        return RequestType.GET;
    }
}
