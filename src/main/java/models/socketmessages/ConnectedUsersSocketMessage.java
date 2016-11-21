package models.socketmessages;

import models.User;
import models.socketmessages.MessageType;
import models.socketmessages.SocketMessage;

import java.util.List;

/**
 * Created by manasb on 13-11-2016.
 */
public final class ConnectedUsersSocketMessage extends SocketMessage {

    private final List<User> connectedUsers;

    public ConnectedUsersSocketMessage(List<User> connectedUsers) {
        super(MessageType.CONNECTED_USERS, connectedUsers);
        this.connectedUsers = connectedUsers;
    }

    public List<User> getConnectedUsers() {
        return connectedUsers;
    }
}
