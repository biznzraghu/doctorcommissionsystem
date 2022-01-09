import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from '../../shared/util/request-util';

@Injectable()
export class NavbarService {

    private getPreferenceUrl = 'api/preference'
    private preferenceUrl = '/api/change-preference';
    private unitUrl = '/api/organizations';
    private userUnitMappingUrl = '/api/_search/user-organization-department-mappings';


    constructor(private http: HttpClient) { }

    getAllUnits(req): Observable<any> {
        const option = createRequestOption(req);
        return this.http.get(this.unitUrl, { params: option });
    }

    getUnitByUser(req): Observable<any>  {
        const option = createRequestOption(req);
        return this.http.get(this.userUnitMappingUrl, { params: option });      
    }

    getPreference(req) {
        const option = createRequestOption(req);
        return this.http.get(this.getPreferenceUrl, { params: option });
    }

    savePreferenceUnit(unitId: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.preferenceUrl}/${unitId}`, { observe: 'response' });
    }

}
