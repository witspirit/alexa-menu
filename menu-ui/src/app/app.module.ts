import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
// import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MenuMainComponent } from './menu-editor-page/menu-main/menu-main.component';
import { MenuHeaderComponent } from './menu-header/menu-header.component';
import { CollapseModule } from 'ngx-bootstrap/collapse' ;
import { AmazonLoginService } from './auth/amazon/amazon-login.service';
import { MenuService } from './menu.service';
import { HttpClientModule } from '@angular/common/http';
import { DateSelectorComponent } from './common/date-selector/date-selector.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FooterComponent } from './footer/footer.component';
import { MenuEditorPageComponent } from './menu-editor-page/menu-editor-page.component';
import { PopoverModule } from 'ngx-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DisplayControllerComponent } from './menu-editor-page/display-controller/display-controller.component';
import { CalendarModule } from 'primeng/primeng';
import { PrimeDateSelectorComponent } from './common/prime-date-selector/prime-date-selector.component';
import { DaysSelectorComponent } from './common/days-selector/days-selector.component';
import { AuthService } from './auth/auth.service';

@NgModule({
  declarations: [
    AppComponent,
    MenuMainComponent,
    MenuHeaderComponent,
    DateSelectorComponent,
    FooterComponent,
    MenuEditorPageComponent,
    DisplayControllerComponent,
    PrimeDateSelectorComponent,
    DaysSelectorComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CollapseModule.forRoot(),
    PopoverModule.forRoot(),
    CalendarModule
  ],
  providers: [AmazonLoginService, MenuService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
