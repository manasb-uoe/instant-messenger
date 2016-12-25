import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { RootComponent } from './components/root/root.component';
import { SocketMessageService } from "./services/socket-message-service";
import { ConfigService } from "./services/config-service";
import { ConnectedUsersComponent } from "./components/connected-users/connected-users.component";
import { ChatComponent } from "./components/chat/chat.component";
import { MomentModule } from "angular2-moment";

@NgModule({
  declarations: [
    RootComponent,
    ConnectedUsersComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MomentModule
  ],
  providers: [ConfigService, SocketMessageService],
  bootstrap: [RootComponent]
})
export class AppModule { }
