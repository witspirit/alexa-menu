import { Injectable } from '@angular/core';
import { Menu } from './model/menu';
import { Subject } from 'rxjs/Subject';
import * as moment from 'moment';
import { AmazonLoginService } from './amazon-login.service';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { User } from './model/user';


// Small helper that pins down the format we are using on the server
function format(date: moment.Moment) {
  return date.format('YYYYMMDD');
}

@Injectable()
export class MenuService {
  private startDate: moment.Moment = moment();
  private accessToken: string;
  private menuUpdates = new Subject<Menu[]>();

  public menus$ = this.menuUpdates.asObservable();

  constructor(private http: HttpClient, private login: AmazonLoginService) {
    login.user$.subscribe((user) => this.onUserUpdate(user));
  }

  private onUserUpdate(user: User): void {
    this.accessToken = user.accessToken;
    if (this.accessToken === null) {
      this.menuUpdates.next([]);
    } else {
      this.getMenusFor(this.startDate);
    }
  }

  getMenusFor(date: moment.Moment) {
    this.http.get<Menu[]>('https://api.menu.witspirit.be/menus', {
      headers: new HttpHeaders().set('Authorization', this.accessToken),
      params: new HttpParams().set('since', format(date))
    }).subscribe((receivedMenus) => this.menuUpdates.next(receivedMenus), this.errorHandler);

    // NOTE: We create fresh moment instances based on the base date, since otherwise the single date instance is modified !
    // this.menuUpdates.next([
    //   new Menu(format(date), 'Groen patatjes, worst, eitje'),
    //   new Menu(format(moment(date).add(1, 'days')), 'Diepvriespizza'),
    //   new Menu(format(moment(date).add(2, 'days')), 'Something else')
    // ]);
  }

  private errorHandler(err: HttpErrorResponse): void {
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
