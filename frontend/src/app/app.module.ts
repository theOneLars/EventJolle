import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RxStompConfig} from "./config/rx-stomp.config";
import {TopNavigationComponent} from './navigation/top-navigation/top-navigation.component';
import {
  MatButtonModule,
  MatIconModule,
  MatInputModule,
  MatListModule, MatSelectModule,
  MatSidenavModule,
  MatToolbarModule,
  MatSnackBarModule, MatCardModule
} from "@angular/material";
import {NavigationComponent} from './navigation/navigation.component';
import {SidebarComponent} from "./navigation/sidebar/sidebar.component";
import {LayoutModule} from "@angular/cdk/layout";
import { WelcomeComponent } from './view/welcome/welcome.component';
import { WindCockpitComponent } from './view/wind-cockpit/wind-cockpit.component';
import { WindCockpitChartComponent } from './charts/wind-cockpit-chart/wind-cockpit-chart.component';

@NgModule({
  declarations: [
    AppComponent,
    TopNavigationComponent,
    SidebarComponent,
    NavigationComponent,
    WelcomeComponent,
    WindCockpitComponent,
    WindCockpitChartComponent
  ],
  imports: [
    BrowserModule,
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
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: RxStompConfig
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
