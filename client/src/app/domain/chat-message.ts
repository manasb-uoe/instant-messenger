/**
 * Created by manasb on 23-12-2016.
 */
import {User} from "./user";

export class ChatMessage {
  public source: string;
  public user: User;
  public messageText: string;
  public timestamp: number;

    public constructor(source: string, user: User, messageText: string, timestamp: number) {
        this.source = source;
        this.user = user;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }
}
