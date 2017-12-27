import { Injectable } from '@angular/core';
import { User } from './user';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import { AmazonLoginService } from './amazon/amazon-login.service';
import { AuthError } from './authError';

export const NO_USER = new User(null);

@Injectable()
export class AuthService {
  private userUpdates = new Subject<User>();

  public user$: Observable<User> = this.userUpdates.asObservable();

  constructor(private amazon: AmazonLoginService) { }

  public login() {
    console.log('login()');
    // Perform actual login
    this.amazon.login((user) => this.handleSuccess(user), (authError) => this.handleError(authError)); // Arrow functions to preserve this
  }

  public logout() {
    console.log('logout()');
    // Trigger actual logout
    this.amazon.logout();
    this.notify(NO_USER);
  }

  private handleSuccess(user: User) {
    this.notify(user);
  }

  private handleError(error: AuthError) {
    // Should probably emit an error event of some kind
    console.log(error);
    alert(error);
  }

  private notify(user: User) {
    console.log('notify(' + user + ')');
    // Should notify UI that state has changed
    this.userUpdates.next(user);
  }

}
