/**
 * Created by manasb on 27-12-2016.
 */

import { Injectable } from '@angular/core';
import { Http, Response } from "@angular/http";
import { ConfigService } from "./config-service";
import { Observable } from "rxjs/Rx";
import {User} from "../domain/user";

@Injectable()
export class ApiInteractionService {

  public constructor(
    private httpService: Http,
    private configService: ConfigService
  ) {}

  private getBaseUrl(): string {
    return "http://" + window.location.hostname + ":" + this.configService.getPort();
  }

  public addUser(username: string): Promise<User> {
    const url = this.getBaseUrl() + "/api/add-user";

    return new Promise((resolve, reject) => {
      this.httpService.post(url, username)

        .map((response: Response) => response.json())

        .catch((error: Response) => {
          console.error(error);
          return Observable.throw(error.json().error || "Error adding user.");
        })

        .subscribe(
          (response: Object) => {
            if (response['statusCode'] === 200) {
              resolve(response['data']);
            } else {
              reject(response["error"]);
            }
          },
          error => {
            console.error(error);
            reject(error);
          }
        );
    });
  }

}

