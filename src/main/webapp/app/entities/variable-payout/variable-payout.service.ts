import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { VariablePayout, ServiceItemBenefitTemplate, ServiceItemBenefit } from './../artha-models/variable-payout.model';
import { Plan } from './../artha-models/plan.model';
import { VariablePayoutTemplate } from '../artha-models/variable-payout-template.model';
import { AnyPtrRecord } from 'dns';

type EntityResponseType = HttpResponse<VariablePayout>;
type EntityArrayResponseType = HttpResponse<VariablePayout[]>;

type TemplateEntityResponseType = HttpResponse<VariablePayoutTemplate>;
type TemplateEntityArrayResponseType = HttpResponse<VariablePayoutTemplate[]>;
@Injectable({
  providedIn: 'root'
})
export class VariablePayoutService {
  public formDirty: Boolean;
  private resourceUrl = SERVER_API_URL + 'api/variable-payouts';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/variable-payouts';
  private serviceItemExceptionsResource = SERVER_API_URL + 'api/service-item-exceptions';
  private getTariffClassListResource = SERVER_API_URL + 'api/tariff-classes';
  private departmentResource = SERVER_API_URL + 'api/_search/departments';
  private resourceVariablePayoutsUrl = SERVER_API_URL + 'api/payouts/latest-variable-payouts';
  private resourceServiceItemBenefitTemplatesUrl = SERVER_API_URL + 'api/_search/service-item-benefit-templates';
  private resourcePlan = 'api/_search/plans';
  // VariablePayoutTemplate
  private resourceUrlPayoutTemplate = SERVER_API_URL + 'api/service-item-benefit-templates';
  private userOrgDeptSearchUrl = SERVER_API_URL + 'api/_search/user-organization-department-mappings';
  // Resource Url for service Item Benefits
  private serviceItemBenefitSearchUrl = SERVER_API_URL + 'api/_search/service-item-benefits';
  private serviceItemBenifitsSearchUrl = SERVER_API_URL + 'api/service-item-benefits-by-variablePayout';
  private serviceItemBenefitUrl = SERVER_API_URL + 'api/service-item-benefits';
  // Resource Url for service Item Exceptions
  private serviceItemExceptionSearchUrl = SERVER_API_URL + 'api/_search/service-item-exceptions';
  private serviceItemExceptionUrl = SERVER_API_URL + 'api/service-item-exceptions';
  // Resource Url for copy rules
  private variablePayoutCopyRules = SERVER_API_URL + 'api/variablePayout/copyRules';
  private variableTemplateCopyRules = SERVER_API_URL + 'api/template/copyRules';
  // Resource Url for Advance Search version list
  private versionListUrl = SERVER_API_URL + 'api/variable-payouts/distinct-version';
  // Resource Url for Vaiable Payout Template Unit export
  private unitExport = SERVER_API_URL + 'api/template/export/unit-mapping';

  constructor(private http: HttpClient) {}

  create(variablePayout: VariablePayout): Observable<EntityResponseType> {
    const copy: VariablePayout = Object.assign({}, variablePayout);
    return this.http.post<VariablePayout>(this.resourceUrl, copy, { observe: 'response' });
  }

  createVariablePayoutTemplate(variablePayoutTemplate: VariablePayoutTemplate): Observable<TemplateEntityResponseType> {
    const copy: VariablePayoutTemplate = Object.assign({}, variablePayoutTemplate);
    return this.http.post<VariablePayoutTemplate>(this.resourceUrlPayoutTemplate, copy, { observe: 'response' });
  }

  updateVariablePayoutTemplate(variablePayoutTemplate: VariablePayoutTemplate): Observable<TemplateEntityResponseType> {
    const copy: VariablePayoutTemplate = Object.assign({}, variablePayoutTemplate);
    return this.http.put<VariablePayoutTemplate>(this.resourceUrlPayoutTemplate, copy, { observe: 'response' });
  }

