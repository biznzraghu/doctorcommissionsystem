import { HttpResponse, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared/util/request-util';
import { AdministrationGroup } from 'app/entities/artha-models/group.model';
import { SERVER_API_URL } from 'app/app.constants';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

type EntityTypeResponse = HttpResponse<AdministrationGroup>;
type EntityArrayTypeReponse = HttpResponse<AdministrationGroup[]>;

@Injectable({ providedIn: 'root' })
export class GroupService {
  private resourceUrl = SERVER_API_URL + 'api/groups';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/groups';
  private resourceExportUrl = SERVER_API_URL + 'api/_export/groups';
  private resourceListUrl = SERVER_API_URL + 'api/_search/list/groups';

  constructor(private http: HttpClient) {}

  create(group: AdministrationGroup): Observable<EntityTypeResponse> {
    return this.http.post(this.resourceUrl, group, { observe: 'response' });
  }

  update(group: AdministrationGroup): Observable<EntityTypeResponse> {
    return this.http.put(this.resourceUrl, group, { observe: 'response' });
  }

  find(id: number): Observable<EntityTypeResponse> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<AdministrationGroup[]>(this.resourceUrl, { params, observe: 'response' });
  }

  delete(id: number): Observable<EntityTypeResponse> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<AdministrationGroup[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  export(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<AdministrationGroup[]>(this.resourceExportUrl, { params, observe: 'response' });
  }

  list(req?: any): Observable<EntityArrayTypeReponse> {
    const params = createRequestOption(req);
    return this.http.get<AdministrationGroup[]>(this.resourceListUrl, { params, observe: 'response' });
  }
}

@Injectable({ providedIn: 'root' })
export class GroupDataResolver implements Resolve<any> {
  constructor(private groupService: GroupService) {}

  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      return this.groupService.find(route.params['id']);
    } else {
      return Observable.of(new AdministrationGroup());
    }
  }
}
