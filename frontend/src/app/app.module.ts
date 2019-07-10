import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TopNavigationComponent} from './navigation/top-navigation/top-navigation.component';
import {
  MatButtonModule,
  MatCardModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatToolbarModule
} from "@angular/material";
import {NavigationComponent} from './navigation/navigation.component';
import {SidebarComponent} from "./navigation/sidebar/sidebar.component";
import {LayoutModule} from "@angular/cdk/layout";
import {WelcomeComponent} from './view/welcome/welcome.component';
import {WindCockpitComponent} from './view/wind-cockpit/wind-cockpit.component';
import {WindCockpitChartComponent} from './charts/wind-cockpit-chart/wind-cockpit-chart.component';
import {WindCockpitService} from "./service/wind-cockpit.service";
import {GoogleMapsChartComponent} from './charts/google-maps-chart/google-maps-chart.component';
import {AgmCoreModule, LAZY_MAPS_API_CONFIG} from "@agm/core";
import {MapViewComponent} from "./view/map-view/map-view.component";
import {EnvVarService} from "./core/env-var.service";
import {GoogleMapsConfig} from "./core/google-maps.config";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    TopNavigationComponent,
    SidebarComponent,
    NavigationComponent,
    WelcomeComponent,
    WindCockpitComponent,
    WindCockpitChartComponent,
    GoogleMapsChartComponent,
    MapViewComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatCardModule,
    LayoutModule,
    AgmCoreModule.forRoot()
  ],
  providers: [
    {provide: LAZY_MAPS_API_CONFIG, useClass: GoogleMapsConfig},
    WindCockpitService,
    EnvVarService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
