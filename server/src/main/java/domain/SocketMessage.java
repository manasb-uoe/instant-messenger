package domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import utils.Util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by manasb on 13-11-2016.
 */
public class SocketMessage<T> {

    private final MessageType messageType;
    private final T data;

    public SocketMessage(MessageType messageType, T data) {
        this.messageType = messageType;
        this.data = data;
    }

    public final MessageType getMessageType() {
        return messageType;
    }

    public final T getData() {
        return data;
    }

    public final String toJson() {
        final Map<String, Object> map = new HashMap<>();
        map.put("messageType", messageType.name());
        map.put("data", data);
        return Util.gson.toJson(map);
    }

    public static SocketMessage fromJson(String json) {
        final JsonParser jsonParser = new JsonParser();
        final JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();;
        final MessageType messageType = MessageType.valueOf(jsonObject.get("messageType").getAsString());
        Type type;

        switch (messageType) {
            case CONNECT:
                type = new TypeToken<SocketMessage<String>>() {}.getType();
                break;
            case CHAT_MESSAGE:
                type = new TypeToken<SocketMessage<ChatMessage>>() {}.getType();
                break;
            case CONNECTED_USERS:
                type = new TypeToken<SocketMessage<List<User>>>() {}.getType();
                break;
            case ERROR:
                type = new TypeToken<SocketMessage<String>>() {}.getType();
                break;
            default:
                throw new RuntimeException("Not implemented yet.");
        }

        return Util.gson.fromJson(json, type);
    }
}
