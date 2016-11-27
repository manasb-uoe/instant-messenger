/**
 * Created by manasb on 24-11-2016.
 */

let EventBus = require("eventbusjs");
let config = require("../config");

let WebSocketHandler = (function() {

        let webSocket = undefined;

        function setupWindowOnErrorHandler() {
            let errors = document.getElementById("errors");

            window.onerror = function (message, source, line, col, error) {
                let text = error ? error.stack || error : message + ' (at ' + source + ':' + line + ':' + col + ')';
                errors.textContent += text + '\n';
                errors.style.display = 'block';
            };
        }

        function openSocketConnection() {
            return new WebSocket("ws://" + window.location.hostname + ":" + config.port + "/" + config.websocket_endpoint);
        }

        function setupWebSocketHandlers() {
            webSocket.onmessage = function (message) {
                const data = JSON.parse(message.data);
                EventBus.dispatch(data.messageType, data);
            };
        }

        function init() {
            setupWindowOnErrorHandler();
            webSocket = openSocketConnection();
            setupWebSocketHandlers(webSocket);
        }

        function getWebSocket() {
            return webSocket;
        }

        return {
            init: init,
            getWebSocket: getWebSocket
        }
}());

module.exports = WebSocketHandler;