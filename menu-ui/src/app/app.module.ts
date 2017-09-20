import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MenuMainComponent } from './menu-main/menu-main.component';
import { MenuHeaderComponent } from './menu-header/menu-header.component';
import { CollapseModule } from 'ngx-bootstrap/collapse' ;
import { AmazonLoginService } from './amazon-login.service';
import { MenuService } from './menu.service';
import { HttpClientModule } from '@angular/common/http';
import { DateSelectorComponent } from './date-selector/date-selector.component';

@NgModule({
  declarations: [
    AppComponent,
    MenuMainComponent,
    MenuHeaderComponent,
    DateSelectorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CollapseModule.forRoot()
  ],
  providers: [AmazonLoginService, MenuService],
  bootstrap: [AppComponent]
})
export class AppModule { }
