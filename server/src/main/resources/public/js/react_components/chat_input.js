/**
 * Created by manasb on 24-11-2016.
 */

let React = require('react');
let EventBus = require("eventbusjs");
let MessageType = require("../domain/MessageType");
let WebSocketHandler = require("../websocket_handler");
let SocketMessageFactory = require("../factories/SocketMessageFactory");

class ChatInput extends React.Component {
    constructor() {
        super();

        this.state = {
            currentUser: undefined,
            chatInputText: ""
        };

        EventBus.addEventListener(MessageType.IDENTITY, this.onIdentityMessage, this);

        this.onHandleChatInputChange = this.onHandleChatInputChange.bind(this);
        this.onSendButtonClick = this.onSendButtonClick.bind(this);
        this.onInputKeyPress = this.onInputKeyPress.bind(this);
    }

    onIdentityMessage(message) {
        this.setState({
            currentUser: message.target.data
        });
    }

    onSendButtonClick(event) {
        event.preventDefault();
        this.sendChatMessage();
    }

    onInputKeyPress(target) {
        if (target.charCode == 13) {
            this.sendChatMessage();
        }
    }

    sendChatMessage() {
        if (!this.state.currentUser) {
            alert("No current user found");
            return;
        }

        const chatInputText = this.state.chatInputText;

        if (!chatInputText || chatInputText.length == 0) {
            alert("Message should be more than 0 characters long");
            return;
        }

        const chatMessage = SocketMessageFactory.createChatMessage(this.state.currentUser, chatInputText);
        WebSocketHandler.getWebSocket().send(chatMessage);

        this.setState({
            chatInputText: ""
        });
    }

    onHandleChatInputChange(event) {
        this.setState({
            chatInputText: event.target.value
        });
    }

    render() {
        return (
            <div className="input-group">
                <input onKeyPress={this.onInputKeyPress} onChange={this.onHandleChatInputChange}
                       value={this.state.chatInputText} type="text" className="form-control"
                       placeholder="Type your message here"/>
                <span className="input-group-btn">
                    <button onClick={this.onSendButtonClick} className="btn btn-default" type="button">Send</button>
                </span>
            </div>
        );
    }
}

module.exports = ChatInput;