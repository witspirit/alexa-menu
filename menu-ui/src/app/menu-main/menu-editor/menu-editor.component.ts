import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Menu } from '../../model/menu';
import { FormControl } from '@angular/forms';

const RETURN_KEY = 13;

@Component({
  selector: 'app-menu-editor',
  templateUrl: './menu-editor.component.html',
  styleUrls: ['./menu-editor.component.css']
})
export class MenuEditorComponent implements OnInit, OnChanges {
  @Input() menu: Menu;
  @Output() onUpdate = new EventEmitter<Menu>();

  dinnerInput = new FormControl();

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.dinnerInput.setValue(this.menu.dinner);
  }

  public handleUpdate(event: KeyboardEvent): void {
    if (event.keyCode === RETURN_KEY) {
      const newMenu = new Menu(this.menu.date, this.dinnerInput.value);
      console.log('Received confirmation for ' + newMenu);
      this.onUpdate.emit(newMenu);
    }
  }

}
