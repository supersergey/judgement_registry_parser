import {Document} from "./document"

export class DashboardEntry {
  public keyword : string;
  public documents : Document[];
  public constructor(keyword : string, documents: Document[]) {
    this.keyword = keyword;
    this.documents = documents;
  }

  public getDocuments() : Document[] {
    return this.documents;
  }
}
