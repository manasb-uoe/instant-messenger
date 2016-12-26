package websocket;

import domain.User;
import domain.SocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ChatService;

import java.util.Optional;

/**
 * Created by manasb on 12-11-2016.
 */
@WebSocket
public class ChatWebSocket {

    private final ChatService chatService = ChatService.getInstance();
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        try {
            final User newUser = chatService.addUser(session);
            chatService.broadcastConnectedUsers();
            chatService.sendIdentityToSession(session);
            chatService.broadcastUserConnectedSystemMessage(newUser);
        } catch (Exception e) {
            chatService.sendErrorToUser(e.getMessage(), session);
            log.error(e.getMessage(), e);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Optional<User> removedUserOptional = chatService.removeUser(session);
        removedUserOptional.ifPresent(user -> {
            chatService.broadcastConnectedUsers();
            chatService.broadcastUserDisconnectedSystemMessage(user);
        });
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        log.info(message);
        SocketMessage socketMessage = SocketMessage.fromJson(message);

        switch (socketMessage.getMessageType()) {
            case CHAT_MESSAGE:
                chatService.broadcastMessage(socketMessage);
        }
    }
}
