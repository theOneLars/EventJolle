import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WelcomeComponent} from "./view/welcome/welcome.component";
import {WindCockpitComponent} from "./view/wind-cockpit/wind-cockpit.component";
import {MapViewComponent} from "./view/map-view/map-view.component";

const routes: Routes = [
  {path: 'welcome', component: WelcomeComponent},
  {path: 'wind-cockpit', component: WindCockpitComponent},
  {path: 'map-view', component: MapViewComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
