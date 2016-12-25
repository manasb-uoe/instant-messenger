package models.socketmessages;

import models.ChatMessage;

/**
 * Created by manasb on 13-11-2016.
 */
public class ChatMessageSocketMessage extends SocketMessage {

    private ChatMessage chatMessage;

    public ChatMessageSocketMessage(ChatMessage chatMessage) {
        super(MessageType.CHAT_MESSAGE, chatMessage);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
