import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MenuMainComponent } from './menu-main/menu-main.component';
import { MenuHeaderComponent } from './menu-header/menu-header.component';
import { CollapseModule } from 'ngx-bootstrap/collapse' ;
import { AmazonLoginService } from './amazon-login.service';

@NgModule({
  declarations: [
    AppComponent,
    MenuMainComponent,
    MenuHeaderComponent
  ],
  imports: [
    BrowserModule,
    CollapseModule.forRoot()
  ],
  providers: [AmazonLoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }
