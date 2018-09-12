import { Component, OnInit } from '@angular/core';
import { Keyword } from './keyword'
import { KeywordService } from './keyword.service';
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { MessageService } from "../messages/message.service";

@Component({
  selector: 'app-keywords',
  templateUrl: './keywords.component.html',
  styleUrls: ['./keywords.component.css'],
})

export class KeywordsComponent implements OnInit {

  keywordToDelete: string;

  constructor(private keywordService : KeywordService,
              private modalService: NgbModal,
              private messageService : MessageService) {}


  ngOnInit() {
    this.getKeywords();
  }

  keywords : Keyword[];

  getKeywords() : void {
    this.keywordService.getKeywords().subscribe(kwords => {
      this.keywords = kwords;
      console.log(kwords)
    });
  }

  addKeyword(keyword : string) {
    keyword = keyword.trim();
    if (!keyword) {
      return;
    }
    const newKeyword : Keyword = { keyword } as Keyword;
    this.keywordService.addKeyword(newKeyword).subscribe(kword => this.keywords.push(kword));
  }

  deleteKeyword(keywordToDelete: Keyword, content) {
    this.keywordToDelete = keywordToDelete.keyword;
    this.confirmDelete(keywordToDelete, content);
  }

  confirmDelete(keywordToDelete, content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.keywordService.deleteKeyword(keywordToDelete).subscribe();
      this.keywords = this.keywords.filter(item => item !== keywordToDelete);
    }, (reason) => {});
  }

  updateKeyword(keyword: Keyword) {
    if (Date.now().valueOf() - new Date(keyword.updatedTs).valueOf() < 60*60*1000) {
      this.messageService.add("Обновление возможно не чаще 1 раза в час.")
    } else {
      // this.keywordService.updateKeyword(keyword);
    }
  }
}
