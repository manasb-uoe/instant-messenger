import models.socketmessages.SocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
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

    @OnWebSocketConnect
    public void onConnect(Session session) {
        try {
            chatService.addUser(session);
        } catch (Exception e) {
            chatService.sendErrorToUser(e.getMessage(), session);
            log.error(e.getMessage(), e);
        }

        chatService.broadcastConnectedUsers();
        chatService.sendIdentityToSession(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        chatService.removeUser(session);
        chatService.broadcastConnectedUsers();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        log.info(message);
        SocketMessage socketMessage = SocketMessage.parse(message);

        switch (socketMessage.getMessageType()) {
            case CHAT_MESSAGE:
                chatService.broadcastMessage(socketMessage);
        }
    }
}