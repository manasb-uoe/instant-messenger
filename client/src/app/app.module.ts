import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

import { ChatRootComponent } from './components/chat-root/chat-root.component';
import { SocketMessageService } from "./services/socket-message-service";
import { ConfigService } from "./services/config-service";
import { ConnectedUsersComponent } from "./components/connected-users/connected-users.component";
import { ChatComponent } from "./components/chat/chat.component";
import { MomentModule } from "angular2-moment";
import { AppRootComponent } from "./components/app-root/app-root.component";
import { PageNotFoundComponent } from "./components/pagenotfound/pagenotfound.component.";
import { LandingPageComponent } from "./components/landing-page/landing-page.component";
import { ApiInteractionService } from "./services/api-interaction.service";
import { DataService } from "./services/data.service";
import {paths} from "./app.paths";

@NgModule({
  declarations: [
    AppRootComponent,
    LandingPageComponent,
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
    RouterModule.forRoot([
      {
        path: paths.landingPage,
        component: LandingPageComponent
      },
      {
        path: paths.chat,
        component: ChatRootComponent
      },
      {
        path: '',
        redirectTo: paths.landingPage,
        pathMatch: 'full'
      },
      {
        path: '**',
        component: PageNotFoundComponent
      }
    ])
  ],
  providers: [
    ConfigService,
    SocketMessageService,
    ApiInteractionService,
    DataService
  ],
  bootstrap: [AppRootComponent]
})
export class AppModule { }
