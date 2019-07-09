export class WindCockpitDto {
  apparentWind: Wind;
  trueWind: TrueWind;
  speedOverGround: number;
  courseOverGround: Radiant;
  magneticHeading: Radiant;

}

export class Wind {
  speed: number;
  radiant: Radiant;
}

export class TrueWind {
  speed: number;
  angle: Radiant;
  direction: Radiant;
}

export class Radiant {
  value: number;
}
