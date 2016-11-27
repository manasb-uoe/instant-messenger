/**
 * Created by manasb on 21-11-2016.
 */

let React = require("react");
let MessageType = require("../domain/MessageType");
let EventBus = require("eventbusjs");
var classnames = require("classnames");

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
            let isCurrentUser = this.state.currentUser && this.state.currentUser.username === connectedUser.username;

            let userClassNames = classnames({
                "user": true,
                "current-user": isCurrentUser
            });

            let usernameText = isCurrentUser ? connectedUser.username + " (you)" : connectedUser.username;

            listItems.push(<p key={index} className={userClassNames}>{usernameText}</p>);
        });

        return (
            <div id="connected-users-sidebar">
                {listItems}
            </div>
        );
    }
}

module.exports = ConnectedUsersSidebar;