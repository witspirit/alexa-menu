import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { AmazonLoginComponent } from './amazon-login/amazon-login.component';
import { MenuMainComponent } from './menu-main/menu-main.component';

@NgModule({
  declarations: [
    AppComponent,
    AmazonLoginComponent,
    MenuMainComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
