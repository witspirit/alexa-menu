import { Component, OnInit } from '@angular/core';
import { MenuService } from '../menu.service';

@Component({
  selector: 'app-menu-editor-page',
  templateUrl: './menu-editor-page.component.html',
  styleUrls: ['./menu-editor-page.component.scss']
})
export class MenuEditorPageComponent implements OnInit {

  constructor(public menuService: MenuService) { }

  ngOnInit() {
  }

}
