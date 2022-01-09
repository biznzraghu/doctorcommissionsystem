import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { Department } from 'app/entities/artha-models/department.model';

type EntityResponseType = HttpResponse<Department>;
type EntityArrayResponseType = HttpResponse<Department[]>;

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private resourceUrl = SERVER_API_URL + 'api/departments';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/departments';
  private resourceExportUrl = SERVER_API_URL + 'api/_export/departments';
  private deptMasterSearchURL = SERVER_API_URL + 'api/_search/department-masters';

  private userOrganizationDepartmentMapping = SERVER_API_URL + 'api/_search/user-organization-department-mappings';
  constructor(private http: HttpClient) {}

  create(departmentData: Department): Observable<Department> {
    return this.http.post(this.resourceUrl, departmentData);
  }

  update(departmentData: Department): Observable<Department> {
    return this.http.put(this.resourceUrl, departmentData);
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
    return this.http.get<Department[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  userOrganizationDepartmentMappingSearch(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(this.userOrganizationDepartmentMapping, { params, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceExportUrl, { params, observe: 'response' });
  }

  searchDepartment(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<Department[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }
}
