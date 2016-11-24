/**
 * Created by manasb on 21-11-2016.
 */

let React = require('react');
let EventBus = require("eventbusjs");
let MessageType = require("../models/MessageType");

class Chat extends React.Component {
    constructor() {
        super();

        this.state = {
            chatMessages: []
        };

        EventBus.addEventListener(MessageType.CHAT_MESSAGE, this.onChatMessage, this);
    }

    onChatMessage(message) {
        this.setState({
            chatMessages: this.state.chatMessages.concat(message.target.data)
        });
    }

    render() {
        let items = [];
        this.state.chatMessages.forEach((chatMessage, index) => {
            items.push(
                <div key={index} className="message">
                    <div className="message-body">{chatMessage.message}</div>
                    <div className="message-author">{chatMessage.user.username}</div>
                    <div className="message-timestamp">{new Date(chatMessage.timestamp).toUTCString()}</div>
                </div>
            );
        });

        return (
            <div id="chat">
                <h4>Chat</h4>
                <ul>{items}</ul>
            </div>
        );
    }
}

module.exports = Chat;