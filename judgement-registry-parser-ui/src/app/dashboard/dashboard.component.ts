import { Component, OnInit } from '@angular/core';
import { DashboardService } from "./dashboard.service";
import { DashboardEntry } from "./dashboard-entry";
import { environment } from "../../environments/environment";
import { Document } from "../document";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  collectionSize: number;
  page: number = 0;
  dashboardEntries: DashboardEntry[];

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.getDashboardEntries();
  }

  getDashboardEntries(): void {
    this.dashboardService.getDashboardEntries()
      .subscribe(page => {
        this.collectionSize = page.collectionSize;
        this.page = page.page;
        this.dashboardEntries = DashboardComponent.processEntries(page.payload);
      });
  }

  private static processEntries(entries : Map<string, Document[]>) : DashboardEntry[] {
    let result : DashboardEntry[] = [];
    let keys : string [] = Object.keys(entries);
    for (let key of keys) {
      let docs = entries[key];
      let dashboardEntry : DashboardEntry;
      docs.forEach(d => {
        d.fullUrl = environment.registryUrl + d.registryId;
      });
      result.push(new DashboardEntry(key, docs));
    }
    return result || [];
  }
}
