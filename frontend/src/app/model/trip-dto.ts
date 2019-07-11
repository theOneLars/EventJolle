import {PositionDto} from "./position-dto";

export class TripDto {
  date: Date;
  id: string;
  positions: Array<PositionDto>;
}
