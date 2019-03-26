package oracle.kv.impl.tif.esclient.esRequest;


/**
 * Base Parametrized class for Request objects.
 * 
 * All Request objects also implement the Builder pattern, so the base class has
 * builder setter methods for two common properties, in all request objects -
 * index and type. They return the parameter type.
 * 
 * Currently FTS models an index on a table. And consraints iteself to one
 * search request on one index only.
 * 
 * When search over indices is added, same can be changed to indices[] Also,
 * null value for indices[] would mean search all indices and all types.
 * 
 * One interface to be enforced semantically is that every concrete ESRequest
 * subtype is also a builder, so all set methods should return the "this" object
 * instance.
 * 
 *
 */
public abstract class ESRequest<T> {

	protected String index;
	protected String type;

	public ESRequest() {
	}

	public ESRequest(String index, String type) {
		this.index = index;
		this.type = type;
	}

	public String index() {
		return index;
	}

	public String type() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public T type(String type) {
		this.type = type;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T index(String index) {
		this.index = index;
		return (T) this;
	}
	
	public abstract RequestType requestType();
	
	public static enum RequestType {
            INDEX, DELETE,
            /* For now FTS does not use CREATE/UPDATE operation */
            CREATE, UPDATE,
            BULK_INDEX,GET,SEARCH,
            CREATE_INDEX, PUT_MAPPING,
            GET_MAPPING, EXIST_INDEX,
            MAPPING_EXIST,
            DELETE_INDEX,ES_VERSION,
            CLUSTER_HEALTH, GET_NODES,
            CAT, REFRESH;

            public static RequestType get(String reqTypeStr) {
                    for (RequestType reqType : RequestType.values()) {
                            if (reqTypeStr.toLowerCase().equals(reqType.toString().toLowerCase())) {
                                    return reqType;
                            }
                    }
                    return null;
            }
    }

}
