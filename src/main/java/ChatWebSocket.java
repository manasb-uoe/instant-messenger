import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by manasb on 12-11-2016.
 */
@WebSocket
public class ChatWebSocket {

    private final ChatController chatController = ChatController.getInstance();
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        try {
            chatController.addUser(session);
        } catch (Exception e) {
            chatController.sendErrorToUser(e.getMessage(), session);
            log.error(e.getMessage(), e);
        }

        chatController.sendConnectedUsersListToUser(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        chatController.removeUser(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            SocketMessage socketMessage = SocketMessage.parse(message);
            if (socketMessage.getMessageType() == SocketMessage.MessageType.CHAT_MESSAGE) {
                chatController.broadcastMessage(socketMessage);
            } else {
                throw new NotImplementedException();
            }
        } catch (Exception e) {
            String error = "Cannot parse message sent by client: " + message;
            chatController.sendErrorToUser(error, session);
            log.error(error, e);
        }
    }
}
