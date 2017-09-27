import { Component, OnDestroy, OnInit } from '@angular/core';
import { AmazonLoginService } from '../../amazon-login.service';
import { Subscription } from 'rxjs/Subscription';

const SIGN_IN = 'Sign In';

@Component({
  selector: 'app-menu-header',
  templateUrl: './menu-header.component.html',
  styleUrls: ['./menu-header.component.scss']
})
export class MenuHeaderComponent implements OnInit, OnDestroy {
  isCollapsed = true;
  user = SIGN_IN;
  loggedIn = false;

  private userSubscription: Subscription;

  constructor(private loginService: AmazonLoginService) {
  }

  ngOnInit() {
    this.userSubscription = this.loginService.user$.subscribe((user) => {
      console.log('user update received : ' + JSON.stringify(user));
      if (user.isLoggedIn()) {
        this.user = user.name;
        this.loggedIn = true;
      } else {
        this.user = SIGN_IN;
        this.loggedIn = false;
      }
    });
  }

  login() {
    this.loginService.login();
  }

  logout() {
    this.loginService.logout();
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

}
