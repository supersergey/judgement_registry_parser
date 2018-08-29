import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { KeywordsComponent } from './keywords/keywords.component';
import { FormsModule } from '@angular/forms';
import { KeywordDetailComponent } from './keyword-detail/keyword-detail.component';
import { MessagesComponent } from './messages/messages.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HttpClient } from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    KeywordsComponent,
    KeywordDetailComponent,
    MessagesComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClient
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
