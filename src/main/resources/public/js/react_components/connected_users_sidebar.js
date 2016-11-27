/**
 * Created by manasb on 21-11-2016.
 */

let React = require("react");
let MessageType = require("../domain/MessageType");
let EventBus = require("eventbusjs");

class ConnectedUsersSidebar extends React.Component {
    constructor() {
        super();

        this.state = {
            connectedUsers: [],
            currentUser: undefined
        };

        EventBus.addEventListener(MessageType.CONNECTED_USERS, this.onConnectedUsersMessage, this);
        EventBus.addEventListener(MessageType.IDENTITY, this.onIdentityMessage, this);
    }

    onConnectedUsersMessage(message) {
        this.setState({
            connectedUsers: message.target.data
        });
    }

    onIdentityMessage(message) {
        this.setState({
            currentUser: message.target.data
        });
    }

    render() {
        let listItems = [];
        this.state.connectedUsers.forEach((connectedUser, index) => {
            if (this.state.currentUser && this.state.currentUser.username === connectedUser.username) {
                listItems.push(<p key={index} className="current-user">{connectedUser.username}</p>);
            } else {
                listItems.push(<p key={index}>{connectedUser.username}</p>);
            }
        });

        return (
            <div id="connected-users-sidebar">
                {listItems}
            </div>
        );
    }
}

module.exports = ConnectedUsersSidebar;