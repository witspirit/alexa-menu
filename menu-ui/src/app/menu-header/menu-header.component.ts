import { Component, OnInit } from '@angular/core';
import { AmazonLoginService } from '../amazon-login.service';

const SIGN_IN = 'Sign In';

@Component({
  selector: 'app-menu-header',
  templateUrl: './menu-header.component.html',
  styleUrls: ['./menu-header.component.scss']
})
export class MenuHeaderComponent implements OnInit {

  isCollapsed = true;

  user = SIGN_IN;

  constructor(private loginService : AmazonLoginService) { }

  ngOnInit() {
  }

  login() {
    this.loginService.login();
    this.user = 'Dummy User';
  }

  logout() {
    this.loginService.logout();
    this.user = SIGN_IN;

  }

}
