package oracle.kv.impl.tif.esclient.esRequest;

public abstract class ESWriteRequest<T> extends ESRequest<T> implements ESRestRequestGenerator {

	public static final byte JSONItemSeperator = '\n';

	protected String id;
	protected String routing;
	protected long version;
	protected String refreshType = "false";

	public ESWriteRequest() {

	}

	public ESWriteRequest(String index, String type, String id) {
		super(index, type);
		this.id = id;
	}

	public InvalidRequestException validate() {
		InvalidRequestException exception = null;
		if (index == null || index.length() <= 0) {
			exception = new InvalidRequestException("index name is not provided");
		}
		if (type == null || type.length() <= 0) {
			exception = new InvalidRequestException("index type is not provided");
		}

		return exception;
	}

	/**
	 * Note that if "id" is null, it will be generated by ES, for index
	 * requests. However, currently ESHandler requires indexing operation to be
	 * idempotent, so id should be required. Semantically "id" is required but
	 * no validation exception will be thrown if it is null.
	 */
	public InvalidRequestException validateWithId() {
		InvalidRequestException exception = null;
		validate();
		if (id == null || id.length() <= 0) {
			exception = new InvalidRequestException("id is required for deletion.");
		}

		return exception;
	}

	public String refreshType() {
		return refreshType;
	}

	/**
	 * The id of the indexed document. If not set, will be automatically
	 * generated.
	 */
	public String id() {
		return id;
	}

	public long version() {
		return this.version;
	}

	/**
	 * Controls the shard routing of the delete request. Using this value to
	 * hash the shard and not the id.
	 */
	public String routing() {
		return this.routing;
	}

	@SuppressWarnings("unchecked")
	public T id(String id) {
		this.id = id;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T routing(String routing) {
		if (routing != null && routing.length() == 0) {
			this.routing = null;
		} else {
			this.routing = routing;
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T version(long version) {
		this.version = version;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T refreshType(String refreshType) {
		if (refreshType == null || refreshType.length() == 0) {
			refreshType = "false";
		}
		this.refreshType = refreshType;
		return (T) this;
	}

	public static String getContentType() {
		return "application/json; charset=UTF-8";
	}

}
