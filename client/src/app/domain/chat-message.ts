/**
 * Created by manasb on 23-12-2016.
 */
import {User} from "./user";

export class ChatMessage {
  private user: User;
  private messageText: string;
  private timestamp: number;

    public constructor(user: User, messageText: string, timestamp: number) {
        this.user = user;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }
}
