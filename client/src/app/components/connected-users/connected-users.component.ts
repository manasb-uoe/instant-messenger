/**
 * Created by manasb on 25-12-2016.
 */
import { Component, OnInit } from '@angular/core';
import { SocketMessageService } from "../../services/socket-message-service";
import {User} from "../../domain/user";
import {DataService} from "../../services/data.service";

@Component({
  selector: 'connected-users',
  templateUrl: './connected-users.component.html',
  styleUrls: ['./connected-users.component.css']
})
export class ConnectedUsersComponent implements OnInit {
  public users: User[];

  constructor(
    private socketMessageService: SocketMessageService,
    private dataService: DataService
  ) {}

  ngOnInit(): void {
    this.socketMessageService.connectedUsers$.subscribe(users => this.users = users);
  }

  public isCurrentUser(user: User): Boolean {
    const currentUser = this.dataService.currentUser;
    return currentUser && user.username === currentUser.username;
  }
}
