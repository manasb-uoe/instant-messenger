/**
 * Created by manasb on 12-11-2016.
 */

function setupWindowOnErrorHandler() {
    let errors = document.getElementById("errors");

    window.onerror = function(message, source, line, col, error) {
        let text = error ? error.stack || error : message + ' (at ' + source + ':' + line + ':' + col + ')';
        errors.textContent += text + '\n';
        errors.style.display = 'block';
    };
}

function createWebSocketConnection() {
    return connection = new WebSocket("ws://localhost:4567/chat");
}

function setupWebSocketHandlers(connection) {
    connection.onopen = function () {
        console.log("Connection open");
    }
}

//=====================

setupWindowOnErrorHandler();
let connection = createWebSocketConnection();
setupWebSocketHandlers(connection);
