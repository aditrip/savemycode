package oracle.kv.impl.tif.esclient.jsonContent;

import com.fasterxml.jackson.core.JsonLocation;

public class JsonParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final int UNKNOWN_POSITION = -1;
    private final int lineNumber;
    private final int columnNumber;

    public JsonParseException(JsonLocation location,
            String msg,
            Object... args) {
        this(location, msg, null, args);
    }

    public JsonParseException(JsonLocation location,
            String msg,
            Throwable cause,
            Object... args) {
        super(msg, cause);
        int lineNumber = UNKNOWN_POSITION;
        int columnNumber = UNKNOWN_POSITION;
        if (location != null) {
            lineNumber = location.getLineNr();
            columnNumber = location.getColumnNr();
        }
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

}
