import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {environment} from "../../environments/environment";
import {Page} from "../page";
import {Document} from "../document";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private requestUrl = environment.host + ':' + environment.port + '/' + environment.baseUrl;

  constructor(private http: HttpClient) {
  }

  public getDashboardEntries(): Observable<Page<Map<string, Document[]>>> {
    const endpoint = '/dashboard';
    return this.http.get<Page<Map<string, Document[]>>>(this.requestUrl + endpoint);
  }
}
