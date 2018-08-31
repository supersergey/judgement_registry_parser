import { Component, OnInit } from '@angular/core';
import { DashboardService } from "../dashboard.service";
import { DashboardEntry } from "../dashboard-entry";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  dashboardEntries: DashboardEntry[] = [];

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.getDashboardEntries();
  }

  getDashboardEntries(): void {
    this.dashboardService.getDashboardEntries()
      .subscribe(entries => this.dashboardEntries = DashboardComponent.processEntries(entries));
  }

  private static processEntries(entries: DashboardEntry[]) : DashboardEntry[] {
    for (let entry of entries) {
      for (let doc of entry.documents) {
        doc.fullUrl = environment.registryUrl + doc.id;
      }
    }
    return entries;
  }
}
