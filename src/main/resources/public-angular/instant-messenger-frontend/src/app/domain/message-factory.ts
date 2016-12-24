/**
 * Created by manasb on 23-12-2016.
 */

import { MessageType } from './message-type';
import { ChatMessage } from './chat-message';
import { User } from "./user";

export class MessageFactory {

  public static createChatMessage(user: User, messageText: string) {
    const chatMessage = new ChatMessage(user, messageText, Date.now());
    return JSON.stringify({
      messageType: MessageType[MessageType.CHAT_MESSAGE],
      data: chatMessage
    });
  }
}
