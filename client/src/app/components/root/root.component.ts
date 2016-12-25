import { Component, OnInit } from '@angular/core';
import { SocketMessageService } from "../../services/socket-message-service";
import { WebSocketWrapper } from "../../websocket/websocket-wrapper";
import { ConfigService } from "../../services/config-service";

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit{
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
