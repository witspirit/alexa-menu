import { Injectable } from '@angular/core';
import { User } from './model/user';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

declare let amazon: any; // No proper type bindings for amazon login SDK, so just declaring it as 'present'

const NO_USER = new User(null);

@Injectable()
export class AmazonLoginService {
  private user = NO_USER;

  private userUpdates = new Subject<User>();

  public user$: Observable<User> = this.userUpdates.asObservable();

  constructor() {
  }

  login() {
    console.log('login()');

    const options = {scope: 'profile'};
    amazon.Login.authorize(options, (response) => { // Arrow function is required to preserve 'this' !
      this.handleAmazonAuthorize(response);
    });
  }

  logout() {
    console.log('logout()');
    amazon.Login.logout();

    this.user = NO_USER;

    this.notifyUi();
  }

  private handleAmazonAuthorize(response) {
    console.log('handleAmazonAuthorize');

    if (response.error) {
      this.reportError('authorize', response);
      return;
    }
    console.log('Access Token: ' + response.access_token);

    this.user = new User(response.access_token);

    this.retrieveProfile();
  }

  private retrieveProfile() {
    console.log('retrieveProfile()');
    amazon.Login.retrieveProfile(this.user.accessToken, (response) => { // Arrow function is required to preserve 'this' !
      console.log('Profile Response:' + JSON.stringify(response));

      if (response.error) {
        this.reportError('profile', response);
        return;
      }

      this.user.setProfile(response.profile.Name, response.profile.PrimaryEmail);

      this.notifyUi();
    });
  }

  private notifyUi() {
    console.log('notifyUi()');
    // Should notify UI that state has changed
    this.userUpdates.next(this.user);
  }

  private reportError(context, errorResponse) {
    // Should probably emit an error event of some kind
    console.log(context + ' ' + errorResponse.error + ' - ' + errorResponse.error_description);
    alert(context + ' ' + errorResponse.error + ' - ' + errorResponse.error_description);
  }


}
