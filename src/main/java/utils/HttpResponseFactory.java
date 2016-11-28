package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manasb on 28-11-2016.
 */
public class HttpResponseFactory {

    private static final String statusCodeKey = "statusCode";
    private static final String dataKey = "data";
    private static final String errorKey = "error";

    public static String createOkResponse(Object data) {
        final Map<String, Object> map = new HashMap<>();
        map.put(statusCodeKey, 200);
        if (data != null) {
            map.put(dataKey, data);
        }

        return Util.gson.toJson(map);
    }

    public static String createBadRequestResponse(String error) {
        final Map<String, Object> map = new HashMap<>();
        map.put(statusCodeKey, 400);
        if (error != null) {
            map.put(errorKey, error);
        }

        return Util.gson.toJson(map);
    }
}
