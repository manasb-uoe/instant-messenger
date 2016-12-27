/**
 * Created by manasb on 27-12-2016.
 */
import { Component } from '@angular/core';
import { ApiInteractionService } from "../../services/api-interaction.service";
import { SocketMessageService } from "../../services/socket-message-service";
import {MessageFactory} from "../../domain/message-factory";
import { Router } from '@angular/router';
import {User} from "../../domain/user";
import {DataService} from "../../services/data.service";

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html'
})
export class LandingPageComponent {
  public username: string = "";
  public error: string;

  public constructor(
    private apiInteractionService: ApiInteractionService,
    private socketMessageService: SocketMessageService,
    private dataService: DataService,
    private router: Router
  ) {}

  public onInputKeyPress(event: KeyboardEvent): void {
    if (event.charCode == 13 /* ENTER */) {
      this.addUser();
    }
  }

  public onGoButtonClick(event: Event): void {
    event.preventDefault();
    this.addUser();
  }

  private addUser(): void {
    this.error = null;
    const username = this.username;

    this.apiInteractionService.addUser(username)
      .then((user: User) => {
        this.dataService.currentUser = user;
        this.router.navigateByUrl("/chat");
      })
      .catch(error => this.error = error);
  }

}
