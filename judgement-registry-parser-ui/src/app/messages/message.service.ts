import { Injectable } from '@angular/core';
import { timer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  messages: string[] = [];

  add(message : string) {
    this.messages.push(message);
    let source = timer(5000).subscribe(() => this.messages.pop());
  }

  clear() {
    this.messages = [];
  }

  constructor() { }
}
