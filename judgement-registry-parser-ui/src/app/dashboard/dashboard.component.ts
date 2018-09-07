import { Component, OnInit } from '@angular/core';
import { DashboardService } from "../dashboard.service";
import { Document } from "../document";
import { environment } from "../../environments/environment";
import {KeyValue} from "@angular/common";
import {DashboardEntry} from "../dashboard-entry";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  entries: DashboardEntry[];

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.getDashboardEntries();
  }

  getDashboardEntries(): void {
    this.dashboardService.getDashboardEntries()
      .subscribe(entries => this.entries = DashboardComponent.processEntries(entries));
  }

  private static processEntries(entries : Map<string, Document[]>) : DashboardEntry[] {
    let result : DashboardEntry[] = [];
    let keys : string [] = Object.keys(entries);
    for (let key of keys) {
      let docs = entries[key];
      let dashboardEntry : DashboardEntry;
      docs.forEach(d => {
        d.fullUrl = environment.registryUrl + d.id;
      });
      result.push(new DashboardEntry(key, docs));
    }
    return result || [];
  }
}
