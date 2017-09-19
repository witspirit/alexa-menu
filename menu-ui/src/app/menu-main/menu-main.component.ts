import { Component, OnInit } from '@angular/core';
import { Menu } from '../model/menu';
import { MenuService } from '../menu.service';

@Component({
  selector: 'app-menu-main',
  templateUrl: './menu-main.component.html',
  styleUrls: ['./menu-main.component.scss']
})
export class MenuMainComponent implements OnInit {
  menus: Array<Menu> = [];

  constructor(private menuService: MenuService) { }

  ngOnInit() {
    this.menuService.menus$.subscribe((menus) => this.menus = menus );
  }

}
