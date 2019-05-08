import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'wind-cockpit-chart',
  templateUrl: './wind-cockpit-chart.component.html',
  styleUrls: ['./wind-cockpit-chart.component.css']
})
export class WindCockpitChartComponent implements OnInit {

  private trueWindAngle = 0;

  @Input()
  private magneticHeading = 0;

  @Input()
  private apparentWindSpeed: number;

  @Input() // unit: degree
  private courseOverGround: number;

  @Input() // unit: degree
  private apparentWindAngle: number;

  @Input() // unit: kt
  private speedOverGround: number;

  @Input()
  private width: number;

  @Input()
  private height: number;

  constructor() {
  }


  ngOnInit() {
  }

  private getArcRadius(): number {
    return (this.width / 2) * 0.87;
  }

  private sinOf(angle: number): number {
    if (isNaN(angle)) {
      return 0;
    }
    return Math.cos(angle * Math.PI / 180);
  }

  private cosOf(angle: number): number {
    if (isNaN(angle)) {
      return 0;
    }
    return Math.sin(angle * Math.PI / 180);
  }

}
