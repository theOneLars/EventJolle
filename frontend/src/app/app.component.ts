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

  private topicSubscription: Subscription;
  private model: any;

  constructor(private rxStompService: RxStompService) {
  }

  ngOnInit() {
    let path = AppConstants.MODEL_BROKER_PATH + AppConstants.MODEL_BROKER_UPDATE_PATH;
    this.topicSubscription = this.rxStompService.watch(path).subscribe((message: any) => {
      if (message.body) {
        this.model = JSON.parse(message.body);
        console.log('received model: ', this.model)
      }
    });
  }

}
