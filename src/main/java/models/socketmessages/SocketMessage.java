package models.socketmessages;

import utils.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manasb on 13-11-2016.
 */
public class SocketMessage {

    private final MessageType messageType;
    private final Object data;

    public SocketMessage(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public final MessageType getMessageType() {
        return messageType;
    }

    public final String toJson() {
        final Map<String, Object> map = new HashMap<>();
        map.put("messageType", messageType.name());
        map.put("data", data);
        return Util.gson.toJson(map);
    }

    public static SocketMessage parse(String json) {
        return Util.gson.fromJson(json, SocketMessage.class);
    }
}
