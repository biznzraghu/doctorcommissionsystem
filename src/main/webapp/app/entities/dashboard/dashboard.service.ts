import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Subject } from 'rxjs/Subject';
import { Dashboard } from './dashboard.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { AccountService } from 'app/core/auth/account.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { CurrentUserPreferencesDataResolver } from '../artha-common-services/preference-data-resolver.service';

@Injectable({ providedIn: 'root' })
export class AuthenticationResolver {
  constructor(private account: AccountService) {}

  resolve() {
    return new Promise(resolve => {
      const accountInfo: any = this.account;
      resolve({ auth: accountInfo });
    });
  }
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private resourceTabUrl = 'api/_search/user-dashboards';
  private resourceWidgetSearchUrl = 'api/_search/user-dashboard-widgets';
  private resourceWidgetUrl = 'api/user-dashboard-widgets';
  private resourceWidgetAllUrl = 'api/user-dashboard-widgets/all';
  private resourceDashboardAllUrl = 'api/user-dashboards/all';
  private resourceDashboardUrl = 'api/user-dashboards';
  private resourceUrl = 'api/dashboards';
  private resourceSearchUrl = 'api/_search/dashboards';
  private allWidgetSearch = 'api/_search/widget-masters';

  constructor(
    private http: HttpClient,
    private preferencesService: CurrentUserPreferencesDataResolver,
    private stateStorageService: StateStorageService
  ) {}

  getAllTabs(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceTabUrl, { params: options, observe: 'response' });
  }
  getAllWidgets(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.allWidgetSearch, { params: options, observe: 'response' });
  }

  getWidgets(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceWidgetSearchUrl, { params: options, observe: 'response' });
  }

  createNewDashboard(data: any): Observable<any> {
    return this.http.post(this.resourceDashboardUrl, data);
  }

  editDashboard(data: any): Observable<any> {
    return this.http.put(this.resourceDashboardUrl, data);
  }

  deleteDashboard(data: any): Observable<any> {
    return this.http.delete(this.resourceDashboardUrl + '/' + data.id);
  }

  saveWidgets(data: any): Observable<any> {
    return this.http.put(this.resourceWidgetUrl, data);
  }

  saveAllWidgets(data: any): Observable<any> {
    return this.http.post(this.resourceWidgetAllUrl, data);
  }
  saveAllDashboards(data: any): Observable<any> {
    return this.http.post(this.resourceDashboardAllUrl, data);
  }

  createWidgets(data: any): Observable<any> {
    return this.http.post(this.resourceWidgetUrl, data);
  }

  deleteWidgets(id: any): Observable<any> {
    return this.http.delete(this.resourceWidgetUrl + '/' + id);
  }

  create(dashboard: Dashboard): Observable<Dashboard> {
    const copy = this.convert(dashboard);
    return this.http.post(this.resourceUrl, copy).map((res: HttpResponse<any>) => {
      return res.body;
    });
  }

  update(dashboard: Dashboard): Observable<Dashboard> {
    const copy = this.convert(dashboard);
    return this.http.put(this.resourceUrl, copy).map((res: HttpResponse<any>) => {
      return res.body;
    });
  }

  find(id: number): Observable<Dashboard> {
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

  public getPreferencesData() {
    return new Promise((resolve, reject) => {
      const preferences = this.stateStorageService.getValueFromSessionStorage('preferences');
      if (preferences && preferences.id && preferences.organization) {
        resolve(preferences);
      } else {
        // this.preferencesService.currentUser().subscribe((res: any) => {
        //     resolve(res);
        // }, (err) => {
        //     reject(err);
        // })
      }
    });
  }

  private convert(dashboard: Dashboard): Dashboard {
    const copy: Dashboard = Object.assign({}, dashboard);
    return copy;
  }
}

@Injectable({
  providedIn: 'root'
})
export class UpdateWidget {
  private subject = new Subject<any>();

  setChengedWidget(widget: any) {
    this.subject.next(widget);
  }

  getChengedWidget(): Observable<any> {
    return this.subject.asObservable();
  }
}
