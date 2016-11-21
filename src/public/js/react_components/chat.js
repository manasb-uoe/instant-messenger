/**
 * Created by manasb on 21-11-2016.
 */

let React = require('react');
let EventBus = require("eventbusjs");
let MessageType = require("../models/MessageType");

class Chat extends React.Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div id="chat">
                <h4>Chat</h4>
            </div>
        );
    }
}

module.exports = Chat;