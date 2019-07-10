import {Component, Input, OnInit} from '@angular/core';
import {PositionDto} from "../../model/position-dto";

@Component({
  selector: 'google-maps-chart',
  templateUrl: './google-maps-chart.component.html',
  styleUrls: ['./google-maps-chart.component.css']
})
export class GoogleMapsChartComponent implements OnInit {

  @Input()
  public longitude: number;

  @Input()
  public latitude: number;

  @Input()
  public positions: Array<PositionDto>;

  zoom: number = 15;

  constructor() { }

  ngOnInit() {
  }

}
