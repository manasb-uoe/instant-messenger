/**
 * Created by manasb on 23-12-2016.
 */
import {User} from "./user";
import {MessageSource} from "./message-source";

export class ChatMessage {
  public source: MessageSource;
  public user: User;
  public messageText: string;
  public timestamp: number;

    public constructor(source: MessageSource, user: User, messageText: string, timestamp: number) {
        this.source = source;
        this.user = user;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }
}
