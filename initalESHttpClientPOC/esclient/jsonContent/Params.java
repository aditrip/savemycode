package oracle.kv.impl.tif.esclient.jsonContent;

import java.util.Map;



public interface Params {
	String param(String key);

    String param(String key, String defaultValue);



   Params EMPTY_PARAMS = new Params() {
    @Override
    public String param(String key) {
        return null;
    }

    @Override
    public String param(String key, String defaultValue) {
        return defaultValue;
    }


};

class MapParams implements Params {

    private final Map<String, String> params;

    public MapParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String param(String key) {
        return params.get(key);
    }

    @Override
    public String param(String key, String defaultValue) {
        String value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

   
}

}
