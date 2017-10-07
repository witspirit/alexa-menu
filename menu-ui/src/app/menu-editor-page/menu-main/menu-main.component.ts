import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Menu } from '../../model/menu';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-menu-main',
  templateUrl: './menu-main.component.html',
  styleUrls: ['./menu-main.component.scss']
})
export class MenuMainComponent implements OnInit, OnChanges {
  @Input() menus: Array<Menu> = [];
  @Output() onMenuUpdate = new EventEmitter<Menu>();

  menuEditors: Array<MenuEditor> = [];

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.menuEditors = this.menus.map((menu) => new MenuEditor(menu, new FormControl()));
  }

  public applyMenuUpdate(menuEditor: MenuEditor): void {
    const newMenu = new Menu(menuEditor.menu.date, menuEditor.dinnerInput.value);
    console.log('Received confirmation for ' + newMenu);
    this.onMenuUpdate.emit(newMenu);
  }

}

export class MenuEditor {
  constructor(public menu: Menu, public dinnerInput: FormControl) {
    this.dinnerInput.setValue(menu.dinner);
  }
}
