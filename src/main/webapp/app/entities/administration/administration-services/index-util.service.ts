import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpClient } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';

@Injectable({
  providedIn: 'root'
})
export class IndexUtilService {
  private resourceUrl = [
    ['api/_index', 'api/index'],
    ['api/_index', 'api/_index', 'api/_index'],
    ['api/_index', 'api/index'],
    ['api/_index', 'api/index']
  ];

  constructor(private http: HttpClient) {}

  query(indexName?: string, req?: any, tabIndex?: any, subTab?: any, isContain_index?: boolean): Observable<any> {
    const params = createRequestOption(req);
    if (tabIndex === 3 && isContain_index === true) {
      subTab = 0;
    } else {
      if (tabIndex === 3 && isContain_index === false) {
        subTab = 1;
      }
    }
    return this.http.get(this.resourceUrl[tabIndex][subTab] + '/' + indexName, { params });
  }
}
