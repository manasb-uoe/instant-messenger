/**
 * Created by manasb on 21-11-2016.
 */

let React = require('react');
let EventBus = require("eventbusjs");
let MessageType = require("../domain/MessageType");
var classnames = require("classnames");

class Chat extends React.Component {
    constructor() {
        super();

        this.state = {
            chatMessages: [],
            currentUser: undefined
        };

        EventBus.addEventListener(MessageType.CHAT_MESSAGE, this.onChatMessage, this);
        EventBus.addEventListener(MessageType.IDENTITY, this.onIdentityMessage, this);
    }

    onChatMessage(message) {
        this.setState({
            chatMessages: this.state.chatMessages.concat(message.target.data)
        });

        // Scroll to bottom of div
        const chatContainer = document.getElementById("chat-container");
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }

    onIdentityMessage(message) {
        this.setState({
            currentUser: message.target.data
        });
    }

    render() {
        let items = [];
        this.state.chatMessages.forEach((chatMessage, index) => {
            let messageContainerClasses = classnames({
                "message-container-mine": this.state.currentUser && this.state.currentUser.username === chatMessage.user.username,
                "message-container-not-mine": this.state.currentUser && this.state.currentUser.username !== chatMessage.user.username
            });
            let messageClasses = classnames({
                "message": true,
                "message-mine": this.state.currentUser && this.state.currentUser.username === chatMessage.user.username,
                "message-not-mine": this.state.currentUser && this.state.currentUser.username !== chatMessage.user.username
            });

            items.push(
                <div key={index} className={messageContainerClasses}>
                    <div className={messageClasses}>
                        <div className="message-body">{chatMessage.message}</div>
                        <div className="message-author">{chatMessage.user.username}</div>
                        <div className="message-timestamp">{new Date(chatMessage.timestamp).toUTCString()}</div>
                    </div>
                </div>
            );
        });

        return (
            <div>{items}</div>
        );
    }
}

module.exports = Chat;