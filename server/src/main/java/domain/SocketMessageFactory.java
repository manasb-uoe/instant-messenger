package domain;

import java.util.List;

/**
 * Created by manasb on 26-12-2016.
 */
public final class SocketMessageFactory {

    public static SocketMessage<ChatMessage> createChatMessage(final ChatMessage chatMessage) {
        return new SocketMessage<>(MessageType.CHAT_MESSAGE, chatMessage);
    }

    public static SocketMessage<String> createErrorMessage(final String error) {
        return new SocketMessage<>(MessageType.ERROR, error);
    }

    public static SocketMessage<List<User>> createConnectedUsersMessage(final List<User> connectedUsers) {
        return new SocketMessage<>(MessageType.CONNECTED_USERS, connectedUsers);
    }
}
