import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AmazonLoginComponent } from './amazon-login.component';

describe('AmazonLoginComponent', () => {
  let component: AmazonLoginComponent;
  let fixture: ComponentFixture<AmazonLoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AmazonLoginComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmazonLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
