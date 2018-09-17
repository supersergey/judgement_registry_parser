import {Injectable} from '@angular/core';
import {Keyword} from './keyword';
import {Document} from "../document";
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {MessageService} from "../messages/message.service";
import {Page} from "../page";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
    'Access-Control-Allow-Origin': '*'
  })
};


@Injectable({
  providedIn: 'root'
})

export class KeywordService {
  requestUrl = `${environment.host}:${environment.port}/${environment.baseUrl}`;
  keyword : Keyword;
  constructor(private http: HttpClient, private messageService : MessageService ) { }


  getKeywords(page: number, size: number) : Observable<Page<Keyword[]>> {
    const endpoint = `/keyword?page=${page}&size=${size}`;
    return this.http.get<Page<Keyword[]>>(this.requestUrl + endpoint);
  }

  getDocumentsByKeyword(name : string): Observable<Document[]> {
    const endpoint = "/keyword/details/" + name;
    return this.http.get<Document[]>(this.requestUrl + endpoint);
  }

  addKeyword(newKeyword: Keyword) : Observable<Keyword> {
    const endpoint = `${this.requestUrl}/keyword/`;
    return this.http.post<Keyword>(endpoint, newKeyword, httpOptions);
  }

  deleteKeyword(keyword: Keyword) : Observable<Keyword> {
    const endpoint = `${this.requestUrl}/keyword/${keyword.keyword}`;
    return this.http.delete<Keyword>(endpoint, httpOptions);
  }

  updateKeyword(keyword: Keyword) {
    const endpoint = `${this.requestUrl}/keyword/`;
    return this.http.put<Keyword>(endpoint, keyword, httpOptions);
  }

  findKeyword(value: string) : Observable<Keyword[]>{
    const endpoint = `${this.requestUrl}/keyword/${value}`;
    return this.http.get<Keyword[]>(endpoint);
  }
}
