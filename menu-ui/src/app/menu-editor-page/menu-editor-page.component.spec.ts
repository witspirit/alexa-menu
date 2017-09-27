import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuEditorPageComponent } from './menu-editor-page.component';

describe('MenuEditorPageComponent', () => {
  let component: MenuEditorPageComponent;
  let fixture: ComponentFixture<MenuEditorPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuEditorPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuEditorPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
