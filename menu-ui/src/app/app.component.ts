import { Component } from '@angular/core';
import { MenuService } from './menu.service';
import { AmazonLoginService, NO_USER } from './amazon-login.service';
import { User } from './model/user';
import * as moment from 'moment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  user: User = NO_USER;
  startDate = moment();

  constructor(private amazonLogin: AmazonLoginService, private menuService: MenuService) {
    amazonLogin.user$.subscribe(newUser => this.onUserUpdate(newUser));
    menuService.setStartDate(this.startDate);
  }

  private onUserUpdate(updatedUser: User): void {
    this.user = updatedUser;
    this.menuService.setAcessToken(updatedUser.accessToken);
  }

  public login(): void {
    this.amazonLogin.login();
  }

  public logout(): void {
    this.amazonLogin.logout();
  }
}
