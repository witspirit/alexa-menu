import { Injectable } from '@angular/core';
import { Menu } from './model/menu';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class MenuService {
  private menuUpdates = new Subject<Menu[]>();

  public menus$ = this.menuUpdates.asObservable();

  constructor() { }

  getMenusFor(date: string) {
    this.menuUpdates.next([
      new Menu('20170918', 'Groen patatjes, worst, eitje [from Observable]'),
      new Menu('20170919', 'Diepvriespizza [from Observable]')
    ])
  }

}