  findVariablePayoutTemplate(id: number): Observable<TemplateEntityResponseType> {
    return this.http.get<VariablePayoutTemplate>(`${this.resourceUrlPayoutTemplate}/${id}`, { observe: 'response' });
  }

  update(variablePayout: VariablePayout): Observable<EntityResponseType> {
    const copy: VariablePayout = Object.assign({}, variablePayout);
    return this.http.put<VariablePayout>(this.resourceUrl, copy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<VariablePayout>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<VariablePayout[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<VariablePayout[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEnum(enumName: string, req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(enumName, { params, observe: 'response' });
  }

  getValue(apiUrl, req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(apiUrl, { params, observe: 'response' });
  }

  getServiceItemExceptions(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.serviceItemExceptionsResource, { params, observe: 'response' });
  }

  getTariffClassList(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.getTariffClassListResource, { params, observe: 'response' });
  }

  getDepartment(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.departmentResource, { params, observe: 'response' });
  }

  getUserOrgDeptSearch(req?: any): Observable<HttpResponse<any>> {
    const params = createRequestOption(req);
    return this.http.get(this.userOrgDeptSearchUrl, { params, observe: 'response' });
  }

  getPayouts(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<VariablePayout[]>(this.resourceVariablePayoutsUrl, { params: options, observe: 'response' });
  }

  getResourceServiceItemBenefitTemplates(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ServiceItemBenefitTemplate[]>(this.resourceServiceItemBenefitTemplatesUrl, {
      params: options,
      observe: 'response'
    });
  }

  getPlan(req?: any): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<Plan[]>(this.resourcePlan, { params, observe: 'response' });
  }

  // API call for service Item Benefits
  getServiceItemBenefitsByPayoutId(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.serviceItemBenefitSearchUrl, { params, observe: 'response' });
  }
  getServiceItemBenifits(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.serviceItemBenifitsSearchUrl, { params, observe: 'response' });
  }
  searchServiceItemBenifits(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.serviceItemBenefitSearchUrl, { params, observe: 'response' });
  }
  saveServiceItemBenefits(serviceItemBenefits) {
    const copy: ServiceItemBenefit = Object.assign({}, serviceItemBenefits);
    return this.http.post<ServiceItemBenefit>(this.serviceItemBenefitUrl, copy, { observe: 'response' });
  }
  updateServiceItemBenefits(serviceItemBenefits) {
    const copy: ServiceItemBenefit = Object.assign({}, serviceItemBenefits);
    return this.http.put<ServiceItemBenefit>(this.serviceItemBenefitUrl, copy, { observe: 'response' });
  }
  deleteServiceItemBenefit(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.serviceItemBenefitUrl}/${id}`, { observe: 'response' });
  }
  // API call for service Item Exceptions
  getServiceItemExceptionsByPayoutId(req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(this.serviceItemExceptionSearchUrl, { params, observe: 'response' });
  }

  // API call for Variable Payout Copy Rules
  getVariablePayoutRulesCopy(variablePayoutTemplate: VariablePayoutTemplate, req: any): Observable<any> {
    const copy: VariablePayoutTemplate = Object.assign({}, variablePayoutTemplate);
    const options = createRequestOption(req);
    return this.http.post<any>(this.variablePayoutCopyRules, copy, { params: options, observe: 'response' });
  }

  // API call for Variable Template Copy Rules
  getVariableTemplateRulesCopy(serviceItemBenefits, req: any): Observable<any> {
    const copy: ServiceItemBenefit = Object.assign({}, serviceItemBenefits);
    const options = createRequestOption(req);
    return this.http.post<any>(this.variableTemplateCopyRules, copy, { params: options, observe: 'response' });
  }

  // API call for get version list for Adance Search
  getVariablePayoutVersionsList(req) {
    const params = createRequestOption(req);
    return this.http.get(this.versionListUrl, { params });
  }

  exportUnit(req?: any): Observable<EntityResponseType> {
    const params = createRequestOption(req);
    return this.http.get(this.unitExport, { params, observe: 'response' });
  }
}
