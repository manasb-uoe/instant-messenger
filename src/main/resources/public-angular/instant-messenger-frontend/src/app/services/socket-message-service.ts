/**
 * Created by manasb on 24-12-2016.
 */
import {Injectable, EventEmitter} from '@angular/core';
import {User} from "../domain/user";
import {WebSocketWrapper} from "../websocket/websocket-wrapper";
import {IWebSocketEventHandlers} from "../websocket/websocket-event-handlers";

@Injectable()
export class SocketMessageService implements IWebSocketEventHandlers{
  public connectedUsers$: EventEmitter<User[]>;
  private webSocketWrapper: WebSocketWrapper;

  public init(webSocketWrapper: WebSocketWrapper) {
    this.webSocketWrapper = webSocketWrapper;
    webSocketWrapper.setEventHandlers(this);
  }

  public onConnectedUsersMessage(message: any) {
    this.connectedUsers$.emit(message.target.data);
  }

  public onSocketOpen(): void {
    console.log("Opened");
  }

  public onSocketClose(): void {
    console.log("Closed");
  }

  public onSocketMessage(message: any): void {
    console.log("Message: " + message);
  }
}
