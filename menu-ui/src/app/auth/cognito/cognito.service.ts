import { Injectable } from '@angular/core';
import { CognitoAuth, AWSCognito } from 'amazon-cognito-auth-js/dist/amazon-cognito-auth';

@Injectable()
export class CognitoService {
  private auth;

  constructor() {
    const authData = {
      ClientId: '69hh6ogikt9laiv0eto691b78t', // Your client id here - dev-experiment for now
      AppWebDomain: 'https://menu.auth.eu-west-1.amazoncognito.com',
      TokenScopesArray: ['email', 'profile'],
      RedirectUriSignIn: 'http://localhost:4200',
      RedirectUriSignOut: 'http://localhost:4200',
      IdentityProvider: 'Cognito User Pool',
      UserPoolId: 'eu-west-1_zoPFtXmEL', // Your user pool id here
      AdvancedSecurityDataCollectionFlag: true
    };
    this.auth = new AWSCognito.CognitoIdentityServiceProvider.CognitoAuth(authData);
    // Not sure what this does... But it is mentioned in the GitHub for the SDK...
    const curUrl = window.location.href;
    this.auth.parseCognitoWebResponse(curUrl);
  }

  public login(onSuccess: (User) => void, onError: (AuthError) => void) {
    this.auth.userhandler = {
      onSuccess: (result) => {
        console.log('cognito userhandler onSuccess: ' + result);
        // call onSuccess with the user if we have all info
      },
      onFailure: (err) => {
        console.log('cognito userhandler onFailure: ' + err);
      }
    };

    this.auth.getSession();
  }

  public logout() {
    this.auth.signOut();
  }

}
