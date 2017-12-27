import { Component, OnInit } from '@angular/core';
import { MenuService } from './menu.service';
import { User } from './auth/user';
import * as moment from 'moment';
import { Menu } from './model/menu';
import { AuthService, NO_USER } from './auth/auth.service';

// With the current initial setup of jump by 7 and nrOfDays 21, it is nice to put the start date
// starting at last weeks scheduling period. Such that the current period is in the middle of the screen.
const SCHEDULE_ISO_DAY = 6;
const WEEK_SIZE = 7;

function initialStartDate() {
  const today = moment();
  const dayOfWeekNr = Number(today.format('E'));
  const daysAfterStart = ((dayOfWeekNr - SCHEDULE_ISO_DAY) + WEEK_SIZE) % WEEK_SIZE;
  return today.subtract(daysAfterStart + WEEK_SIZE, 'days');
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  user: User = NO_USER;
  startDate = initialStartDate();
  menus: Array<Menu> = [];
  jumpLength = 7;
  nrOfDays = 21;

  constructor(private auth: AuthService, private menuService: MenuService) {
    menuService.setStartDate(this.startDate);
    menuService.setNrOfDays(this.nrOfDays);
  }

  ngOnInit(): void {
    this.auth.user$.subscribe(newUser => this.onUserUpdate(newUser));
    this.menuService.menus$.subscribe(menus => this.menus = menus);
  }

  private onUserUpdate(updatedUser: User): void {
    this.user = updatedUser;
    this.menuService.setAccessToken(updatedUser.accessToken);
  }

  public login(): void {
    this.auth.login();
  }

  public logout(): void {
    this.auth.logout();
  }

  public handleStartDateUpdate(newStartDate: moment.Moment) {
    // Hmm... This feels awkward... I seem to have two places to maintain the current start date... That cannot be right
    this.startDate = newStartDate;
    this.menuService.setStartDate(newStartDate);
  }

  public handleNrOfDaysUpdate(nrOfDays: number) {
    this.nrOfDays = nrOfDays;
    this.menuService.setNrOfDays(nrOfDays);
  }

  public handleMenuUpdate(newMenu: Menu) {
    this.menuService.setDinner(newMenu);
  }
}
