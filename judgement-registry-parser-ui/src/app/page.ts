export class Page<T> {
  public constructor(public collectionSize: number, public page: number, public payload : T) {
  }

  public getCollectionSize() : number {
    return this.collectionSize;
  };

  public getPage() : number {
    return this.page;
  }

  public getPayload(): T {
    return this.payload;
  }
}
