/**
 * Created by manasb on 26-12-2016.
 */
import { Component, OnInit } from '@angular/core';
import { WebSocketWrapper } from "../../websocket/websocket-wrapper";
import { ConfigService } from "../../services/config-service";
import { SocketMessageService } from "../../services/socket-message-service";

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppRootComponent implements OnInit {
  constructor(
    private socketMessageService: SocketMessageService,
    private configService: ConfigService
  ) {}

  ngOnInit(): void {
    this.configService.load().then(() => {
      const webSocketWrapper = new WebSocketWrapper(
        this.configService.getPort(),
        this.configService.getWebSocketEndpoint()
      );
      this.socketMessageService.init(webSocketWrapper);
    });
  }
}
