package models.socketmessages;

import models.User;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectedUsersSocketMessage that = (ConnectedUsersSocketMessage) o;

        return connectedUsers != null ? connectedUsers.equals(that.connectedUsers) : that.connectedUsers == null;
    }

    @Override
    public int hashCode() {
        return connectedUsers != null ? connectedUsers.hashCode() : 0;
    }
}
