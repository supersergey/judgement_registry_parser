import { Component, OnInit } from '@angular/core';
import {Keyword} from "../keyword";
import {KeywordService} from "../keyword.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  keywords: Keyword[] = [];

  constructor(private keywordService: KeywordService) { }

  ngOnInit() {
    this.getKeywords();
  }

  getKeywords(): void {
    this.keywordService.getKeywords()
      .subscribe(keywords => this.keywords = keywords.slice(0, 5));
  }
}
