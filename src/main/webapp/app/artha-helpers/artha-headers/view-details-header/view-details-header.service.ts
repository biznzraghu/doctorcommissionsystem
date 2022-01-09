import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class ViewDetailsHeaderService {
  constructor(private http: HttpClient) {}

  search(resourceSearchUrl: string, req?: any): Observable<HttpResponse<any[]>> {
    const params = createRequestOption(req);
    return this.http.get<any[]>(resourceSearchUrl, { params, observe: 'response' });
  }
}
