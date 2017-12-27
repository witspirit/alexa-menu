import { Injectable } from '@angular/core';
import { User } from '../user';
import { AuthError } from '../authError';
import { AuthCallback } from '../authCallback';

declare let amazon: any; // No proper type bindings for amazon login SDK, so just declaring it as 'present'

@Injectable()
export class AmazonLoginService {

  constructor() {
  }

  login(callback: AuthCallback) {
    console.log('Amazon login()');

    const options = {scope: 'profile'};
    amazon.Login.authorize(options, (response) => { // Arrow function is required to preserve 'this' !
      this.handleAmazonAuthorize(response, callback);
    });
  }

  logout() {
    console.log('Amazon logout()');
    amazon.Login.logout();
  }

  private handleAmazonAuthorize(response, callback: AuthCallback) {
    console.log('handleAmazonAuthorize');

    if (response.error) {
      callback.onError(new AuthError('authorize', response.error, response.error_description));
      return;
    }
    console.log('Access Token: ' + response.access_token);

    const user = new User(response.access_token);

    this.retrieveProfile(user, callback);
  }

  private retrieveProfile(user: User, callback: AuthCallback) {
    console.log('retrieveProfile(' + user + ')');
    amazon.Login.retrieveProfile(user.accessToken, (response) => { // Arrow function is required to preserve 'this' !
      console.log('Profile Response:' + JSON.stringify(response));

      if (response.error) {
        callback.onError(new AuthError('profile', response.error, response.error_description));
        return;
      }

      user.setProfile(response.profile.Name, response.profile.PrimaryEmail);

      callback.onSuccess(user);
    });
  }
}
