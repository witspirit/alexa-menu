export class User {
  readonly accessToken: string;
  name: string;
  email: string;

  constructor(accessToken: string) {
    this.accessToken = accessToken;
    this.name = null;
    this.email = null;
  }

  setProfile(name: string, email: string) {
    this.name = name;
    this.email = email;
  }

  isLoggedIn() {
    return this.accessToken !== null;
  }
}
