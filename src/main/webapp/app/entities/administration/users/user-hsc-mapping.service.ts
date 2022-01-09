import { createRequestOption } from '../../../shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';
import { Injectable } from '@angular/core';

import { HttpResponse, HttpClient } from '@angular/common/http';
import { UserHSCMapping } from './models/user-hsc-mapping.model';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<UserHSCMapping>;
type EntityArrayResponseType = HttpResponse<UserHSCMapping[]>;

@Injectable({
  providedIn: 'root'
})
export class UserHSCMappingService {
  private resourceUrl = `${SERVER_API_URL}api/user-hsc-mappings`;
  private resourceSearchUrl = `${SERVER_API_URL}api/_search/user-hsc-mappings`;
  private resourceExportUrl = `${SERVER_API_URL}/api/_export/user-hsc-mappings`;

  constructor(private http: HttpClient) {}

  create(userHSCMapping: UserHSCMapping): Observable<EntityResponseType> {
    const copy: UserHSCMapping = Object.assign({}, userHSCMapping);
    return this.http.post<UserHSCMapping>(this.resourceUrl, copy, { observe: 'response' });
  }

  update(userHSCMapping: UserHSCMapping): Observable<EntityResponseType> {
    const copy: UserHSCMapping = Object.assign({}, userHSCMapping);
    return this.http.put<UserHSCMapping>(this.resourceUrl, copy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<UserHSCMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserHSCMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserHSCMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceExportUrl, { params: options, observe: 'response' });
  }

  list(type: string, fields: string, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserHSCMapping[]>(this.resourceSearchUrl + '/' + type + '/' + fields, { params: options, observe: 'response' });
  }

  deleteUserHSCMappingByUserIdHscId(req?: any): Observable<HttpResponse<{}>> {
    const options = createRequestOption(req);
    return this.http.delete(this.resourceUrl, { params: options, observe: 'response' });
  }
}
