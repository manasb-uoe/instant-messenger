package services;

import models.ChatMessage;
import models.MessageSource;
import models.User;
import models.socketmessages.*;
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
    final AtomicInteger currentUserNumber;
    private final User systemUser = new User("System");

    private ChatService() {
        sessionUserMap = Collections.synchronizedMap(new LinkedHashMap<>());
        currentUserNumber = new AtomicInteger(1);
    }

    public synchronized static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }

        return instance;
    }

    public User addUser(final Session session) throws Exception {
        final String username = "User " + currentUserNumber.getAndIncrement();

        if (doesUsernameAlreadyExist(username)) {
            throw new Exception("Another user with the same username already exists.");
        }

        final User user = new User(username);
        sessionUserMap.put(session, user);

        log.info(username + " connected.");

        return user;
    }

    public Session updateUsername(final String existingUsername, final String newUsername) throws Exception {
        final Optional<Map.Entry<Session, User>> sessionUserEntryToUpdateOptional = sessionUserMap.entrySet().stream()
                .filter(sessionUserEntry -> sessionUserEntry.getValue().getUsername().equals(existingUsername))
                .findFirst();

        if (!sessionUserEntryToUpdateOptional.isPresent()) {
            throw new Exception(String.format("No user with username [%s] exists", existingUsername));
        }

        if (doesUsernameAlreadyExist(newUsername)) {
            throw new Exception("Another user with the same username already exists.");
        }

        Map.Entry<Session, User> sessionUserEntryToUpdate = sessionUserEntryToUpdateOptional.get();
        sessionUserEntryToUpdate.getValue().setUsername(newUsername);
        return sessionUserEntryToUpdate.getKey();
    }

    public Optional<User> removeUser(final Session session) {
        final Optional<User> removedUserOptional = Optional.of(sessionUserMap.remove(session));
        removedUserOptional.ifPresent(user -> log.info(user.getUsername() + " disconnected."));
        return removedUserOptional;
    }

    public void broadcastConnectedUsers()  {
        broadcastMessage(new ConnectedUsersSocketMessage(getConnectedUsers()));
    }

    public void sendErrorToUser(final String error, final Session session) {
        sendMessage(session, new ErrorSocketMessage(error));
    }

    public void broadcastMessage(final SocketMessage socketMessage) {
        sessionUserMap.keySet().forEach(session -> {
            sendMessage(session, socketMessage);
        });
    }

    public void sendIdentityToSession(final Session session) {
        IdentitySocketMessage identitySocketMessage =
                new IdentitySocketMessage(sessionUserMap.get(session));
        sendMessage(session, identitySocketMessage);
    }

    public void broadcastUserConnectedSystemMessage(final User user) {
        final ChatMessage chatMessage = new ChatMessage(
                MessageSource.SYSTEM, systemUser,
                user.getUsername() + " has joined the chat.",
                System.currentTimeMillis()
        );
        broadcastMessage(new ChatMessageSocketMessage(chatMessage));
    }

    public void broadcastUserDisconnectedSystemMessage(final User user) {
        final ChatMessage chatMessage = new ChatMessage(
                MessageSource.SYSTEM, systemUser,
                user.getUsername() + " has left the chat.",
                System.currentTimeMillis()
        );
        broadcastMessage(new ChatMessageSocketMessage(chatMessage));
    }

    private void sendMessage(final Session session, final SocketMessage socketMessage) {
        try {
            session.getRemote().sendString(socketMessage.toJson());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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

}
