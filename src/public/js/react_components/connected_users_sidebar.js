/**
 * Created by manasb on 21-11-2016.
 */

let React = require("react");

class ConnectedUsersSidebar extends React.Component {
    render() {
        let listItems = [];
        this.props.connectedUsers.forEach((connectedUser, index) => {
            if (connectedUser.username === this.props.currentUser.username) {
                listItems.push(<li key={index} className="current-user">{connectedUser.username}</li>);
            } else {
                listItems.push(<li key={index}>{connectedUser.username}</li>);
            }
        });

        return (
            <ul>{listItems}</ul>
        );
    }
}

module.exports = ConnectedUsersSidebar;