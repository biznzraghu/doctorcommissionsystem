import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
@Injectable({ providedIn: 'root' })
export class UserOrganizationMappingService {
  private resourceUrl = SERVER_API_URL + 'api/user-organization-mappings';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-organization-mappings';

  constructor(private http: HttpClient) {}

  query(req?: any): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl, { params, observe: 'response' });
  }

  search(req?: any): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  searchByType(req?: any, searchFields?: string, searchType?: string): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(this.resourceSearchUrl + '/' + searchType + '/' + searchFields, { params, observe: 'response' });
  }

  searchUnitFromUnitField(req?: any): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }
}
