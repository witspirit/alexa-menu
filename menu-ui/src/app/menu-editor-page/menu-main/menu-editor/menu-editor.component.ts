import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Menu } from '../../../model/menu';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-menu-editor',
  templateUrl: './menu-editor.component.html',
  styleUrls: ['./menu-editor.component.css']
})
export class MenuEditorComponent implements OnInit, OnChanges {
  @Input() menu: Menu;
  @Output() onUpdate = new EventEmitter<Menu>();

  dinnerInput = new FormControl();

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.dinnerInput.setValue(this.menu.dinner);
  }

  public applyMenuUpdate(): void {
    const newMenu = new Menu(this.menu.date, this.dinnerInput.value);
    console.log('Received confirmation for ' + newMenu);
    this.onUpdate.emit(newMenu);
  }

}
