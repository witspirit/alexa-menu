import { Injectable } from '@angular/core';

@Injectable()
export class AmazonLoginService {
  loggedIn = false;

  constructor() {
  }

  login() {
    console.log('login()');
    this.loggedIn = true;
    const options = {scope: 'profile'};
    amazon.Login.authorize(options, this.handleAmazonAuthorize);
  }

  handleAmazonAuthorize(response) {
    console.log('handleAmazonAuthorize');

    if (response.error) {
      this.reportError('authorize', response);
      return;
    }
    console.log('Access Token: ' + response.access_token);

    // userInfo.accessToken = response.access_token;
    // userInfo.authenticated = true;

    // validate();

   //  this.loggedIn = true;
  }

  reportError(context, errorResponse) {
    console.log(context + ' ' + errorResponse.error + ' - ' + errorResponse.error_description);
    alert(context + ' ' + errorResponse.error + ' - ' + errorResponse.error_description);
  }

  logout() {
    console.log('logout()');
    amazon.Login.logout();
    this.loggedIn = false;
  }

}
