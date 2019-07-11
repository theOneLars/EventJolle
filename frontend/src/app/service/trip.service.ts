import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppConstants} from "../app.constants";
import {TripDto} from "../model/trip-dto";

@Injectable({
  providedIn: 'root'
})
export class TripService {

  constructor(private http: HttpClient) {
  }

  loadTrips(from: Date, to: Date): Observable<Array<TripDto>> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params: new HttpParams()
        .set('from', from.toISOString().split('T')[0])
        .set('to', to.toISOString().split('T')[0])
    };

    return this.http.get<TripDto[]>(AppConstants.URL_TRIPS_FOR_RANGE, httpOptions);
  }
}
