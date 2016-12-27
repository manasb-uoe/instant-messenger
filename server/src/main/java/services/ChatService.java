package services;

import domain.*;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by manasb on 12-11-2016.
 */
public final class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private static ChatService instance;
    final Map<Session, User> sessionUserMap;
    final List<User> connectedUsers;
    final AtomicInteger currentUserNumber;
    private final User systemUser = new User("System");

    private ChatService() {
        sessionUserMap = Collections.synchronizedMap(new LinkedHashMap<>());
        currentUserNumber = new AtomicInteger(1);
        connectedUsers = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }

        return instance;
    }

    public User addUser(final String username) throws Exception {
        validateNewUsername(username);

        final User newUser = new User(username);
        connectedUsers.add(newUser);

        return newUser;
    }

    private void validateNewUsername(String username) throws Exception {
        if (username.length() == 0) {
            throw new Exception("Username must be at least 1 character long.");
        }

        if (username.length() > 20) {
            throw new Exception("Username must be at most 20 characters long.");
        }

        if (doesUsernameAlreadyExist(username)) {
            throw new Exception("Another user with the same username already exists.");
        }
    }

    public User bindSessionToUser(final Session session, final String username) throws Exception {
        final Optional<Map.Entry<Session, User>> sessionUserEntryToUpdateOptional = sessionUserMap.entrySet().stream()
                .filter(sessionUserEntry -> sessionUserEntry.getValue().getUsername().equals(username))
                .findFirst();

        if (sessionUserEntryToUpdateOptional.isPresent()) {
            throw new Exception(String.format("User with username [%s] is already bound to a session.", username));
        }

        final Optional<User> existingUserOptional = connectedUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if (!existingUserOptional.isPresent()) {
            throw new Exception(String.format("No user with username [%s] exists.", username));
        }

        final User user = existingUserOptional.get();
        sessionUserMap.put(session, user);

        log.info(username + " connected.");

        return user;
    }

    public User removeUser(final Session session) throws Exception {
        final Optional<User> removedUserOptional = Optional.of(sessionUserMap.remove(session));
        if (!removedUserOptional.isPresent()) {
            throw new Exception("Failed to remove user since session does not exist.");
        }

        final User user = removedUserOptional.get();
        connectedUsers.remove(user);

        log.info(user.getUsername() + " disconnected.");

        return user;
    }

    public void broadcastConnectedUsers() {
        broadcastMessage(SocketMessageFactory.createConnectedUsersMessage(connectedUsers));
    }

    public void sendErrorToUser(final String error, final Session session) {
        sendMessage(session, SocketMessageFactory.createErrorMessage(error));
    }

    public void broadcastMessage(final SocketMessage socketMessage) {
        sessionUserMap.keySet().forEach(session -> {
            sendMessage(session, socketMessage);
        });
    }

    public void sendIdentityToSession(final Session session) {
        final User user = sessionUserMap.get(session);
        sendMessage(session, SocketMessageFactory.createIdentityMessage(user));
    }

    public void broadcastUserConnectedSystemMessage(final User user) {
        final ChatMessage chatMessage = new ChatMessage(
                MessageSource.SYSTEM, systemUser,
                user.getUsername() + " has joined the chat.",
                System.currentTimeMillis()
        );
        broadcastMessage(SocketMessageFactory.createChatMessage(chatMessage));
    }

    public void broadcastUserDisconnectedSystemMessage(final User user) {
        final ChatMessage chatMessage = new ChatMessage(
                MessageSource.SYSTEM, systemUser,
                user.getUsername() + " has left the chat.",
                System.currentTimeMillis()
        );
        broadcastMessage(SocketMessageFactory.createChatMessage(chatMessage));
    }

    private void sendMessage(final Session session, final SocketMessage socketMessage) {
        try {
            session.getRemote().sendString(socketMessage.toJson());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean doesUsernameAlreadyExist(final String username) {
        for (User user : connectedUsers) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }
}
