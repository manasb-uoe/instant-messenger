/**
 * Created by manasb on 15-11-2016.
 */

let SocketMessageFactory = require('./factories/SocketMessageFactory');
let ChatMessage = require("./models/ChatMessage");
let MessageType = require("./models/MessageType");
let User = require("./models/User");
let ConnectedUsersSidebar = require("./react_components/connected_users_sidebar");
let Chat = require("./react_components/chat");
let ReactDom = require("react-dom");
let EventBus = require("eventbusjs");

class App {

    constructor(port) {
        this.port = port;
        this.wsEndPoint = "chat";
    }

    _setupWindowOnErrorHandler() {
        let errors = document.getElementById("errors");

        window.onerror = function (message, source, line, col, error) {
            let text = error ? error.stack || error : message + ' (at ' + source + ':' + line + ':' + col + ')';
            errors.textContent += text + '\n';
            errors.style.display = 'block';
        };
    }

    _openSocketConnection() {
        return new WebSocket("ws://localhost:" + this.port + "/" + this.wsEndPoint);
    }

    _setupWebSocketHandlers(connection) {
        connection.onopen = function () {
            var message = SocketMessageFactory.createChatMessage(new User("manasb"), "Hello world!");
            console.log(message);
            connection.send(message);
        };

        connection.onmessage = function (message) {
            const data = JSON.parse(message.data);
            console.log(data.messageType);
            EventBus.dispatch(data.messageType, data);
        };
    }

    start() {
        this._setupWindowOnErrorHandler();
        this.connection = this._openSocketConnection();
        this._setupWebSocketHandlers(this.connection);
    }
}


//==========================

let app = new App(4567);
app.start();

ReactDom.render(
    <ConnectedUsersSidebar />,
    document.getElementById("connected-users-sidebar-container")
);

// ReactDom.render(
//     <Chat />,
//     document.getElementById("chat-container")
// );