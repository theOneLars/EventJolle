import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {AppConstants} from "../app.constants";
import {PositionDto} from "../model/position-dto";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

declare var EventSource;

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  constructor(private http: HttpClient) {
  }

  observeMessages(): Observable<PositionDto> {
    return new Observable<PositionDto>(obs => {
      const es = new EventSource(AppConstants.URL_TODAY_LIVE_POSITION);
      es.addEventListener('message', (evt) => {
        obs.next(JSON.parse(evt.data));
      });
      return () => es.close();
    });
  }

  loadPositionsFor(date: Date): Observable<PositionDto> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params: new HttpParams().set('date', date.toISOString().split('T')[0])
    };

    return this.http.get<PositionDto>(AppConstants.URL_POSITIONS_BY_DATE, httpOptions);
  }
}
