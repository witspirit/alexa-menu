import { TestBed, inject } from '@angular/core/testing';

import { AmazonLoginService } from './amazon-login.service';

describe('AmazonLoginService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AmazonLoginService]
    });
  });

  it('should be created', inject([AmazonLoginService], (service: AmazonLoginService) => {
    expect(service).toBeTruthy();
  }));
});
