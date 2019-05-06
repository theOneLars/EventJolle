import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'wind-cockpit-chart',
  templateUrl: './wind-cockpit-chart.component.html',
  styleUrls: ['./wind-cockpit-chart.component.css']
})
export class WindCockpitChartComponent implements OnInit {

  private trueWindAngle = 0;

  private speedOverGround = 0;
  private courseOverGround = 0;
  private magneticHeading = 0;
  private aperentWindSpeed = 0;

  @Input() // unit: degree
  private aperentWindAngle = 180;

  @Input()
  private width;

  @Input()
  private height;

  constructor() { }

  ngOnInit() {
  }

  private sinOf(angle: number): number {
    return Math.cos(angle * Math.PI / 180);
  }

  private cosOf(angle: number): number {
    return Math.sin(angle * Math.PI / 180);
  }

}
