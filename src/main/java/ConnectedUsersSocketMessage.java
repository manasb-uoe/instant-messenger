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
}
