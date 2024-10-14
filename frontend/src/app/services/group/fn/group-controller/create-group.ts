/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { GroupRequest } from '../../models/group-request';
import { GroupResponse } from '../../models/group-response';

export interface CreateGroup$Params {
      body: GroupRequest
}

export function createGroup(http: HttpClient, rootUrl: string, params: CreateGroup$Params, context?: HttpContext): Observable<StrictHttpResponse<GroupResponse>> {
  const rb = new RequestBuilder(rootUrl, createGroup.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<GroupResponse>;
    })
  );
}

createGroup.PATH = '/api/groups/create';
