import { Injectable } from '@angular/core';
import { Menu } from './model/menu';
import { Subject } from 'rxjs/Subject';
import * as moment from 'moment';

// Small helper that pins down the format we are using on the server
function format(date: moment.Moment) {
  return date.format('YYYYMMDD');
}

@Injectable()
export class MenuService {
  private menuUpdates = new Subject<Menu[]>();

  public menus$ = this.menuUpdates.asObservable();

  constructor() { }

  getMenusFor(date: moment.Moment) {
    // NOTE: We create fresh moment instances based on the base date, since otherwise the single date instance is modified !
    this.menuUpdates.next([
      new Menu(format(date), 'Groen patatjes, worst, eitje'),
      new Menu(format(moment(date).add(1, 'days')), 'Diepvriespizza'),
      new Menu(format(moment(date).add(2, 'days')), 'Something else')
    ])
  }

}
