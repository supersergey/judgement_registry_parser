import { Injectable } from '@angular/core';
import { Keyword } from './keyword';
import { KEYWORDS } from './mock-keywords';
import { Observable, of } from 'rxjs';
import { MessageService} from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class KeywordService {

  constructor(private httpClient : HttpClient, private messageService : MessageService) { }

  getKeywords() : Observable<Keyword[]> {
    this.log("Fetched keywords: " + KEYWORDS.length);
    return of(KEYWORDS);
  }

  log(message : string) : void {
    this.messageService.add(message);
  }

  getKeyword(name : string): Observable<Keyword> {
    return of(KEYWORDS.find(keyword => keyword.name === name));
  }
}
