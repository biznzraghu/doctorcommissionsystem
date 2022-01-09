import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { createRequestOption } from '../../shared/util/request-util';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  // private userUnitMappingUrl = '/api/_search/user-organization-mappings';
  private userUnitMappingUrl = '/api/_search/user-organization-department-mappings';
  private unitDeptMappingUrl = '/api/_search/departments';
  private unitOrganizationMappingUrl = '/api/_search/user-organization-mappings';

  private exportUrl = '/api/_export/variable-payout-breakup';
  private variableBreakupSummaryUrl = '/api/_export/variable-payout-breakup-summary';
  private departmentWiseRevenueUrl = 'api/export/department-revenue-summary';
  private departmentRevenueBreakUp = 'api/export/department-payout-breakup';
  private itemServiceWisePayoutBreakupUrl = 'api/_export/item-service-wise-payout-breakup';

  private refreshReportSubject = new Subject<boolean>();
  refreshSubjectAction$ = this.refreshReportSubject.asObservable();

  constructor(private http: HttpClient) {}

  getUnitByUser(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.userUnitMappingUrl, { params: option });
  }

  getDeptByUnit(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.unitDeptMappingUrl, { params: option });
  }

  getUserOrganizationMapping(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.userUnitMappingUrl, { params: option });
  }

  // generateReport(req, reportName): Observable<any> {
  //   const option = createRequestOption(req);
  //   let url;
  //   if (reportName === 'variable-payout-breakup') {
  //     url = this.exportUrl;
  //   } else if(reportName === 'variable-payout-breakup-summary'){
  //     url = this.variableBreakupSummaryUrl;
  //   } else if(reportName === 'department-wise-revenue') {
  //     url = this.departmentWiseRevenueUrl;
  //   } else if(reportName === 'department-revenue-break-up') {
  //     url = this.departmentRevenueBreakUp;
  //   } else if(reportName === 'item-service-wise-payout-breakup') {
  //     url = this.itemServiceWisePayoutBreakupUrl;
  //   }
  //   return this.http.get(url, { params: option, responseType: 'blob' });
  // }

  generateReport(req, reportName): Observable<any> {
    const option = createRequestOption(req);
    let url;
    if (reportName === 'variable-payout-breakup') {
      url = this.exportUrl;
    } else if (reportName === 'variable-payout-breakup-summary') {
      url = this.variableBreakupSummaryUrl;
    } else if (reportName === 'department-wise-revenue') {
      url = this.departmentWiseRevenueUrl;
    }
    return this.http.get(url, { params: option, responseType: 'blob' });
  }

  getDepartment(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.departmentRevenueBreakUp, { params: option });
  }

  getItemServiceWisePayoutBreakup(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.itemServiceWisePayoutBreakupUrl, { params: option });
  }

  getDepartmentWiseRevenue(req): Observable<any> {
    const option = createRequestOption(req);
    return this.http.get(this.departmentWiseRevenueUrl, { params: option });
  }

  refreshReport(val: boolean) {
    this.refreshReportSubject.next(val);
  }
}
