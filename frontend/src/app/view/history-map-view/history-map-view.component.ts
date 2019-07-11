import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {MatDatepickerInputEvent} from "@angular/material";
import {TripService} from "../../service/trip.service";
import {TripDto} from "../../model/trip-dto";

@Component({
  selector: 'history-map-view',
  templateUrl: './history-map-view.component.html',
  styleUrls: ['./history-map-view.component.css']
})
export class HistoryMapViewComponent implements OnInit {

  private tripsDataStream: Subscription;

  public longitude: number;
  public latitude: number;
  public trips: Array<TripDto> = [];

  public from: Date = new Date();
  public to: Date = new Date();

  constructor(private tripService: TripService) {
  }

  changeFromDate(event: MatDatepickerInputEvent<Date>): void {
    this.from = event.value;
  }

  changeToDate(event: MatDatepickerInputEvent<Date>): void {
    this.to = event.value;
  }

  loadTrips(): void {
    this.tripsDataStream = this.tripService.loadTrips(this.from, this.to)
      .subscribe((trips: Array<TripDto>) => {
        if (trips && trips.length) {
          this.trips = trips;

          this.latitude = trips[0].positions[0].latitude || 0;
          this.longitude = trips[0].positions[0].longitude || 0;
        } else {
          this.trips = [];
          this.latitude = 0;
          this.longitude = 0;
        }
      });
  }

  ngOnInit() {
    this.loadTrips();
  }

  ngOnDestroy() {
    if (this.tripsDataStream) {
      this.tripsDataStream.unsubscribe();
    }
  }
}
