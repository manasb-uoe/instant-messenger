/**
 * Created by manasb on 25-12-2016.
 */
import { Component, OnInit } from '@angular/core';
import { SocketMessageService } from "../../services/socket-message-service";
import {User} from "../../domain/user";

@Component({
  selector: 'connected-users',
  templateUrl: './connected-users.component.html',
  styleUrls: ['./connected-users.component.css']
})
export class ConnectedUsersComponent implements OnInit {
  public users: User[];
  public currentUser: User;

  constructor(private socketMessageService: SocketMessageService) {}

  ngOnInit(): void {
    this.socketMessageService.connectedUsers$.subscribe(users => this.users = users);
    this.socketMessageService.identity$.subscribe(user => this.currentUser = user);
  }

  public isCurrentUser(user: User): Boolean {
    return this.currentUser && user.username === this.currentUser.username;
  }
}
