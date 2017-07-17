import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-amazon-login',
  templateUrl: './amazon-login.component.html',
  styleUrls: ['./amazon-login.component.css']
})
export class AmazonLoginComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

  doLogin() {

    let handleResponse = function (response) {
      console.log('onAuthorizeResponse');

      if (response.error) {
        alert('oauth error ' + response.error + ' - ' + response.error_description);
        return;
      }
      alert('success: ' + response.access_token);
      console.log('Access Token: ' + response.access_token);

      const token = response.access_token;

      console.log('Calling retrieveProfile');
      // Need to register some Typings binding for this to work :-( It exists, but I need to figure out how to configure it.
      amazon.Login.retrieveProfile(token, function (response) {
        console.log('Profile Response:' + JSON.stringify(response));
      });
    };

    document.getElementById('LoginWithAmazon').onclick = function () {
      console.log('Pressed LoginWithAmazon');
      const options = {scope: 'profile'};
      amazon.Login.authorize(options, handleResponse);
      return false;
    };

  }

}
