import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by manasb on 12-11-2016.
 */
public final class Chat {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocket.class);
    private static Chat instance;
    private final Map<Session, User> sessionUserMap;
    private final AtomicInteger currentUserNumber;

    private Chat() {
        sessionUserMap = new ConcurrentHashMap<>();
        currentUserNumber = new AtomicInteger(1);
    }

    public synchronized static Chat getInstance() {
        if (instance == null) {
            instance = new Chat();
        }

        return instance;
    }

    public void addUser(Session session) throws Exception {
        final String username = "User " + currentUserNumber.getAndIncrement();

        if (doesUsernameAlreadyExist(username)) {
            throw new Exception("Another user with the same username already exists.");
        }

        final User user = new User(username);
        sessionUserMap.put(session, user);

        log.info(username + " connected.");
    }

    public void removeUser(Session session) {
        final Optional<User> userToRemove = Optional.of(sessionUserMap.get(session));
        userToRemove.ifPresent(user -> log.info(user.getUsername() + " disconnected."));

        sessionUserMap.remove(session);
    }

    private boolean doesUsernameAlreadyExist(String username) {
        for (Map.Entry<Session, User> entry : sessionUserMap.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }
}
