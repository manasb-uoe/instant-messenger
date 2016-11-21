/**
 * Created by manasb on 21-11-2016.
 */

let React = require('react');
let EventBus = require("eventbusjs");
let MessageType = require("../models/MessageType");
let ConnectedUsersSidebar = require("./connected_users_sidebar");

class ChatApp extends React.Component {
    constructor() {
        super();
        this.state = {
            connectedUsers: [],
            currentUser: {username: "manas"}
        };

        EventBus.addEventListener(MessageType.CONNECTED_USERS, this.onConnectedUsersMessage, this);
    }

    onConnectedUsersMessage(message) {
        this.setState({
            connectedUsers: message.target.data
        });
    }

    render() {
        return (
            <div className="connected-users-sidebar">
                <ConnectedUsersSidebar
                    connectedUsers={this.state.connectedUsers}
                    currentUser={this.state.currentUser}/>
            </div>
        );
    }
}

module.exports = ChatApp;