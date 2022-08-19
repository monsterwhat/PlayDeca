import { TestBed } from '@angular/core/testing';

import { VoteService } from './vote.service';

describe('VoteService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: VoteService = TestBed.inject(VoteService);
    expect(service).toBeTruthy();
  });
});
