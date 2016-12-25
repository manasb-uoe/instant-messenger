/**
 * Created by manasb on 25-12-2016.
 */
import {Component, OnInit, AfterViewChecked} from '@angular/core';
import {ChatMessage} from "../../domain/chat-message";
import {User} from "../../domain/user";
import {SocketMessageService} from "../../services/socket-message-service";
import {MessageFactory} from "../../domain/message-factory";

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewChecked {
  public chatMessages: ChatMessage[] = [];
  public currentUser: User;
  public chatInputText: string = "";

  public constructor(private socketMessageService: SocketMessageService) {}

  public ngOnInit(): void {
    this.socketMessageService.identity$.subscribe((user: User) => this.currentUser = user);
    this.socketMessageService.chatMessages$.subscribe((chatMessage: ChatMessage) => {
      this.chatMessages.push(chatMessage);
    });
  }

  public ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    const mainContainer = document.getElementById("main");
    mainContainer.scrollTop = mainContainer.scrollHeight;
  }

  public isCurrentUser(user: User) {
    return this.currentUser && this.currentUser.username === user.username;
  }

  public onSendButtonClick(event: Event): void {
    event.preventDefault();
    this.sendChatMessage();
  }

  public onInputKeyPress(event: KeyboardEvent): void {
    if (event.charCode == 13 /* ENTER */) {
      this.sendChatMessage();
    }
  }

  private sendChatMessage(): void {
    const messageText = this.chatInputText;

    if (!this.currentUser) {
      console.error("No current user found so cannot send message");
      return;
    }

    if (messageText.length == 0) {
      alert("Message should be more than 0 characters long");
      return;
    }

    const chatMessage = MessageFactory.createChatMessage(this.currentUser, messageText);
    this.socketMessageService.sendMessage(chatMessage);

    this.chatInputText = "";
  }

  public convertTimeStampToDate(timestamp: number): Date {
    return new Date(timestamp);
  }
}
