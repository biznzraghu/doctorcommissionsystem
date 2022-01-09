import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Scheduler } from './scheduler.model';

type EntityResponseType = HttpResponse<Scheduler>;
type EntityArrayResponseType = HttpResponse<Scheduler[]>;

@Injectable()
export class SchedulerService {
  private resourceUrl = '/api/schedulers';

  constructor(private http: HttpClient) {}

  update(scheduler: Scheduler): Observable<Scheduler> {
    const copy: Scheduler = Object.assign({}, scheduler);
    return this.http.put(`${this.resourceUrl}`, copy);
  }

  query(): Observable<EntityArrayResponseType> {
    return this.http.get<Scheduler[]>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
