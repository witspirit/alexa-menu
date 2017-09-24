import { Component, Input, OnInit } from '@angular/core';
import { Menu } from '../../model/menu';

@Component({
  selector: 'app-menu-editor',
  templateUrl: './menu-editor.component.html',
  styleUrls: ['./menu-editor.component.css']
})
export class MenuEditorComponent implements OnInit {
  @Input() menu: Menu;

  constructor() { }

  ngOnInit() {
  }

}
