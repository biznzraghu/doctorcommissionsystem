import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { PayoutAdjustmentModel } from '../artha-models/payout-adjustment.model';

type EntityResponseType = HttpResponse<PayoutAdjustmentModel>;
type EntityArrayResponseType = HttpResponse<PayoutAdjustmentModel[]>;

@Injectable()
export class PayoutAdjustmentService {
  private resourceUrl = SERVER_API_URL + 'api/payout-adjustments';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/payout-adjustments';

  constructor(private http: HttpClient) {}
  create(payoutAdjustmentData: PayoutAdjustmentModel): Observable<PayoutAdjustmentModel> {
    return this.http.post(this.resourceUrl, payoutAdjustmentData);
  }

  update(payoutAdjustmentData: PayoutAdjustmentModel): Observable<PayoutAdjustmentModel> {
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

  search(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);
    return this.http.get<PayoutAdjustmentModel[]>(this.resourceSearchUrl, { params, observe: 'response' });
  }
}
