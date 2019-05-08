import {Component, HostListener, Input, LOCALE_ID, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import * as d3 from "d3";
import {DecimalPipe} from "@angular/common";

@Component({
  selector: 'wind-cockpit-chart',
  templateUrl: './wind-cockpit-chart.component.html',
  styleUrls: ['./wind-cockpit-chart.component.css']
})
export class WindCockpitChartComponent implements OnInit, OnChanges {

  private _magneticHeading: number;
  private _apparentWindSpeed: number;
  private _courseOverGround: number;
  private _speedOverGround: number;

  @Input()
  set magneticHeading(value: number) {
    this._magneticHeading = value;
    this.drawMagneticHeading();
  };

  @Input()
  set apparentWindSpeed(value: number) {
    this._apparentWindSpeed = value;
    this.drawAWS();
  }

  @Input()
  set courseOverGround(value: number) {
    this._courseOverGround = value;
    this.drawCOG();
  }

  @Input() // unit: kt
  set speedOverGround(value: number) {
    this._speedOverGround = value;
    this.drawSOG();
  }

  @Input() // unit: degree
  private apparentWindAngle: number;

  @Input()
  private width: number = 500;

  @Input()
  private height: number = 500;

  private decimalPipe: DecimalPipe;

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.redrawAll();
  }

  constructor() {
    this.decimalPipe = new DecimalPipe('en-US');
  }

  ngOnInit() {
    this.redrawAll();
  }

  /**
   * Redraws all elements that are created by d3.
   */
  private redrawAll() {
    this.drawWindCircle();
    this.drawSOG();
    this.drawAWS();
    this.drawCOG();
    this.drawMagneticHeading();
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

  ngOnChanges(changes: SimpleChanges): void {
    this.drawWindCircle();
  }

  private drawWindCircle() {
    d3.select('#wind-circle-child').remove();
    d3.select('#wind-circle')
      .append('circle')
      .attr('id', 'wind-circle-child')
      .attr('r', (this.width / 2) * 0.925)
      .attr('stroke', 'white')
      .attr('stroke-width', '2')
      .attr('fill', 'gray');
  }

  private drawSOG() {
    d3.select('#sog-text').remove();
    d3.select('#sog')
      .append('text')
      .attr('id', 'sog-text')
      .attr('transform', 'translate(' + (-1 * this.width * 0.15) + ',' + (this.width * 0.2) + ')')
      .style('font-size', this.getFontSize())
      .text('SOG: ' + this.formatValueForKt(this._speedOverGround));
  }

  private drawAWS() {
    d3.select('#aws-text').remove();
    d3.select('#aws')
      .append('text')
      .attr('id', 'aws-text')
      .attr('transform', 'translate(' + (-1 * this.width * 0.16) + ',' + (this.width * 0.1) + ')')
      .style('font-size', this.getFontSize())
      .text('AWS: ' + this.formatValueForKt(this._apparentWindSpeed));
  }

  private drawCOG() {
    d3.select('#cog-text').remove();
    d3.select('#cog')
      .append('text')
      .attr('id', 'cog-text')
      .attr('transform', 'translate(' + this.width / 80 + ',' + this.width / 15 + ')')
      .style('font-size', this.getFontSize())
      .text('COG: ' + this.formatValueForDegree(this._courseOverGround));
  }

  private drawMagneticHeading() {
    d3.select('#magnetic-text').remove();
    d3.select('#magnetic')
      .append('text')
      .attr('id', 'magnetic-text')
      .attr('transform', 'translate(' + 3 * this.width / 4 + ',' + this.width / 15 + ')')
      .style('font-size', this.getFontSize())
      .text('M: ' + this.formatValueForDegree(this._magneticHeading));
  }

  /**
   * Formats value with one fraction digit and adds knots unit.
   */
  private formatValueForKt(value: number): string {
    if(isNaN(value)) {
      return "n.a.";
    }
    return this.decimalPipe.transform(value, '1.0-1') + ' kt';
  }

  /**
   * Formats value with one fraction digit and adds knots unit.
   */
  private formatValueForDegree(value: number): string {
    if(isNaN(value)) {
      return "n.a.";
    }
    return this.decimalPipe.transform(value, '3.0-0') + '°';
  }

  /**
   * Returns font size based on component size.
   */
  private getFontSize(): string {
    return this.width / 15 + 'px';
  }
}
