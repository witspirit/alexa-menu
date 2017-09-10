import { Injectable } from '@angular/core';

@Injectable()
export class AmazonLoginService {
  loggedIn = false;

  constructor() { }

  login() {
    console.log('login()');
    this.loggedIn = true;
  }

  logout() {
    console.log('logout()');
    this.loggedIn = false;
  }

}
