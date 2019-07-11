import {Injectable} from '@angular/core';
import {LazyMapsAPILoaderConfigLiteral} from "@agm/core";
import {EnvVarService} from "./env-var.service";

@Injectable()
export class GoogleMapsConfig implements LazyMapsAPILoaderConfigLiteral {


  constructor(private envVarService: EnvVarService) {
  }

  get apiKey() {
    debugger;
    return this.envVarService.apiKey;
  }

}
