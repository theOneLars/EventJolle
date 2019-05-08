import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {AppConstants} from "../app.constants";
import {WindCockpitDto} from "../model/wind-cockpit-dto";

declare var EventSource;

@Injectable({
  providedIn: 'root'
})
export class WindCockpitService {


  constructor() { }

  observeMessages(): Observable<WindCockpitDto> {
    return new Observable<WindCockpitDto>(obs => {
      const es = new EventSource(AppConstants.URL_COCKPIT);
      es.addEventListener('message', (evt) => {
        obs.next(JSON.parse(evt.data));
      });
      return () => es.close();
    });
  }
}
