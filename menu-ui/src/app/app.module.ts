import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
// import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MenuMainComponent } from './menu-editor-page/menu-main/menu-main.component';
import { MenuHeaderComponent } from './menu-header/menu-header.component';
import { CollapseModule } from 'ngx-bootstrap/collapse' ;
import { AmazonLoginService } from './amazon-login.service';
import { MenuService } from './menu.service';
import { HttpClientModule } from '@angular/common/http';
import { DateSelectorComponent } from './menu-editor-page/date-selector/date-selector.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MenuEditorComponent } from './menu-editor-page/menu-main/menu-editor/menu-editor.component';
import { FooterComponent } from './footer/footer.component';
import { MenuEditorPageComponent } from './menu-editor-page/menu-editor-page.component';

@NgModule({
  declarations: [
    AppComponent,
    MenuMainComponent,
    MenuHeaderComponent,
    DateSelectorComponent,
    MenuEditorComponent,
    FooterComponent,
    MenuEditorPageComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    CollapseModule.forRoot()
  ],
  providers: [AmazonLoginService, MenuService],
  bootstrap: [AppComponent]
})
export class AppModule { }
