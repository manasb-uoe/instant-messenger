import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { RootComponent } from './components/root/root.component';
import { SocketMessageService } from "./services/socket-message-service";
import {ConfigService} from "./services/config-service";

@NgModule({
  declarations: [
    RootComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [ConfigService, SocketMessageService],
  bootstrap: [RootComponent]
})
export class AppModule { }
