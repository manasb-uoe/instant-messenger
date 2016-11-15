/**
 * Created by manasb on 15-11-2016.
 */

let MessageType = require("../models/MessageType");
let ChatMessage = require ("../models/ChatMessage");

class SocketMessageFactory {

    static createChatMessage(user, message) {
        let chatMessage = new ChatMessage(user, message, Date.now());
        return JSON.stringify({
            messageType: MessageType.CHAT_MESSAGE,
            body: chatMessage
        });
    }
}

module.exports = SocketMessageFactory;