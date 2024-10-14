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

import { createGroup } from '../fn/group-controller/create-group';
import { CreateGroup$Params } from '../fn/group-controller/create-group';
import { createMembership } from '../fn/group-controller/create-membership';
import { CreateMembership$Params } from '../fn/group-controller/create-membership';
import { findAll } from '../fn/group-controller/find-all';
import { FindAll$Params } from '../fn/group-controller/find-all';
import { GroupResponse } from '../models/group-response';
import { MemberResponse } from '../models/member-response';

@Injectable({ providedIn: 'root' })
export class GroupControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createGroup()` */
  static readonly CreateGroupPath = '/api/groups/create';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createGroup()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createGroup$Response(params: CreateGroup$Params, context?: HttpContext): Observable<StrictHttpResponse<GroupResponse>> {
    return createGroup(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createGroup$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createGroup(params: CreateGroup$Params, context?: HttpContext): Observable<GroupResponse> {
    return this.createGroup$Response(params, context).pipe(
      map((r: StrictHttpResponse<GroupResponse>): GroupResponse => r.body)
    );
  }

  /** Path part for operation `createMembership()` */
  static readonly CreateMembershipPath = '/api/groups/createMembership';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createMembership()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createMembership$Response(params: CreateMembership$Params, context?: HttpContext): Observable<StrictHttpResponse<MemberResponse>> {
    return createMembership(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createMembership$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createMembership(params: CreateMembership$Params, context?: HttpContext): Observable<MemberResponse> {
    return this.createMembership$Response(params, context).pipe(
      map((r: StrictHttpResponse<MemberResponse>): MemberResponse => r.body)
    );
  }

  /** Path part for operation `findAll()` */
  static readonly FindAllPath = '/api/groups';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll$Response(params?: FindAll$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<GroupResponse>>> {
    return findAll(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll(params?: FindAll$Params, context?: HttpContext): Observable<Array<GroupResponse>> {
    return this.findAll$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<GroupResponse>>): Array<GroupResponse> => r.body)
    );
  }

}
