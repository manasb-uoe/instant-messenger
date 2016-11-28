/**
 * Created by manasb on 21-11-2016.
 */

let React = require("react");
let MessageType = require("../domain/MessageType");
let EventBus = require("eventbusjs");
let EventBusClientEvents = require("../eventbus_client_events");
let classnames = require("classnames");

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

    onEditButtonClick(event, index) {
        event.preventDefault();
        const currentUsername = this.state.connectedUsers[index].username;
        EventBus.dispatch(EventBusClientEvents.EDIT_USERNAME, currentUsername);
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

            const editButtonJsx = isCurrentUser ?
                <button type="button" className="btn btn-sm btn-default edit-button"
                      onClick={(event) => this.onEditButtonClick(event, index)}>
                    <span className="glyphicon glyphicon-edit"/>
                </button>
                : undefined;

            listItems.push(
                <div key={index} className="user-container">
                    <span className={userClassNames}>{usernameText}</span>
                    {editButtonJsx}
                </div>
            );
        });

        return (
            <div id="connected-users-sidebar">
                {listItems}
            </div>
        );
    }
}

module.exports = ConnectedUsersSidebar;