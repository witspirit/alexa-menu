import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MenuService } from '../menu.service';
import { Menu } from '../model/menu';
import * as moment from 'moment';

@Component({
  selector: 'app-menu-editor-page',
  templateUrl: './menu-editor-page.component.html',
  styleUrls: ['./menu-editor-page.component.scss']
})
export class MenuEditorPageComponent implements OnInit {
  @Input() startDate: moment.Moment;
  @Input() jumpLength: number;
  @Input() menus: Array<Menu> = [];

  @Output() onMenuUpdate = new EventEmitter<Menu>();
  @Output() onStartDateUpdate = new EventEmitter<moment.Moment>();

  constructor() {
  }

  ngOnInit() {
  }

  handleStartDateUpdate(newStartDate: moment.Moment) {
    this.onStartDateUpdate.emit(newStartDate);
  }

  handleMenuUpdate(newMenu: Menu) {
    this.onMenuUpdate.emit(newMenu);
  }



}
