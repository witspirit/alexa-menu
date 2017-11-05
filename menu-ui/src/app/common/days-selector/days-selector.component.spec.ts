import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DaysSelectorComponent } from './days-selector.component';

describe('DaysSelectorComponent', () => {
  let component: DaysSelectorComponent;
  let fixture: ComponentFixture<DaysSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DaysSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DaysSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
