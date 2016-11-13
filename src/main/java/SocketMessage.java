import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manasb on 13-11-2016.
 */
public class SocketMessage {

    private final MessageType messageType;
    private final Object body;

    public SocketMessage(MessageType messageType, Object body) {
        this.messageType = messageType;
        this.body = body;
    }

    public enum MessageType {
        CHAT_MESSAGE, CONNECTED_USERS, ERROR
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String toJson() {
        final Map<String, Object> map = new HashMap<>();
        map.put("messageType", messageType.name());
        map.put("body", body);
        return Util.gson.toJson(map);
    }
}
