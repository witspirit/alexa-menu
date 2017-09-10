import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { AmazonLoginComponent } from './amazon-login/amazon-login.component';
import { MenuMainComponent } from './menu-main/menu-main.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { MenuHeaderComponent } from './menu-header/menu-header.component';
import { CollapseModule } from 'ngx-bootstrap/collapse' ;
import { AmazonLoginService } from './amazon-login.service';

@NgModule({
  declarations: [
    AppComponent,
    AmazonLoginComponent,
    MenuMainComponent,
    MenuHeaderComponent
  ],
  imports: [
    BrowserModule,
    BsDropdownModule.forRoot(),
    CollapseModule.forRoot()
  ],
  providers: [AmazonLoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }
