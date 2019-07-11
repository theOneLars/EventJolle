import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EnvVarService {

  public apiKey = 'tbd';

  constructor(private http: HttpClient) {
  }

  public load() {
    const httpObservable = this.http.get('/environments/env-vars.json');

    httpObservable.subscribe((vars: any) => {
      this.apiKey = vars.googleApiKey;
    });

    return httpObservable;
  }


}
