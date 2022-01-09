import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';
import { DepartmentPayout } from './department-payout.model';

type EntityResponseType = HttpResponse<DepartmentPayout>;
type EntityArrayResponseType = HttpResponse<DepartmentPayout[]>;

@Injectable()
export class DepartmentPayoutService {
  private resourceUrl = SERVER_API_URL + 'api/department-payouts';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/department-payouts';
  private resourcePayoutSearchUrl = SERVER_API_URL + 'api/payouts/latest-department-payouts';
  private resourceApplicable = 'api/applicable-invoices-type';
  private resourceApplicableOn = '/api/applicable_on';
  private resourceVisitType = 'api/visit-type';
  private resourceService = 'api/_search/all-services';
  private resourceServiceGroup = 'api/_search/groups';
  private resourcePlan = 'api/_search/plans';
  private resourceItemBrand = 'api/_search/items-with-brand';
  private resourceSponsore = 'api/_search/all-sponsors';
  private userUnitMappingUrl = '/api/_search/user-organization-department-mappings';
  private resourceConsultantExport = 'api/_export/applicable-consultant';
  private resourceHSC = 'api/_search/all-hsc';

  private versionListUrl = 'api/department-payouts/distinct-version';

  private resourcelistExport = 'api/_export/department-payout-list';

  constructor(private http: HttpClient) {}

  create(payoutAdjustmentData: DepartmentPayout): Observable<DepartmentPayout> {
    return this.http.post(this.resourceUrl, payoutAdjustmentData);
  }

  update(payoutAdjustmentData: DepartmentPayout): Observable<DepartmentPayout> {
    return this.http.put(this.resourceUrl, payoutAdjustmentData);
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

  search(tab: any, req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);

    return this.http.get<DepartmentPayout[]>(tab === 'PAYOUTS' ? this.resourcePayoutSearchUrl : this.resourceSearchUrl, {
      params,
      observe: 'response'
    });
  }
  getApplicable(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceApplicable, { params, observe: 'response' });
  }
  getApplicableOn(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceApplicableOn, { params, observe: 'response' });
  }
  getVisit(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceVisitType, { params, observe: 'response' });
  }
  searchService(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<DepartmentPayout[]>(this.resourceService, { params, observe: 'response' });
  }
  searchServiceGroup(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<DepartmentPayout[]>(this.resourceServiceGroup, { params, observe: 'response' });
  }
  searchPlan(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<DepartmentPayout[]>(this.resourcePlan, { params, observe: 'response' });
  }
  searchItemBrand(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<DepartmentPayout[]>(this.resourceItemBrand, { params, observe: 'response' });
  }
  searchSponsore(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<DepartmentPayout[]>(this.resourceSponsore, { params, observe: 'response' });
  }
  getUserOrganizationMapping(req): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.userUnitMappingUrl, { params, observe: 'response' });
  }
  exportConsultant(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceConsultantExport, { params, observe: 'response' });
  }
  getHSC(req): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceHSC, { params, observe: 'response' });
  }
  searchByVersionNo(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);

    return this.http.get<DepartmentPayout[]>(this.resourceSearchUrl, {
      params,
      observe: 'response'
    });
  }

  // API call for get version list for Adance Search
  getDepartmentPayoutVersionsList(req) {
    const params = createRequestOption(req);
    return this.http.get(this.versionListUrl, { params });
  }

  exportListPage(rqrText, req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.resourcelistExport + '?latest=' + rqrText, { params, observe: 'response' });
  }
}
