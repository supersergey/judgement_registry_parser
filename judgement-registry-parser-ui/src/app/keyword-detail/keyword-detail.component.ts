import {Component, OnInit, Input} from '@angular/core';
import { Document } from '../document';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {KeywordService} from "../keywords/keyword.service";
import {Keyword} from "../keywords/keyword";

@Component({
  selector: 'app-keyword-detail',
  templateUrl: './keyword-detail.component.html',
  styleUrls: ['./keyword-detail.component.css']
})

export class KeywordDetailComponent implements OnInit {
  keywordName: string;
  documents: Document[];

  constructor(private route: ActivatedRoute,
              private location: Location,
              private keywordService: KeywordService) {
  }

  getKeyword() : void {
    this.keywordName = this.route.snapshot.paramMap.get('keyword');
    this.keywordService.getDocumentsByKeyword(this.keywordName).subscribe(documents => this.documents = documents);
  }

  goBack() : void {
    this.location.back();
  }

  ngOnInit() {
    this.getKeyword();
  }
}
