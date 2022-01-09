import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { ServiceMaster } from './../../artha-models/service-master.model';

type EntityResponseType = HttpResponse<ServiceMaster>;
type EntityArrayResponseType = HttpResponse<ServiceMaster[]>;

@Injectable({
    providedIn: 'root'
})
export class ServiceMasterService {
    private resourceUrl = SERVER_API_URL + 'api/service-masters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/service-masters';
  
    constructor(private http: HttpClient) {}
  
    create(serviceMaster: ServiceMaster): Observable<EntityResponseType> {
      const copy: ServiceMaster = Object.assign({}, serviceMaster);
      return this.http.post<ServiceMaster>(this.resourceUrl, copy, { observe: 'response' });
    }
  
    update(serviceMaster: ServiceMaster): Observable<EntityResponseType> {
      const copy: ServiceMaster = Object.assign({}, serviceMaster);
      return this.http.put<ServiceMaster>(this.resourceUrl, copy, { observe: 'response' });
    }
  
    find(id: number): Observable<EntityResponseType> {
      return this.http.get<ServiceMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
  
    query(req?: any): Observable<EntityArrayResponseType> {
      const options = createRequestOption(req);
      return this.http.get<ServiceMaster[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
  
    delete(id: number): Observable<HttpResponse<{}>> {
      return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
  
    search(req?: any): Observable<EntityArrayResponseType> {
      const options = createRequestOption(req);
      return this.http.get<ServiceMaster[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
  
  }