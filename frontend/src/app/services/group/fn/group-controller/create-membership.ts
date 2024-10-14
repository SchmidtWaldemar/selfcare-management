/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MemberRequest } from '../../models/member-request';
import { MemberResponse } from '../../models/member-response';

export interface CreateMembership$Params {
      body: MemberRequest
}

export function createMembership(http: HttpClient, rootUrl: string, params: CreateMembership$Params, context?: HttpContext): Observable<StrictHttpResponse<MemberResponse>> {
  const rb = new RequestBuilder(rootUrl, createMembership.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<MemberResponse>;
    })
  );
}

createMembership.PATH = '/api/groups/createMembership';
