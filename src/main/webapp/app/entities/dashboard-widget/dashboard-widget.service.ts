import { Injectable } from '@angular/core';
// import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DashboardWidget } from './dashboard-widget.model';
// import { ResponseWrapper, createRequestOption } from '../../shared';
import * as moment from 'moment';
// import { createRequestOption, PreferencesService, StateStorageService } from 'athma-commons';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { createRequestOption } from 'app/shared/util/request-util';
import { CurrentUserPreferencesDataResolver } from '../artha-common-services/preference-data-resolver.service';

@Injectable()
export class DashboardWidgetService {
  private resourceUrl = 'dashboard/api/dashboard-widgets';
  private resourceSearchUrl = 'dashboard/api/_search/dashboard-widgets';
  private doctorWiseRevenue = 'api/doctor-wise-revenue-trend';
  private departmentWiseRevenue = 'api/department-wise-revenue-trend';

  constructor(
    private http: HttpClient,
    private preferencesService: CurrentUserPreferencesDataResolver,
    private stateStorageService: StateStorageService
  ) {}
  public getPreferencesData() {
    return new Promise((resolve, reject) => {
      const preferences = this.stateStorageService.getValueFromSessionStorage('preferences');
      if (preferences && preferences.id && preferences.organization) {
        resolve(preferences);
      }
      // else {
      //     this.preferencesService.currentUser().subscribe((res: any) => {
      //         resolve(res);
      //     }, (err) => {
      //         reject(err);
      //     })
      // }
    });
  }

  getDoctorWiseTrend(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.doctorWiseRevenue, { params: options, observe: 'response' });
  }

  getDepartmentWiseTrend(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.departmentWiseRevenue, { params: options, observe: 'response' });
  }

  create(dashboardWidget: DashboardWidget): Observable<DashboardWidget> {
    const copy = this.convert(dashboardWidget);
    return this.http.post(this.resourceUrl, copy).map((res: HttpResponse<any>) => {
      return res.body;
    });
  }

  update(dashboardWidget: DashboardWidget): Observable<DashboardWidget> {
    const copy = this.convert(dashboardWidget);
    return this.http.put(this.resourceUrl, copy).map((res: HttpResponse<any>) => {
      return res.body;
    });
  }

  find(id: number): Observable<DashboardWidget> {
    return this.http.get(`${this.resourceUrl}/${id}`).map((res: HttpResponse<any>) => {
      return res.body;
    });
  }

  query(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  search(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  private convert(dashboardWidget: DashboardWidget): DashboardWidget {
    const copy: DashboardWidget = Object.assign({}, dashboardWidget);
    return copy;
  }

  setDateRange(dateString: string): Promise<any> {
    const dateRangeObj = { fromDate: '', toDate: '', selectedDateFilter: '' };
    if (dateString === 'Today') {
      dateRangeObj.fromDate = moment(new Date()).format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date()).format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'Yesterday') {
      dateRangeObj.fromDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'Last 7 Days') {
      dateRangeObj.fromDate = moment(new Date())
        .subtract(7, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'Last 30 Days') {
      dateRangeObj.fromDate = moment(new Date())
        .subtract(30, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'This Month') {
      dateRangeObj.fromDate = moment(new Date())
        .startOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'Current Month') {
      dateRangeObj.fromDate = moment(new Date())
        .startOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    } else if (dateString === 'Last Month') {
      dateRangeObj.fromDate = moment(new Date())
        .subtract(1, 'months')
        .startOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'months')
        .endOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.selectedDateFilter = dateString;
    }
    return Promise.resolve(dateRangeObj);
  }

  setDateRangeTrend(dateString): Promise<any> {
    const dateRangeObj = { fromDate: '', toDate: '', duration: '' };
    if (dateString === 'Last 7 Days') {
      dateRangeObj.duration = 'day';
      dateRangeObj.fromDate = moment()
        .subtract(7, 'd')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment()
        .subtract(1, 'd')
        .format('YYYY-MM-DD');
    } else if (dateString === 'Last 7 weeks') {
      dateRangeObj.duration = 'week';
      dateRangeObj.fromDate = moment()
        .startOf('w')
        .add(1, 'd')
        .subtract(7, 'w')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment()
        .startOf('w')
        .format('YYYY-MM-DD');
    } else if (dateString === 'Last 12 weeks') {
      dateRangeObj.duration = 'week';
      dateRangeObj.fromDate = moment()
        .startOf('w')
        .add(1, 'd')
        .subtract(12, 'w')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment()
        .startOf('w')
        .format('YYYY-MM-DD');
    } else if (dateString === 'Last 6 months') {
      dateRangeObj.duration = 'month';
      dateRangeObj.fromDate = moment(new Date())
        .subtract(6, 'months')
        .startOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'months')
        .endOf('month')
        .format('YYYY-MM-DD');
    } else if (dateString === 'Last 12 months') {
      dateRangeObj.duration = 'month';
      dateRangeObj.fromDate = moment(new Date())
        .subtract(12, 'months')
        .startOf('month')
        .format('YYYY-MM-DD');
      dateRangeObj.toDate = moment(new Date())
        .subtract(1, 'months')
        .endOf('month')
        .format('YYYY-MM-DD');
    }
    return Promise.resolve(dateRangeObj);
  }
}
