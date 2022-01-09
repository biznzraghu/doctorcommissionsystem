import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Routes } from '@angular/router';
// import { StateStorageService } from 'app/core/auth/state-storage.service';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { DepartmentPayout } from 'app/entities/department-payout/department-payout.model';
import { of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PreferencesDataResolver } from '../artha-common-services/preference-data-resolver.service';
import { DepartmentPayoutMainComponent } from './department-payout-main.component';
import { DepartmentPayoutComponent } from './department-payout.component';
import { DepartmentPayoutService } from './department-payout.service';
import { DepartmentPayoutDetailMainComponent } from './view-details/department-payout-detail-main.component';

@Injectable()
export class DepartmentPayoutDataResolver implements Resolve<any> {
  constructor(private departmentPayoutService: DepartmentPayoutService) {}

  resolve(route: ActivatedRouteSnapshot) {
    const id = route.params['id'];
    if (id) {
      return this.departmentPayoutService.find(id).pipe(
        flatMap((res: HttpResponse<DepartmentPayout>) => {
          if (res.body) {
            return of(res.body);
          } else {
            return of(new DepartmentPayout());
          }
        })
      );
    }
    return of(new DepartmentPayout());
  }
}
// @Injectable()
// export class PreferencesDataResolver implements Resolve<any> {
//   constructor(private stateStorageService: StateStorageService) {}
//   resolve() {
//     return this.stateStorageService.getValue('preferences');
//   }
// }
export const DepartmentPayoutRoute: Routes = [
  {
    path: '',
    component: DepartmentPayoutComponent,
    data: {
      authorities: ['102101', '102100', '102102'], // View, All, Modify
      pageTitle: 'arthaApp.department-payout.home.title',
      module: 'department-payout',
      menu: 'artha/department-payout'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'department-payout-new',
    component: DepartmentPayoutMainComponent,
    data: {
      authorities: ['102100', '102102'],
      pageTitle: 'arthaApp.department-payout.home.title',
      hideSideMenu: true
    },
    resolve: {
      departmentPayout: DepartmentPayoutDataResolver,
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'department-payout-edit',
    component: DepartmentPayoutMainComponent,
    data: {
      authorities: ['102101', '102100', '102102'],
      pageTitle: 'arthaApp.department-payout.home.title',
      hideSideMenu: true
    },
    resolve: {
      departmentPayout: DepartmentPayoutDataResolver,
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'department-payout-view',
    component: DepartmentPayoutDetailMainComponent,
    data: {
      authorities: ['102101', '102100', '102102'],
      pageTitle: 'arthaApp.department-payout.home.title',
      hideSideMenu: true
    },
    resolve: {
      departmentPayout: DepartmentPayoutDataResolver,
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService]
  }
];
