import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { ValueSetCode } from 'app/entities/artha-models/value-set-code.model';

type EntityResponseType = HttpResponse<ValueSetCode>;
type EntityArrayResponseType = HttpResponse<ValueSetCode[]>;

@Injectable({
  providedIn: 'root'
})
export class ValueSetCodeService {
  private resourceUrl = SERVER_API_URL + 'api/value-set-codes';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/value-set-codes';
  private resourceExportUrl = SERVER_API_URL + 'api/_export/value-set-codes';

  constructor(private http: HttpClient) {}

  create(valueSetCode: ValueSetCode): Observable<ValueSetCode> {
    return this.http.post(this.resourceUrl, valueSetCode);
  }

  update(valueSetCode: ValueSetCode): Observable<ValueSetCode> {
    return this.http.put(this.resourceUrl, valueSetCode);
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceUrl, { params, observe: 'response' });
  }

  delete(id: number): Observable<EntityResponseType> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<ValueSetCode[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceExportUrl, { params, observe: 'response' });
  }

  genders(req?: any) {
    const reqParams = req ? req : {};
    reqParams['query'] = 'valueSet.code.raw:1.25.2.1.1 ' + (reqParams['query'] ? reqParams['query'] : '');
    reqParams['sort'] = ['display.sort,asc'];
    reqParams['size'] = 9999;
    return this.search(reqParams);
  }

  getTariffClasses(): Observable<EntityResponseType> {
    const req = {};
    req['query'] = 'valueSet.code.raw:TARIFF_CLASS';
    req['sort'] = ['display.sort,asc'];
    req['size'] = 9999;

    const params = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, { params, observe: 'response' });
  }

  getSponsorType(): Observable<EntityResponseType> {
    const req = {};
    req['query'] = 'valueSet.code:12345';
    req['size'] = 9999;

    const params = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, { params, observe: 'response' });
  }
}
