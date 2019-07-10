import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {AppConstants} from "../app.constants";
import {PositionDto} from "../model/position-dto";

declare var EventSource;

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  constructor() {
  }

  observeMessages(): Observable<PositionDto> {
    return new Observable<PositionDto>(obs => {
      const es = new EventSource(AppConstants.URL_POSITION);
      es.addEventListener('message', (evt) => {
        obs.next(JSON.parse(evt.data));
      });
      return () => es.close();
    });
  }

}
