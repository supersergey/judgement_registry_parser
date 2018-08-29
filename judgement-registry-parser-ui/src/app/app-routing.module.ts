import { NgModule } from '@angular/core';
import { Routes, RouterModule } from "@angular/router";
import { KeywordsComponent } from "./keywords/keywords.component";
import { KeywordDetailComponent } from "./keyword-detail/keyword-detail.component";
import { DashboardComponent} from "./dashboard/dashboard.component";

const routes : Routes = [
  {path: 'keywords', component: KeywordsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'keyword/:name', component: KeywordDetailComponent},
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


