/**
 * Created by manasb on 15-11-2016.
 */

let WebSocketHandler = require("./websocket_handler");
let ConnectedUsersSidebar = require("./react_components/connected_users_sidebar");
let Chat = require("./react_components/chat");
let ChatInput = require("./react_components/chat_input");
let EditUsernameModal = require("./react_components/EditUsernameModal");
let ReactDom = require("react-dom");

WebSocketHandler.init();

ReactDom.render(
    <ConnectedUsersSidebar />,
    document.getElementById("connected-users-container")
);

ReactDom.render(
    <Chat />,
    document.getElementById("chat-messages-container")
);

ReactDom.render(
    <ChatInput />,
    document.getElementById("chat-input-container")
);

ReactDom.render(
    <EditUsernameModal />,
    document.getElementById("modals-container")
);