import { Component, OnInit } from '@angular/core';
import { MenuService } from './menu.service';
import { AmazonLoginService, NO_USER } from './amazon-login.service';
import { User } from './model/user';
import * as moment from 'moment';
import { Menu } from './model/menu';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  user: User = NO_USER;
  startDate = moment();
  menus: Array<Menu> = [];
  jumpLength = 3;
  nrOfDays = 7;

  constructor(private amazonLogin: AmazonLoginService, private menuService: MenuService) {
    menuService.setStartDate(this.startDate);
    menuService.setNrOfDays(this.nrOfDays);
  }

  ngOnInit(): void {
    this.amazonLogin.user$.subscribe(newUser => this.onUserUpdate(newUser));
    this.menuService.menus$.subscribe(menus => this.menus = menus);
  }

  private onUserUpdate(updatedUser: User): void {
    this.user = updatedUser;
    this.menuService.setAccessToken(updatedUser.accessToken);
  }

  public login(): void {
    this.amazonLogin.login();
  }

  public logout(): void {
    this.amazonLogin.logout();
  }

  public handleStartDateUpdate(newStartDate: moment.Moment) {
    // Hmm... This feels awkward... I seem to have two places to maintain the current start date... That cannot be right
    this.startDate = newStartDate;
    this.menuService.setStartDate(newStartDate);
  }

  public handleMenuUpdate(newMenu: Menu) {
    this.menuService.setDinner(newMenu);
  }
}
