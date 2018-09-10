import { Component, OnInit } from '@angular/core';
import { Keyword } from './keyword'
import { KeywordService} from './keyword.service';

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

  getKeywords() : void {
    this.keywordService.getKeywords().subscribe(kwords => this.keywords = kwords);
  }

  addKeyword(keyword : string) {
    keyword = keyword.trim();
    if (!keyword) {
      return;
    }
    const newKeyword : Keyword = { keyword } as Keyword;
    this.keywordService.addKeyword(newKeyword).subscribe(kword => this.keywords.push(kword));
  }

  deleteKeyword(keywordToDelete: Keyword) {
    this.keywordService.deleteKeyword(keywordToDelete).subscribe();
    this.keywords = this.keywords.filter(item => item !== keywordToDelete);
  }
}
