package server;

import domain.SocketMessage;
import domain.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ChatService;

/**
 * Created by manasb on 12-11-2016.
 */
@WebSocket
public class ChatWebSocket {

    private final ChatService chatService = ChatService.getInstance();
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        try {
            final User removedUser = chatService.removeUser(session);
            chatService.broadcastConnectedUsers();
            chatService.broadcastUserDisconnectedSystemMessage(removedUser);
        } catch (Exception e) {
            chatService.sendErrorToUser(e.getMessage(), session);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        log.info(message);
        SocketMessage socketMessage = SocketMessage.fromJson(message);

        switch (socketMessage.getMessageType()) {
            case CONNECT:
                try {
                    final String username = (String) socketMessage.getData();
                    final User user = chatService.bindSessionToUser(session, username);
                    chatService.broadcastConnectedUsers();
                    chatService.broadcastUserConnectedSystemMessage(user);
                } catch (Exception e) {
                    chatService.sendErrorToUser(e.getMessage(), session);
                    log.error(e.getMessage(), e);
                }
                break;
            case CHAT_MESSAGE:
                chatService.broadcastMessage(socketMessage);
                break;
            default:
                log.error(String.format("Cannot handle message of type [%s]", socketMessage.getMessageType()));
        }
    }
}
