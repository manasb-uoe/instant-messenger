/**
 * Created by manasb on 24-12-2016.
 */
import {Injectable, EventEmitter} from '@angular/core';
import {User} from "../domain/user";
import {WebSocketWrapper} from "../websocket/websocket-wrapper";
import {IWebSocketEventHandlers} from "../websocket/websocket-event-handlers";
import {MessageType} from "../domain/message-type";
import {ChatMessage} from "../domain/chat-message";

@Injectable()
export class SocketMessageService implements IWebSocketEventHandlers{
  public connectedUsers$ = new EventEmitter<User[]>();
  public identity$ = new EventEmitter<User>();
  public chatMessages$ = new EventEmitter<ChatMessage>();
  private webSocketWrapper: WebSocketWrapper;

  public init(webSocketWrapper: WebSocketWrapper) {
    this.webSocketWrapper = webSocketWrapper;
    webSocketWrapper.setEventHandlers(this);
  }

  public onSocketOpen(): void {
    console.info("Socket connection opened");
  }

  public onSocketClose(): void {
    console.info("Socket connection closed");
  }

  public onSocketMessage(message: any): void {
    message = JSON.parse(message.data);

    switch (message.messageType) {
      case MessageType[MessageType.CONNECTED_USERS]:
        this.connectedUsers$.emit(message.data);
        break;
      case MessageType[MessageType.IDENTITY]:
        this.identity$.emit(message.data);
        break;
      case MessageType[MessageType.CHAT_MESSAGE]:
        this.chatMessages$.emit(message.data);
        break;
      default:
        console.error("Cannot handle messages of type [" + message.messageType + "]");
    }
  }

  public sendMessage(message: any): void {
    this.webSocketWrapper.getWebSocket().send(message);
  }
}
