import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryMapViewComponent } from './history-map-view.component';

describe('HistoryMapViewComponent', () => {
  let component: HistoryMapViewComponent;
  let fixture: ComponentFixture<HistoryMapViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoryMapViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryMapViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
