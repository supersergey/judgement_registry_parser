export class Page<T>{
  constructor(public page : number, public collectionSize : number, public payload : T) {}

  getPage() : number {
    return this.page;
  }

  getCollectionSize(): number {
    return this.collectionSize;
  }

  getPayload() : T {
    return this.payload;
  }
}
