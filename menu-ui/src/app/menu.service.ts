import { Injectable } from '@angular/core';
import { Menu } from './model/menu';
import { Subject } from 'rxjs/Subject';
import * as moment from 'moment';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';


// Small helper that pins down the format we are using on the server
function format(date: moment.Moment) {
  return date.format('YYYYMMDD');
}

const nrOfDays = 7;

@Injectable()
export class MenuService {
  private startDate: moment.Moment;
  private accessToken: string;

  private menuUpdates = new Subject<Menu[]>();

  public menus$ = this.menuUpdates.asObservable();

  constructor(private http: HttpClient) {
  }

  public setStartDate(date: moment.Moment) {
    this.startDate = date;
    this.refreshMenus();
  }

  public setAcessToken(accessToken: string): void {
    this.accessToken = accessToken;
    this.refreshMenus();
  }

  private refreshMenus(): void {
    if (this.accessToken == null || this.startDate == null) {
      this.menuUpdates.next([]);
    } else {
      this.getMenusFor(this.startDate);
    }
  }

  getMenusFor(date: moment.Moment) {
    this.http.get<Menu[]>('https://api.menu.witspirit.be/menus', {
      headers: new HttpHeaders().set('Authorization', this.accessToken),
      params: new HttpParams().set('since', format(date)).set('nrOfDays', nrOfDays.toString())
    }).subscribe(receivedMenus => this.onMenusReceived(date, receivedMenus), err => this.onApiError(err));
  }

  setDinner(menu: Menu): void {
    this.http.put('https://api.menu.witspirit.be/menus/' + menu.date, menu.dinner, {
      headers: new HttpHeaders().set('Authorization', this.accessToken)
    }).subscribe(() => this.refreshMenus(), err => this.onApiError(err));
  }


  private onMenusReceived(startDate: moment.Moment, receivedMenus: Menu[]): void {
    // Convert the received data structure to a lookup map, indexed by date
    const menuLookup = {};
    receivedMenus.forEach((menu) => {
      menuLookup[menu.date]  = menu.dinner;
    });

    // Doesn't feel like idiomatic JavaScript/TypeScript but seems to get the job done in a fairly ok way
    const processMenus = [];
    for (let i = 0; i < nrOfDays; i++) {
      processMenus.push(this.menuEntry(startDate, i, menuLookup));
    }

    this.menuUpdates.next(processMenus);
  }

  private menuEntry(baseDate: moment.Moment, dayOffset: number, menuLookup: Object): Menu {
    // NOTE: We create fresh moment instances based on the base date, since otherwise the single date instance is modified !
    const dateKey = format(moment(baseDate).add(dayOffset, 'days'));
    return new Menu(dateKey, menuLookup[dateKey]);
  }

  private onApiError(err: HttpErrorResponse): void {
    if (err.error instanceof Error) {
      // A client-side or network error occurred. Handle it accordingly.
      console.log('An error occurred:', err.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.log(`Backend returned code ${err.status}, body was: ${err.error}`);
    }
  }

}
