import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by manasb on 12-11-2016.
 */
@WebSocket
public class ChatWebSocket {

    private final Chat chat = Chat.getInstance();
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        try {
            chat.addUser(session);
            chat.sendConnectedUsersListToUser(session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        chat.removeUser(session);
    }
}
