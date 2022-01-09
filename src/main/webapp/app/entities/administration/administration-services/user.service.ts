import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { User, IUser } from 'app/entities/artha-models/user.model';

type EntityResponseType = HttpResponse<User>;
type EntityArrayResponseType = HttpResponse<User[]>;

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private resourceUrl = SERVER_API_URL + 'api/user-masters';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-masters';
  private resourceExportUrl = SERVER_API_URL + 'api/_export/user-master';
  private resourceImportUrl = SERVER_API_URL + 'api/_import/user-master';
  private userOrgDeptsSaveUrl = SERVER_API_URL + 'api/multiple/save/user-organization-department-mappings';
  private userOrgDeptUrl = SERVER_API_URL + 'api/user-organization-department-mappings';
  private userOrgDeptSearchUrl = SERVER_API_URL + 'api/_search/user-organization-department-mappings';

  private organizationUrl = SERVER_API_URL + 'api/_search/organizations';
  private userRoleMappingUrl = SERVER_API_URL + 'api/user-organization-role-mappings';
  private userRoleUrl = SERVER_API_URL + 'api/_search/user-organization-role-mappings';

  constructor(private http: HttpClient) {}

  create(userData: User): Observable<User> {
    return this.http.post(this.resourceUrl, userData);
  }

  update(userData: User): Observable<User> {
    return this.http.put(this.resourceUrl, userData);
  }

  find(id: number): Observable<IUser> {
    return this.http.get(`${this.resourceUrl}/${id}`);
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
    return this.http.get<User[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceExportUrl, { params, observe: 'response' });
  }

  importPackages(req?: any, data?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.post(this.resourceImportUrl, data, { params, observe: 'response' });
  }

  createUserOrganizationDepartmentMapping(body: any[]): Observable<any> {
    return this.http.post(this.userOrgDeptsSaveUrl, body);
  }

  getUserOrgDeptMapping(): Observable<HttpResponse<any>> {
    return this.http.get(this.userOrgDeptUrl, { observe: 'response' });
  }

  getUserOrgDeptSearch(req?: any): Observable<HttpResponse<any>> {
    const params = createRequestOption(req);
    return this.http.get(this.userOrgDeptSearchUrl, { params, observe: 'response' });
  }

  updateUserOrganizationDepartmentMapping(body: any[]): Observable<any> {
    return this.http.put(this.userOrgDeptUrl, body);
  }

  deleteUserOrgDept(id): Observable<any> {
    const url = `${this.userOrgDeptUrl}/${id}`;
    return this.http.delete(url);
  }

  findUserOrgDept(id): Observable<any> {
    const url = `${this.userOrgDeptUrl}/${id}`;
    return this.http.get(url);
  }

  getOrganization(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.organizationUrl, { params });
  }

  createUserRoleMapping(req?: any): Observable<any> {
    return this.http.post(this.userRoleMappingUrl, req);
  }

  searchUserRoles(req): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.userRoleUrl, { params });
  }
}
