import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {Observable} from "rxjs/index";
import {DashboardEntry} from "./dashboard-entry";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private requestUrl = environment.host + ':' + environment.port + '/' + environment.baseUrl;
  constructor(private http: HttpClient,
              private messageService : MessageService) { }

  public getDashboardEntries(): Observable<DashboardEntry[]> {
    const endpoint = '/dashboard';
    return this.http.get<DashboardEntry[]>(this.requestUrl + endpoint);
  }
}
