import {Component, OnInit, OnDestroy} from '@angular/core';
import {ConfigService} from "../../services/config-service";
import {SocketMessageService} from "../../services/socket-message-service";
import {DataService} from "../../services/data.service";
import {MessageFactory} from "../../domain/message-factory";
import {Router} from "@angular/router";
import {paths} from "../../app.paths";

@Component({
  selector: 'app-root',
  templateUrl: './chat-root.component.html',
  styleUrls: ['./chat-root.component.css']
})
export class ChatRootComponent implements OnInit, OnDestroy {

  public constructor(
    private configService: ConfigService,
    private socketMessageService: SocketMessageService,
    private dataService: DataService,
    private router: Router
  ) {}

  public ngOnInit(): void {
    const currentUser = this.dataService.currentUser;

    if (!currentUser) {
      this.router.navigateByUrl(paths.landingPage);
    } else {
      this.socketMessageService.init(this.configService.getPort(), this.configService.getWebSocketEndpoint());

      this.socketMessageService.openSocketConnection()
        .then(() => {
          const connectMessage: string = MessageFactory.createConnectMessage(currentUser.username);
          this.socketMessageService.sendSocketMessage(connectMessage);
        })
        .catch(error => console.error(error));
    }
  }

  public ngOnDestroy(): void {
      this.socketMessageService.closeSocketConnection()
        .then(() => this.dataService.currentUser = null)
        .catch(err => console.error(err));
  }
}
