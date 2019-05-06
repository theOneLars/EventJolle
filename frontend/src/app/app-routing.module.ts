import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WelcomeComponent} from "./view/welcome/welcome.component";
import {WindCockpitComponent} from "./view/wind-cockpit/wind-cockpit.component";

const routes: Routes = [
  {path: 'welcome', component: WelcomeComponent},
  {path: 'wind-cockpit', component: WindCockpitComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
