/**
 * Created by manasb on 15-11-2016.
 */

class ChatMessage {

    constructor (user, message, timestamp) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
    }
}

module.exports = ChatMessage;