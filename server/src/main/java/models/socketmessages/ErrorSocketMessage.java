package models.socketmessages;

/**
 * Created by manasb on 13-11-2016.
 */
public class ErrorSocketMessage extends SocketMessage {
    private final String error;

    public ErrorSocketMessage(String error) {
        super(MessageType.ERROR, error);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
