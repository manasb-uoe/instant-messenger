/**
 * Created by manasb on 15-11-2016.
 */

let MessageType = require("../domain/MessageType");
let ChatMessage = require ("../domain/ChatMessage");

class SocketMessageFactory {

    static createChatMessage(user, message) {
        let chatMessage = new ChatMessage(user, message, Date.now());
        return JSON.stringify({
            messageType: MessageType.CHAT_MESSAGE,
            data: chatMessage
        });
    }
}

module.exports = SocketMessageFactory;