/**
 * Created by manasb on 27-12-2016.
 */
import { Injectable } from '@angular/core';
import {User} from "../domain/user";

@Injectable()
export class DataService {
  public currentUser: User;
}
