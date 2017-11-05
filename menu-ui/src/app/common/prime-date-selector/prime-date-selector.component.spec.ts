import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrimeDateSelectorComponent } from './prime-date-selector.component';

describe('PrimeDateSelectorComponent', () => {
  let component: PrimeDateSelectorComponent;
  let fixture: ComponentFixture<PrimeDateSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrimeDateSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrimeDateSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
