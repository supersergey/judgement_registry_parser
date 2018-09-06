import { Injectable } from '@angular/core';
import { Keyword } from './keyword';
import { Document } from "./document";
import { KEYWORDS } from './mock-keywords';
import { Observable, of } from 'rxjs';
import { MessageService} from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment'
@Injectable({
  providedIn: 'root'
})
export class KeywordService {
  private requestUrl = environment.host + ':' + environment.port + '/' + environment.baseUrl;

  constructor(private http: HttpClient,
              private messageService : MessageService) { }


  getKeywords() : Observable<Keyword[]> {
    this.log("Fetched keywords: " + KEYWORDS.length);
    const endpoint = "/summary";
    return this.http.get<Keyword[]>(this.requestUrl + endpoint);
  }

  log(message : string) : void {
    this.messageService.add(message);
  }

  getDocumentsByKeyword(name : string): Observable<Document[]> {
    const endpoint = "/keyword/" + name;
    return this.http.get<Document[]>(this.requestUrl + endpoint);
  }
}
