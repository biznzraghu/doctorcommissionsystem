import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { Unit } from 'app/entities/artha-models/unit.model';

type EntityResponseType = HttpResponse<Unit>;
type EntityArrayResponseType = HttpResponse<Unit[]>;

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private resourceUrl = SERVER_API_URL + 'api/organizations';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/organizations';
  private resourceExportUrl = SERVER_API_URL + 'api/_export/unit';
  private resourceImportUrl = SERVER_API_URL + 'api/_import/organization-masters';

  constructor(private http: HttpClient) {}

  create(unitData: Unit): Observable<Unit> {
    return this.http.post(this.resourceUrl, unitData);
  }

  update(unitData: Unit): Observable<Unit> {
    return this.http.put(this.resourceUrl, unitData);
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
    return this.http.get<Unit[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceExportUrl, { params, observe: 'response' });
  }

  importPackages(req?: any, data?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.post(this.resourceImportUrl, data, { params, observe: 'response' });
  }
}
