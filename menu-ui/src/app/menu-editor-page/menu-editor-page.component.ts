import { Component, OnInit } from '@angular/core';
import { MenuService } from '../menu.service';
import { Menu } from '../model/menu';

@Component({
  selector: 'app-menu-editor-page',
  templateUrl: './menu-editor-page.component.html',
  styleUrls: ['./menu-editor-page.component.scss']
})
export class MenuEditorPageComponent implements OnInit {
  menus: Array<Menu> = [];

  constructor(public menuService: MenuService) { }

  ngOnInit() {
    this.menuService.menus$.subscribe(menus => this.menus = menus);
  }

}
