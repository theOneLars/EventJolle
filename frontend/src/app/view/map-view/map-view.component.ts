import {Component, OnDestroy, OnInit} from '@angular/core';
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

  public longitude: number;
  public latitude: number;

  public positions: Array<PositionDto> = [];

  constructor(private positionService: PositionService) {
  }

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
