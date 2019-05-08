import { TestBed } from '@angular/core/testing';

import { WindServiceService } from './wind-cockpit.service';

describe('WindServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WindServiceService = TestBed.get(WindServiceService);
    expect(service).toBeTruthy();
  });
});
