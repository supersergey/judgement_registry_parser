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
  pageSize : number = environment.defaultPageSize;
  page: number = 0;
  dashboardEntries: DashboardEntry[];

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.getDashboardEntries(1, environment.defaultPageSize);
  }

  getDashboardEntries(page : number, size : number): void {
    this.dashboardService.getDashboardEntries(page - 1, size)
      .subscribe(dashboardPage => {
        this.collectionSize = dashboardPage.collectionSize;
        this.page = dashboardPage.page + 1;
        this.dashboardEntries = DashboardComponent.processEntries(dashboardPage.payload);
      });
  }

  changePage(newPage : any) : void {
    this.getDashboardEntries(newPage, environment.defaultPageSize);
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
