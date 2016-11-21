import models.socketmessages.ConnectedUsersSocketMessage;
import models.socketmessages.ErrorSocketMessage;
import models.socketmessages.SocketMessage;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by manasb on 12-11-2016.
 */
public final class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);
    private static ChatController instance;
    private final Map<Session, User> sessionUserMap;
    private final AtomicInteger currentUserNumber;

    private ChatController() {
        sessionUserMap = new ConcurrentHashMap<>();
        currentUserNumber = new AtomicInteger(1);
    }

    public synchronized static ChatController getInstance() {
        if (instance == null) {
            instance = new ChatController();
        }

        return instance;
    }

    public void addUser(final Session session) throws Exception {
        final String username = "User " + currentUserNumber.getAndIncrement();

        if (doesUsernameAlreadyExist(username)) {
            throw new Exception("Another user with the same username already exists.");
        }

        final User user = new User(username);
        sessionUserMap.put(session, user);

        log.info(username + " connected.");
    }

    public void removeUser(final Session session) {
        final Optional<User> userToRemove = Optional.of(sessionUserMap.get(session));
        userToRemove.ifPresent(user -> log.info(user.getUsername() + " disconnected."));

        sessionUserMap.remove(session);
    }

    private boolean doesUsernameAlreadyExist(final String username) {
        for (Map.Entry<Session, User> entry : sessionUserMap.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    private List<User> getConnectedUsers() {
        return new ArrayList<>(sessionUserMap.values());
    }

    public void broadcastConnectedUsers()  {
        final SocketMessage socketMessage = new ConnectedUsersSocketMessage(getConnectedUsers());

        sessionUserMap.keySet().forEach(session -> {
            try {
                session.getRemote().sendString(socketMessage.toJson());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendErrorToUser(final String error, final Session session) {
        final SocketMessage socketMessage = new ErrorSocketMessage(error);
        try {
            session.getRemote().sendString(socketMessage.toJson());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMessage(final SocketMessage socketMessage) {
        sessionUserMap.keySet().forEach(session -> {
            try {
                session.getRemote().sendString(socketMessage.toJson());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
