import {Component, OnInit} from '@angular/core';
import {Keyword} from './keyword'
import {KeywordService} from './keyword.service';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MessageService, MessageType} from "../messages/message.service";
import {environment} from "../../environments/environment";
import {Observable, ObservableLike} from "rxjs/index";
import {Page} from "../page";

@Component({
  selector: 'app-keywords',
  templateUrl: './keywords.component.html',
  styleUrls: ['./keywords.component.css'],
})

export class KeywordsComponent implements OnInit {

  keywordToDelete: string;
  keywordToFind: string;

  collectionSize: number;
  pageSize: number = environment.defaultPageSize;
  page: number = 1;


  constructor(private keywordService: KeywordService,
              private modalService: NgbModal,
              private messageService: MessageService) {
  }

  keywords: Keyword[];

  ngOnInit() {
    this.findKeyword("");
  }

  getKeywords(page: number, size: number): void {
    this.keywordService.getKeywords(page - 1, size).subscribe(kwords => {
      this.keywords = kwords.payload;
      this.page = kwords.page + 1;
      this.collectionSize = kwords.collectionSize;
    });
  }

  addKeyword(keyword: string) {
    keyword = keyword.trim();
    if (!keyword) {
      return;
    }
    const newKeyword: Keyword = {keyword} as Keyword;
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
    }, (reason) => {
    });
  }

  updateKeyword(keyword: Keyword) {
    if (Date.now().valueOf() - new Date(keyword.updatedTs).valueOf() < 60 * 60 * 1000) {
      this.messageService.add("Обновление возможно не чаще 1 раза в час.", MessageType.ERROR)
    } else {
      keyword.updatedTs = new Date();
      this.keywordService.updateKeyword(keyword).subscribe(() =>
        this.messageService.add('Новые документы будут вскоре загружены', MessageType.NORMAL));
    }
  }

  findKeyword(value: string) {
    this.keywordToFind = value;
    this.doFindKeyword(value, 0, environment.defaultPageSize)
  }

  changePage(newPage: any): void {
    this.doFindKeyword(this.keywordToFind, newPage - 1, environment.defaultPageSize);
  }

  doFindKeyword(keyword: string, page: number, size: number) : void {
    this.keywordService.findKeyword(keyword, page, size).subscribe(response => {
          this.keywords = response.payload;
          this.collectionSize = response.collectionSize;
          this.page = response.page + 1;
        },
        error => this.messageService.add(error.error, MessageType.ERROR)
      )
  }
}
