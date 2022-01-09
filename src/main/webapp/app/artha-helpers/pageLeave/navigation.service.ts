import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { Observable, of } from 'rxjs';
import 'rxjs/add/operator/delay';
import { fromPromise } from 'rxjs/internal/observable/fromPromise';
// import { StateStorageService } from './../../core/auth/state-storage.service';
import { DialogService } from './dialog.service';
import { map, flatMap } from 'rxjs/operators';

export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}
@Injectable()
export class NavigationService implements CanDeactivate<CanComponentDeactivate> {
  constructor(private dialogService: DialogService) {} // , private $storageService: StateStorageService

  canDeactivate(component: CanComponentDeactivate) {
    if (!component.canDeactivate) {
      return new Promise<boolean>(resolve => {
        return resolve(true);
      });
    }
    const retValue = component.canDeactivate();
    // let retValue = component.canDeactivate();
    // const isLoggedOut = this.$storageService.getValue('idealTimeOutOn');
    // if (isLoggedOut === null || isLoggedOut === undefined) {
    //     retValue = true;
    // }
    if (retValue instanceof Observable) {
      return this.intercept(retValue);
    } else {
      return retValue;
    }
  }
  private intercept(observable: Observable<any>): Observable<any> {
    return observable.pipe(
      map(res => {
        return res;
      }),
      flatMap(res => {
        if (res === false) {
          const modalPromise = this.dialogService.confirm();
          const newObservable = fromPromise(modalPromise);
          newObservable.subscribe(() => {
            // respose
            // if (respose === true) {
            // } else {
            // }
          });
          return newObservable;
        } else {
          return of(res);
        }
      })
    );
  }
}
