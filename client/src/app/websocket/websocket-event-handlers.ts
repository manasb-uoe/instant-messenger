/**
 * Created by manasb on 24-12-2016.
 */

export interface IWebSocketEventHandlers {
  onSocketOpen(): void;
  onSocketClose(): void;
  onSocketMessage(message: any): void;
}
