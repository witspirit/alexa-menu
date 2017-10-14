import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Menu } from '../../model/menu';
import { FormControl } from '@angular/forms';
import * as moment from 'moment';



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
    console.log('MenuMainComponent - Input changes');
    this.menuEditors = this.menus.map((menu) => new MenuEditor(menu, new FormControl()));
  }

  public applyMenuUpdate(menuEditor: MenuEditor): void {
    const newDinner = menuEditor.dinnerInput.value;
    console.log('applyMenuUpdate: originalDinner = ' + menuEditor.originalDinner + ', newDinner = ' + newDinner);
    if (menuEditor.originalDinner === newDinner) {
      return; // Nothing changed... So don't do anything
    }
    const newMenu = new Menu(menuEditor.menu.date, newDinner);
    menuEditor.originalDinner = newDinner; // Temporary workaround until I figure out why I don't get a menu refresh as expected
    console.log('Received confirmation for ' + newMenu);
    this.onMenuUpdate.emit(newMenu);
  }

}

export class MenuEditor {
  public displayDay: string;
  public displayDate: string;

  public dayIndex: number;
  public today: boolean;
  public originalDinner: string;

  constructor(public menu: Menu, public dinnerInput: FormControl) {
    const date = moment(menu.date, 'YYYYMMDD');

    this.displayDay = date.format('dddd');
    this.displayDate = date.format('MMM DD');
    this.dinnerInput.setValue(menu.dinner);

    this.dayIndex = Number(date.format('E'));
    this.today = moment().isSame(date, 'date');
    this.originalDinner = menu.dinner;
  }
}
