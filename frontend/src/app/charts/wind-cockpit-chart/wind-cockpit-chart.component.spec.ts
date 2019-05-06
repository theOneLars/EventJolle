import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WindCockpitChartComponent } from './wind-cockpit-chart.component';

describe('WindCockpitChartComponent', () => {
  let component: WindCockpitChartComponent;
  let fixture: ComponentFixture<WindCockpitChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WindCockpitChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WindCockpitChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
