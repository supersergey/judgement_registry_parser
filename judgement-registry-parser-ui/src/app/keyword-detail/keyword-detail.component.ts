import {Component, OnInit, Input} from '@angular/core';
import {Keyword} from '../keyword';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {KeywordService} from "../keyword.service";

@Component({
  selector: 'app-keyword-detail',
  templateUrl: './keyword-detail.component.html',
  styleUrls: ['./keyword-detail.component.css']
})

export class KeywordDetailComponent implements OnInit {
  keyword: Keyword;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private keywordService: KeywordService) {
  }

  getKeyword() : void {
    const name = this.route.snapshot.paramMap.get('name');
    this.keywordService.getKeyword(name).subscribe(keyword => this.keyword = keyword);
  }

  goBack() : void {
    this.location.back();
  }

  ngOnInit() {
    this.getKeyword();
  }

}
