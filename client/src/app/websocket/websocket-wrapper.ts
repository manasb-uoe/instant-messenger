/**
 * Created by manasb on 23-12-2016.
 */
import {IWebSocketEventHandlers} from "./websocket-event-handlers";

export class WebSocketWrapper {
  private readonly webSocket: WebSocket;

  public constructor(port: number, webSocketEndpoint: string) {
    this.webSocket = new WebSocket("ws://" + window.location.hostname + ":" + port  + "/" + webSocketEndpoint);
  }

  public getWebSocket(): WebSocket {
    return this.webSocket;
  }

  public setEventHandlers(webSocketHandlers: IWebSocketEventHandlers) {
    this.webSocket.onopen = (event) => webSocketHandlers.onSocketOpen();
    this.webSocket.onclose = (event) => webSocketHandlers.onSocketClose();
    this.webSocket.onmessage = (message) => webSocketHandlers.onSocketMessage(message);
  }
}
