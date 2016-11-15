/**
 * Created by manasb on 15-11-2016.
 */

let SocketMessageFactory = require('./factories/SocketMessageFactory');
let ChatMessage = require("./models/ChatMessage");
let User = require("./models/User");

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
            console.log("Connection open");
            connection.send(SocketMessageFactory.createChatMessage(new User("manasb"), "Hello world!"));
        };

        connection.onmessage = function (message) {
            console.log(JSON.parse(message.data));
        };
    }

    start() {
        this._setupWindowOnErrorHandler();
        this.connection = this._openSocketConnection();
        this._setupWebSocketHandlers(this.connection);
    }
}


//==========================

new App(4567).start();