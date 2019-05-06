import { Component, OnInit } from '@angular/core';
import {timer} from "rxjs";

@Component({
  selector: 'app-wind-cockpit',
  templateUrl: './wind-cockpit.component.html',
  styleUrls: ['./wind-cockpit.component.css']
})
export class WindCockpitComponent implements OnInit {

  private aperentWindAngle = 0;

  constructor() { }

  ngOnInit() {
    const source = timer(10, 360);
    const subscribe = source.subscribe(val => {
      this.aperentWindAngle = this.aperentWindAngle + 1;
      console.log(this.aperentWindAngle)
    });
  }

}
