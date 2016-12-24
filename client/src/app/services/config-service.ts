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

  constructor(private httpService: Http) {}

  load(): Promise<void> {
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
            resolve();
          },
          error => reject(error)
        )
    });
  }

  getPort(): number {
    return this.config['server_port'];
  }

  getWebSocketEndpoint(): string {
    return this.config['websocket_endpoint'];
  }

}
