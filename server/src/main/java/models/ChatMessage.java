package models;

/**
 * Created by manasb on 12-11-2016.
 */
public class ChatMessage {

    private final MessageSource source;
    private final User user;
    private final String messageText;
    private final long timestamp;

    public ChatMessage(MessageSource source, User user, String messageText, long timestamp) {
        this.source = source;
        this.user = user;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public MessageSource getSource() {
        return source;
    }

    public User getUser() {
        return user;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
