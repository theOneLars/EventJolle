import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WindCockpitComponent } from './wind-cockpit.component';

describe('WindCockpitComponent', () => {
  let component: WindCockpitComponent;
  let fixture: ComponentFixture<WindCockpitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WindCockpitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WindCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
