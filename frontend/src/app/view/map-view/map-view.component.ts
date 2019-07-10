import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PositionService} from "../../service/position.service";
import {PositionDto} from "../../model/position-dto";

@Component({
  selector: 'app-map-view',
  templateUrl: './map-view.component.html',
  styleUrls: ['./map-view.component.css']
})
export class MapViewComponent implements OnInit, OnDestroy {

  private positionDataStream: Subscription;

  private longitude: number;
  private latitude: number;

  private positions: Array<PositionDto> = new Array();

  constructor(private positionService: PositionService) {}

  ngOnInit() {
    this.positionDataStream = this.positionService.observeMessages()
      .subscribe(message => {
        this.positions.push(message);
          this.latitude = message.latitude;
          this.longitude = message.longitude;
          console.log(this.latitude, this.longitude);
      });
  }

  ngOnDestroy() {
    if (this.positionDataStream) {
      this.positionDataStream.unsubscribe();
    }
  }

}
