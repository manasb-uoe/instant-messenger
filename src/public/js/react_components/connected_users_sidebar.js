/**
 * Created by manasb on 21-11-2016.
 */

let React = require("react");
let MessageType = require("../models/MessageType");
let EventBus = require("eventbusjs");

class ConnectedUsersSidebar extends React.Component {
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
        let listItems = [];
        this.state.connectedUsers.forEach((connectedUser, index) => {
            if (connectedUser.username === this.state.currentUser.username) {
                listItems.push(<li key={index} className="current-user">{connectedUser.username}</li>);
            } else {
                listItems.push(<li key={index}>{connectedUser.username}</li>);
            }
        });

        return (
            <div id="connected-users-sidebar">
                <h4>Connected Users</h4>
                <ul>{listItems}</ul>
            </div>
        );
    }
}

module.exports = ConnectedUsersSidebar;