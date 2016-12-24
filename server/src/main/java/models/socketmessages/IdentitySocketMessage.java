package models.socketmessages;

import models.User;

/**
 * Created by manasb on 21-11-2016.
 */
public class IdentitySocketMessage extends SocketMessage {

    private final User user;

    public IdentitySocketMessage(User user) {
        super(MessageType.IDENTITY, user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
