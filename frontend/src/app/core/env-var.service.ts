import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EnvVarService {

  public apiKey = 'tbd';

  constructor(private http: HttpClient) {
    // debugger;
    // this.http.get('/env-vars.json').subscribe(vars => {
    //   // Todo: get api key from env vars
    //   debugger;
    // });
  }


}
