import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {Observable} from "rxjs/index";
import {environment} from "../environments/environment";
import {Document} from "./document";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private requestUrl = environment.host + ':' + environment.port + '/' + environment.baseUrl;

  constructor(private http: HttpClient,
              private messageService: MessageService) {
  }

  public getDashboardEntries(): Observable<Map<string, Document[]>> {
    const endpoint = '/dashboard';
    return this.http.get<Map<string, Document[]>>(this.requestUrl + endpoint);
  }
}
