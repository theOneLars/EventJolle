import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {WindCockpitService} from "../../service/wind-cockpit.service";
import {ConvertUtil} from "../../model/ConvertUtil";

@Component({
  selector: 'app-wind-cockpit',
  templateUrl: './wind-cockpit.component.html',
  styleUrls: ['./wind-cockpit.component.css']
})
export class WindCockpitComponent implements OnInit, OnDestroy {

  private cockpitDataStream: Subscription;

  public apparentWindAngle: number;
  public speedOverGround: number;
  public courseOverGround: number;
  public apparentWindSpeed: number;
  public magneticHeading: number;
  public trueWindSpeed: number;
  public trueWindAngle: number;
  public trueWindDirection: number;
  public velocityMadeGood: number;


  constructor(private windCockpitService: WindCockpitService) {}

  ngOnInit() {
    this.cockpitDataStream = this.windCockpitService.observeMessages()
      .subscribe(message => {
        this.apparentWindAngle = ConvertUtil.radToDegree(message.apparentWind.radiant.value);
        this.speedOverGround = ConvertUtil.msTokt(message.speedOverGround);
        this.courseOverGround = ConvertUtil.radToDegree(message.courseOverGround.value);
        this.apparentWindSpeed = ConvertUtil.msTokt(message.apparentWind.speed);
        this.magneticHeading = ConvertUtil.radToDegree(message.magneticHeading.value);
        this.trueWindSpeed = ConvertUtil.msTokt(message.trueWind.speed);
        this.trueWindAngle = ConvertUtil.radToDegree(message.trueWind.angle.value);
        this.trueWindDirection = ConvertUtil.radToDegree(message.trueWind.direction.value);
        this.velocityMadeGood = ConvertUtil.msTokt(message.velocityMadeGood);
      });
  }

  ngOnDestroy() {
    if (this.cockpitDataStream) {
      this.cockpitDataStream.unsubscribe();
    }
  }

}
