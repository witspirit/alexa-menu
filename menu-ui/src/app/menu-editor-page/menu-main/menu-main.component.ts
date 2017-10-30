import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Menu } from '../../model/menu';
import { FormControl } from '@angular/forms';
import * as moment from 'moment';
import { MenuService } from '../../menu.service';


@Component({
  selector: 'app-menu-main',
  templateUrl: './menu-main.component.html',
  styleUrls: ['./menu-main.component.scss']
})
export class MenuMainComponent implements OnInit, OnChanges {
  @Input() menus: Array<Menu> = [];
  @Output() onMenuUpdate = new EventEmitter<Menu>();

  menuEditors: Array<MenuEditor> = [];

  constructor(public menuService: MenuService) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('MenuMainComponent - Input changes');
    this.menuEditors = this.menus.map((menu) => new MenuEditor(this, menu, new FormControl()));
  }

}

export class MenuEditor {
  public displayDay: string;
  public displayDate: string;

  public dayIndex: number;
  public today: boolean;
  public originalDinner: string;
  public suggestions: string[] = [];

  constructor(private main: MenuMainComponent, public menu: Menu, public dinnerInput: FormControl) {
    const date = moment(menu.date, 'YYYYMMDD');

    this.displayDay = date.format('dddd');
    this.displayDate = date.format('MMM DD');
    this.dinnerInput.setValue(menu.dinner);

    this.dayIndex = Number(date.format('E'));
    this.today = moment().isSame(date, 'date');
    this.originalDinner = menu.dinner;
  }

  public fetchSuggestions(): void {
    console.log('suggestFor ' + this.displayDate);
    this.main.menuService.getSuggestions(3, suggestions => {
      console.log('Received suggestions: ' + suggestions);
      this.suggestions = suggestions;
    });
  }

  public applySuggestion(suggestion: string): void {
    console.log('applySuggestion');
    this.dinnerInput.setValue(suggestion);
    this.applyMenuUpdate();

  }

  public applyMenuUpdate(): void {
    const newDinner = this.dinnerInput.value;
    console.log('applyMenuUpdate: originalDinner = ' + this.originalDinner + ', newDinner = ' + newDinner);
    if (this.originalDinner === newDinner) {
      return; // Nothing changed... So don't do anything
    }
    const newMenu = new Menu(this.menu.date, newDinner);
    this.originalDinner = newDinner; // Temporary workaround until I figure out why I don't get a menu refresh as expected
    console.log('Received confirmation for ' + newMenu);
    this.main.onMenuUpdate.emit(newMenu);
  }
}
