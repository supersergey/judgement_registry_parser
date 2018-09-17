import { Injectable } from '@angular/core';
import { timer } from 'rxjs';

export enum MessageType {
  NORMAL,
  ERROR
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  messages: string[] = [];
  errorMessages: string[] = [];

  add(message : string, type: MessageType) {
    if (type === MessageType.ERROR) {
      this.errorMessages.push(message);
      this.startExpirationTimer(this.errorMessages);
    } else {
      this.messages.push(message);
      this.startExpirationTimer(this.messages);
    }
  }

  private startExpirationTimer(messages: string[]) {
    timer(5000).subscribe(() => messages.pop());
  }

  constructor() { }
}
