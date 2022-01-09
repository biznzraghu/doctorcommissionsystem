import { Injectable } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { UserOrganizationRoleMapping, UserHSCRoleMapping } from './models/user-organization-role-mapping.model';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';

type EntityResponseType = HttpResponse<UserOrganizationRoleMapping>;
type EntityArrayResponseType = HttpResponse<UserOrganizationRoleMapping[]>;

@Injectable({
  providedIn: 'root'
})
export class UserOrganizationRoleMappingService {
  private resourceUrl = `${SERVER_API_URL}api/user-organization-role-mappings`;
  private resourceSearchUrl = `${SERVER_API_URL}api/_search/user-organization-role-mappings`;
  private resourceExportUrl = `${SERVER_API_URL}api/_export/user-organization-role-mappings`;
  private userHscRoleResourceUrl = `${SERVER_API_URL}api/user-hsc-role-mappings`;
  private userHscRoleSearchUrl = `${SERVER_API_URL}api/_search/user-hsc-role-mappings`;
  private roleOptions = createRequestOption({ query: '*' });
  allRoles$ = this.http.get<UserOrganizationRoleMapping[]>(this.resourceSearchUrl, { params: this.roleOptions });
  constructor(private http: HttpClient) {}

  create(userOrganizationRoleMapping: any): Observable<EntityResponseType> {
    const copy: UserOrganizationRoleMapping = Object.assign({}, userOrganizationRoleMapping);
    return this.http.post<UserOrganizationRoleMapping>(this.resourceUrl, copy, { observe: 'response' });
  }

  update(userOrganizationRoleMapping: UserOrganizationRoleMapping): Observable<EntityResponseType> {
    const copy: UserOrganizationRoleMapping = Object.assign({}, userOrganizationRoleMapping);
    return this.http.put<UserOrganizationRoleMapping>(this.resourceUrl, copy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<UserOrganizationRoleMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserOrganizationRoleMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserOrganizationRoleMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  export(req?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceExportUrl, { params: options, observe: 'response' });
  }

  createUserHSCRoleMapping(userHSCRoleMapping: UserHSCRoleMapping): Observable<HttpResponse<UserHSCRoleMapping>> {
    const copy: UserHSCRoleMapping = Object.assign({}, userHSCRoleMapping);
    return this.http.post<UserHSCRoleMapping>(this.userHscRoleResourceUrl, copy, { observe: 'response' });
  }

  deleteUsingUserIdHSCIdRoleID(userid, hscid, roleid): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.userHscRoleResourceUrl}/${userid}/${hscid}/${roleid}`, { observe: 'response' });
  }

  deleteByUserOrganizationRoleIds(userid, orgid, roleid): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${userid}/${orgid}/${roleid}`, { observe: 'response' });
  }

  searchUserHSCRoleMappings(req?: any): Observable<HttpResponse<UserHSCRoleMapping[]>> {
    const options = createRequestOption(req);
    return this.http.get<UserHSCRoleMapping[]>(this.userHscRoleSearchUrl, { params: options, observe: 'response' });
  }

  get(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<UserOrganizationRoleMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
