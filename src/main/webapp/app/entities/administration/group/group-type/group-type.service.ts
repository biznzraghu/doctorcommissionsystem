import { Injectable } from '@angular/core';
// import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import { GroupType } from './group-type.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';

type EntityTypeResponse = HttpResponse<GroupType>;
type EntityArrayTypeReponse = HttpResponse<GroupType[]>;

@Injectable()
export class GroupTypeService {
  private resourceUrl = SERVER_API_URL + 'api/group-types';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/group-types';

  constructor(private http: HttpClient) {}

  create(groupType: GroupType): Observable<EntityTypeResponse> {
    return this.http.post(this.resourceUrl, groupType, { observe: 'response' });
  }

  update(groupType: GroupType): Observable<EntityTypeResponse> {
    return this.http.put(this.resourceUrl, groupType, { observe: 'response' });
  }

  find(id: number): Observable<EntityTypeResponse> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<GroupType[]>(this.resourceUrl, { params, observe: 'response' });
  }

  delete(id: number): Observable<EntityTypeResponse> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<GroupType[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }
}
