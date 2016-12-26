import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';

import { ChatRootComponent } from './components/chat-root/chat-root.component';
import { SocketMessageService } from "./services/socket-message-service";
import { ConfigService } from "./services/config-service";
import { ConnectedUsersComponent } from "./components/connected-users/connected-users.component";
import { ChatComponent } from "./components/chat/chat.component";
import { MomentModule } from "angular2-moment";
import { AppRootComponent } from "./components/app-root/app-root.component";
import { PageNotFoundComponent } from "./components/pagenotfound/pagenotfound.component.";

const routes: Routes = [
  {
    path: 'instant-messenger', component: ChatRootComponent
  },
  {
    path: '**', component: PageNotFoundComponent
  }
];

@NgModule({
  declarations: [
    AppRootComponent,
    ChatRootComponent,
    ConnectedUsersComponent,
    ChatComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MomentModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    ConfigService,
    SocketMessageService
  ],
  bootstrap: [AppRootComponent]
})
export class AppModule { }
