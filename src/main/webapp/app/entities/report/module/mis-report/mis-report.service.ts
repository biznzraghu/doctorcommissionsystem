import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';
import { MisReport } from './mis-report.model';

@Injectable()
export class MisReportService {
  private resourceUrl = '/mis/api/mis-reports';
  private resourceUrlByType = '/mis/api/_by-type/mis-reports';
  private resourceSearchUrl = '/mis/api/_search/mis-reports';
  private resourceConfigurationUrl = '/mis/api/_by-feature/mis-configurations';

  //
  private misResourceUrl = '/api/mis-report';
  private misRequestUrl = '/api/_schedule';
  private resourceDownloadUrl = '/api/download/mis-report';

  constructor(private http: HttpClient, private httpBlockerService: HttpBlockerService) {}

  create(misReport: MisReport, filterCriteria: any, reportName: string): Observable<any> {
    // this.httpBlockerService.enableHttpBlocker(true);
    const options = createRequestOption(filterCriteria);
    return this.http.post(`${this.misRequestUrl}/${reportName}`, misReport, { params: options, observe: 'response' });
  }

  update(misReport: MisReport): Observable<any> {
    return this.http.put(this.resourceUrl, misReport, { observe: 'response' });
  }

  find(id: number): Observable<any> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByType(type: string): Observable<any> {
    // this.httpBlockerService.enableHttpBlocker(true);
    return this.http.get(`${this.resourceUrlByType}/${type}`, { observe: 'response' });
  }

  query(req?: any): Observable<HttpResponse<MisReport>> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl, { params: options, observe: 'response' });
  }

  downloadReport(reportId: number): Observable<any> {
    // return this.http.get(this.resourceDownloadUrl+'/'+reportId, {observe: 'response', responseType: format });
    return this.http.get(this.resourceDownloadUrl + '/' + reportId);
    /**
     * const option = createRequestOption(req);
    return this.http.get(this.itemServiceWisePayoutBreakupUrl, { params: option });
     */
  }

  search(req?: any): Observable<HttpResponse<MisReport>> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getConfiguration(featureCode: string): Observable<any> {
    return this.http.get(`${this.resourceConfigurationUrl}/${featureCode}`, { observe: 'response' });
  }

  fetchMisReportByName(req?: any) {
    const options = createRequestOption(req);
    return this.http.get(this.misResourceUrl, { params: options, observe: 'response' });
  }
}
