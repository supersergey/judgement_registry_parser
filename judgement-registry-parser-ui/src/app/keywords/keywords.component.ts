import { Component, OnInit } from '@angular/core';
import { Keyword } from '../keyword'
import { KEYWORDS } from '../mock-keywords';
import { KeywordService} from '../keyword.service';

@Component({
  selector: 'app-keywords',
  templateUrl: './keywords.component.html',
  styleUrls: ['./keywords.component.css']
})

export class KeywordsComponent implements OnInit {

  constructor(private keywordService : KeywordService) { }

  ngOnInit() {
    this.getKeywords();
  }

  keywords : Keyword[];

  // selectedKeyword: Keyword;
  //
  // onSelect(keyword: Keyword) : void {
  //   this.selectedKeyword = keyword;
  // }

  getKeywords() : void {
    this.keywordService.getKeywords().subscribe(kwords => this.keywords = kwords);
  }
}
