package utils;

import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manasb on 28-11-2016.
 */
public class HttpResponseFactory {

    private static final String statusCodeKey = "statusCode";
    private static final String dataKey = "data";
    private static final String errorKey = "error";

    public static String createOkResponse(Response response, Object data) {
        final Map<String, Object> map = new HashMap<>();
        map.put(statusCodeKey, 200);
        if (data != null) {
            map.put(dataKey, data);
        }

        response.status(200);
        return Util.gson.toJson(map);
    }

    public static String createBadRequestResponse(Response response, String error) {
        final Map<String, Object> map = new HashMap<>();
        map.put(statusCodeKey, 400);
        if (error != null) {
            map.put(errorKey, error);
        }

        response.status(400);
        return Util.gson.toJson(map);
    }
}
