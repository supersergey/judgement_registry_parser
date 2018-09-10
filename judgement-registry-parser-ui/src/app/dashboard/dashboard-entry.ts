import {Document} from "../document";

export class DashboardEntry {
  public constructor(private keyword : string, private documents: Document[]) {
  }

  public getKeyword() : string {
    return this.keyword;
  }

  public getDocuments() : Document[] {
    return this.documents;
  }
}
