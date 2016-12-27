/**
 * Created by manasb on 26-12-2016.
 */
import { Component, OnInit } from '@angular/core';
import {ConfigService} from "../../services/config-service";

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppRootComponent implements OnInit{

  public constructor(private configService: ConfigService) {}

  public ngOnInit(): void {
    this.configService.load().catch(error => console.error(error));
  }
}
