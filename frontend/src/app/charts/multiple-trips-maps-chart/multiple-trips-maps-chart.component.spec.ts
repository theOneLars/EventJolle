import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleTripsMapsChartComponent } from './multiple-trips-maps-chart.component';

describe('MultipleTripsMapsChartComponent', () => {
  let component: MultipleTripsMapsChartComponent;
  let fixture: ComponentFixture<MultipleTripsMapsChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultipleTripsMapsChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleTripsMapsChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
