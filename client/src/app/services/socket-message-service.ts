/**
 * Created by manasb on 24-12-2016.
 */
import {Injectable, EventEmitter} from '@angular/core';
import {User} from "../domain/user";
import {MessageType} from "../domain/message-type";
import {ChatMessage} from "../domain/chat-message";
import {MessageSource} from "../domain/message-source";

@Injectable()
export class SocketMessageService {
  public connectedUsers$ = new EventEmitter<User[]>();
  public identity$ = new EventEmitter<User>();
  public chatMessages$ = new EventEmitter<ChatMessage>();

  private webSocket: WebSocket;
  private port: number;
  private webSocketEndpoint: string;
  private readonly stateOpen = 1;
  private readonly stateClose = 3;
  private readonly retryCounter = 4;

  public init(port: number, webSocketEndpoint: string) {
    this.port = port;
    this.webSocketEndpoint = webSocketEndpoint;
  }

  private bindSocketEventHandlers() {
    this.webSocket.onopen = (event) => this.onSocketOpen();
    this.webSocket.onclose = (event) => this.onSocketClose();
    this.webSocket.onmessage = (message) => this.onSocketMessage(message);
  }

  private onSocketOpen(): void {
    console.info("Socket connection opened");
  }

  private onSocketClose(): void {
    console.info("Socket connection closed");
  }

  private onSocketMessage(message: any): void {
    message = JSON.parse(message.data);
    switch (message.messageType) {
      case MessageType[MessageType.CONNECTED_USERS]:
        this.connectedUsers$.emit(message.data);
        break;
      case MessageType[MessageType.IDENTITY]:
        this.identity$.emit(message.data);
        break;
      case MessageType[MessageType.CHAT_MESSAGE]:
        // Convert string to enum before emitting
        message.data.source = MessageSource[message.data.source];
        this.chatMessages$.emit(message.data);
        break;
      default:
        console.error("Cannot handle messages of type [" + message.messageType + "]");
    }
  }

  public openSocketConnection(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.webSocket = new WebSocket("ws://" + window.location.hostname + ":" + this.port  + "/" +
        this.webSocketEndpoint);

      this.bindSocketEventHandlers();

      this.waitForSocketState(this.retryCounter, this.stateOpen, err => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  }

  public closeSocketConnection(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.webSocket) {
        return resolve();
      }

      this.webSocket.close();
      this.waitForSocketState(this.retryCounter, this.stateClose, err => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  }

  private waitForSocketState(retryCounter: number, state: number, callback) {
    let self = this;

    if (this.webSocket.readyState === state) {
      return callback();
    } else {
      if (retryCounter == 0) {
        return callback("Failed to reach state [" + state + "] after numerous attempts.");
      }

      setTimeout(() => self.waitForSocketState(--retryCounter, state, callback), 100);
    }
  }

  public sendSocketMessage(message: string): void {
    this.webSocket.send(message);
  }
}
