export class WindCockpitDto {
  apparentWind: Wind;
  trueWind: Wind;
  speedOverGround: number;
  courseOverGround: Radiant;
  magneticHeading: Radiant;

}

export class Wind {
  speed: number;
  radiant: Radiant;
}

export class Radiant {
  value: number;
}
