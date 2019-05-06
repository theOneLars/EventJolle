import { Component } from '@angular/core';
import {AppConstants} from "./app.constants";
import {RxStompService} from "@stomp/ng2-stompjs";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'jollen-cockpit';

  constructor() {
  }

  ngOnInit() {
  }

}
