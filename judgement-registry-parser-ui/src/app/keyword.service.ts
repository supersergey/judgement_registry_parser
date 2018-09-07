import {Injectable} from '@angular/core';
import {Keyword} from './keyword';
import {Document} from "./document";
import {KEYWORDS} from './mock-keywords';
import {Observable, throwError} from 'rxjs';
import {MessageService} from './message.service';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import {environment} from '../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, OPTIONS',
    'Access-Control-Allow-Origin': '*'
  })
};


@Injectable({
  providedIn: 'root'
})
export class KeywordService {
  requestUrl = environment.host + ':' + environment.port + '/' + environment.baseUrl;
  keyword : Keyword;
  constructor(private http: HttpClient,
              private messageService : MessageService) { }


  getKeywords() : Observable<Keyword[]> {
    this.log("Fetched keywords: " + KEYWORDS.length);
    const endpoint = "/keyword";
    return this.http.get<Keyword[]>(this.requestUrl + endpoint);
  }

  log(message : string) : void {
    this.messageService.add(message);
  }

  getDocumentsByKeyword(name : string): Observable<Document[]> {
    const endpoint = "/keyword/" + name;
    return this.http.get<Document[]>(this.requestUrl + endpoint);
  }

  addKeyword(newKeyword: Keyword) : Observable<Keyword> {
    const endpoint = '/keyword/';
    console.log(this.requestUrl + endpoint);
    console.log(newKeyword);
    return this.http.post<Keyword>(this.requestUrl + endpoint, newKeyword, httpOptions);
  }

  deleteKeyword(keyword: Keyword) : Observable<Keyword> {
    const endpoint = `${this.requestUrl}/keyword/${keyword.keyword}`;
    return this.http.put<Keyword>(endpoint, httpOptions);
  }
}
