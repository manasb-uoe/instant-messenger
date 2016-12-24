package models;

/**
 * Created by manasb on 12-11-2016.
 */
public class ChatMessage {

    private final User source;
    private final String message;
    private final long timestamp;

    public ChatMessage(User source, String message, long timestamp) {
        this.source = source;
        this.message = message;
        this.timestamp = timestamp;
    }

    public User getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
