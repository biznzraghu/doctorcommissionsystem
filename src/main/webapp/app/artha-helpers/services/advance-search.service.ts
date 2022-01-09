import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class AdvanceSearchService {
  private resourceUrl = 'api/user-personalisations';
  private resourceSearchUrl = 'api/_search/user-personalisations';

  constructor(private http: HttpClient) {}

  create(userPersonalise: any) {
    return this.http.post(`${this.resourceUrl}`, userPersonalise, { observe: 'response' });
  }

  update(userPersonalise: any, req: any) {
    const params = createRequestOption(req);
    return this.http.put(this.resourceUrl, userPersonalise, { params, observe: 'response' });
  }

  query(req?: any): Observable<HttpResponse<any>> {
    const params = createRequestOption(req);
    return this.http.get(this.resourceUrl, { params, observe: 'response' }).pipe(
      map(res => {
        return res;
      })
    );
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<any[]> {
    const params = createRequestOption(req);
    return this.http
      .get<any[]>(`${this.resourceSearchUrl}`, { params, observe: 'response' })
      .pipe(
        map(res => {
          return res.body;
        })
      );
  }

  typeHeadSearch(searchUrl: string, req?: any, filterField?: any): Observable<any[]> {
    const params = createRequestOption(req);
    return this.http
      .get<any[]>(searchUrl, { params, observe: 'response' })
      .pipe(
        map(res => {
          let result = res.body;
          if (filterField) {
            result = res.body.map(x => x[filterField]);
          }
          return result;
        })
      );
  }
}
