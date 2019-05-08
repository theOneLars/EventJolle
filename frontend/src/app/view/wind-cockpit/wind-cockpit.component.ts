import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {WindCockpitService} from "../../service/wind-cockpit.service";
import {WindCockpitDto} from "../../model/wind-cockpit-dto";
import {ConvertUtil} from "../../model/ConvertUtil";

@Component({
  selector: 'app-wind-cockpit',
  templateUrl: './wind-cockpit.component.html',
  styleUrls: ['./wind-cockpit.component.css']
})
export class WindCockpitComponent implements OnInit, OnDestroy {

  private cockpitDataStream: Subscription;

  private apparentWindAngle: number;
  private speedOverGround: number;
  private courseOverGround: number;
  private apparentWindSpeed: number;
  private magneticHeading: number;

  constructor(private windCockpitService: WindCockpitService) {}

  ngOnInit() {
    this.cockpitDataStream = this.windCockpitService.observeMessages()
      .subscribe(message => {
        this.apparentWindAngle = ConvertUtil.radToDegree(message.apparentWind.radiant.value);
        this.speedOverGround = ConvertUtil.msTokt(message.speedOverGround);
        this.courseOverGround = ConvertUtil.radToDegree(message.courseOverGround.value);
        this.apparentWindSpeed = ConvertUtil.msTokt(message.apparentWind.speed)
        this.magneticHeading = ConvertUtil.radToDegree(message.magneticHeading.value)
      });
  }

  ngOnDestroy() {
    if (this.cockpitDataStream) {
      this.cockpitDataStream.unsubscribe();
    }
  }

}
