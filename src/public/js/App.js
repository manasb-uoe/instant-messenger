/**
 * Created by manasb on 15-11-2016.
 */

let WebSocketHandler = require("./websocket_handler");
let ConnectedUsersSidebar = require("./react_components/connected_users_sidebar");
let Chat = require("./react_components/chat");
let ChatInput = require("./react_components/chat_input");
let ReactDom = require("react-dom");

WebSocketHandler.init();

ReactDom.render(
    <ConnectedUsersSidebar />,
    document.getElementById("connected-users-sidebar-container")
);

ReactDom.render(
    <Chat />,
    document.getElementById("chat-container")
);

ReactDom.render(
    <ChatInput />,
    document.getElementById("chat-input-container")
);