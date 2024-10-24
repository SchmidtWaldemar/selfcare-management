/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { apiModeratorsCreatePost } from '../fn/application-starter/api-moderators-create-post';
import { ApiModeratorsCreatePost$Params } from '../fn/application-starter/api-moderators-create-post';
import { ModeratorResponse } from '../models/moderator-response';

@Injectable({ providedIn: 'root' })
export class ApplicationStarterService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `apiModeratorsCreatePost()` */
  static readonly ApiModeratorsCreatePostPath = '/api/moderators/create';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `apiModeratorsCreatePost()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  apiModeratorsCreatePost$Response(params?: ApiModeratorsCreatePost$Params, context?: HttpContext): Observable<StrictHttpResponse<ModeratorResponse>> {
    return apiModeratorsCreatePost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `apiModeratorsCreatePost$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  apiModeratorsCreatePost(params?: ApiModeratorsCreatePost$Params, context?: HttpContext): Observable<ModeratorResponse> {
    return this.apiModeratorsCreatePost$Response(params, context).pipe(
      map((r: StrictHttpResponse<ModeratorResponse>): ModeratorResponse => r.body)
    );
  }

}
