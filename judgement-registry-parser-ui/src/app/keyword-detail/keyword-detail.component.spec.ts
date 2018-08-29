import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeywordDetailComponent } from './keyword-detail.component';

describe('KeywordDetailComponent', () => {
  let component: KeywordDetailComponent;
  let fixture: ComponentFixture<KeywordDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeywordDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeywordDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
