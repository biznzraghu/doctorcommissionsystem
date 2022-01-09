import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared/util/request-util';
import { JhiAlertService } from 'ng-jhipster';

@Injectable({
  providedIn: 'root'
})
export class ArthaEnumHelperService {
  constructor(private http: HttpClient, private jhiAlertService: JhiAlertService) {}
  private getEnumFromServer(enumName: string, req?: any): Observable<any> {
    const params = createRequestOption(req);
    return this.http.get(enumName, { params, observe: 'response' });
  }

  public getEnum(enumName: string, req?: any) {
    return new Promise((resolve, reject) => {
      this.getEnumFromServer(enumName, req).subscribe(
        (res: any) => {
          const data = res.body;
          resolve(data);
        },
        (error: any) => {
          this.onError(error.json);
          reject(error);
        }
      );
    });
  }

  public getFormatedObject(dataArray, bindLabelName, bindValueName) {
    const keys = Object.keys(dataArray);
    let formatedData = [];
    if (keys && keys.length) {
      formatedData = keys.map(key => {
        return { [bindLabelName]: key, [bindValueName]: dataArray[key] };
      });
    }
    return formatedData;
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }
}
