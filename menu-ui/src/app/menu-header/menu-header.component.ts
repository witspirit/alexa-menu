import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { User } from '../auth/user';

const SIGN_IN = 'Sign In';

@Component({
  selector: 'app-menu-header',
  templateUrl: './menu-header.component.html',
  styleUrls: ['./menu-header.component.scss']
})
export class MenuHeaderComponent implements OnInit, OnChanges {
  @Input() user: User;
  @Output() onLogin = new EventEmitter<void>();
  @Output() onLogout = new EventEmitter<void>();

  isCollapsed = true;
  userText = SIGN_IN;
  loggedIn = false;

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('user update received : ' + JSON.stringify(this.user));
    if (this.user.isLoggedIn()) {
      this.userText = this.user.name;
      this.loggedIn = true;
    } else {
      this.userText = SIGN_IN;
      this.loggedIn = false;
    }
  }

  login() {
    this.onLogin.emit();
  }

  logout() {
    this.onLogout.emit();
  }

}
