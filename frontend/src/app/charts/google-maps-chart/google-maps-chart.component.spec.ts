import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GoogleMapsChartComponent } from './google-maps-chart.component';

describe('GoogleMapsChartComponent', () => {
  let component: GoogleMapsChartComponent;
  let fixture: ComponentFixture<GoogleMapsChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GoogleMapsChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GoogleMapsChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
