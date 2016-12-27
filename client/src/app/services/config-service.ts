/**
 * Created by manasb on 24-12-2016.
 */
import { Injectable } from '@angular/core';
import {Http, Response} from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class ConfigService {
  private config: Object;
  private configUrl = "config.json";
  private isLoaded: boolean = false;

  public constructor(private httpService: Http) {}

  public load(): Promise<void> {
    if (this.config) {
      return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
      this.httpService.get(this.configUrl)

        .map((res: Response) => res.json())

        .catch((error: Response) => {
          console.error(error);
          return Observable.throw(error.json().error || "Error loading config from server.");
        })

        .subscribe(
          configData => {
            this.config = configData;
            this.isLoaded = true;
            resolve();
          },
          error => reject(error)
        )
    });
  }

  private checkIfLoaded(): void {
    if (!this.isLoaded) {
      throw new Error("Config was not loaded. Call load() before accessing config.");
    }
  }

  public getPort(): number {
    this.checkIfLoaded();
    return this.config['server_port'];
  }

  public getWebSocketEndpoint(): string {
    this.checkIfLoaded();
    return this.config['websocket_endpoint'];
  }

}
