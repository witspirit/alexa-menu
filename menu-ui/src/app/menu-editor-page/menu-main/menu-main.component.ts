import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Menu } from '../../model/menu';

@Component({
  selector: 'app-menu-main',
  templateUrl: './menu-main.component.html',
  styleUrls: ['./menu-main.component.scss']
})
export class MenuMainComponent implements OnInit {
  @Input() menus: Array<Menu> = [];
  @Output() onMenuUpdate = new EventEmitter<Menu>();

  constructor() { }

  ngOnInit() {
  }

  public updateMenu(menu: Menu) {
    this.onMenuUpdate.emit(menu);
  }

}
