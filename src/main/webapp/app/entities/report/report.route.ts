import { Routes } from '@angular/router'; // , Resolve, ActivatedRouteSnapshot, RouterStateSnapshot
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
// import { Injectable } from '@angular/core';
// import { of, Observable } from 'rxjs';
import { ReportComponent } from './report.component';
// import { PreferenceService } from '../../artha-helpers/services/preference.service';
import { CurrentUserPreferencesDataResolver } from '../artha-common-services/preference-data-resolver.service';

// @Injectable()
// export class PreferencesDataResolver implements Resolve<any> {
//   constructor(
//     private preferenceService: PreferenceService
//   ) { }

//   resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//     return this.preferenceService.currentUser();
//   }
// }

export const ReportRoute: Routes = [
  {
    path: 'variable-payout-breakup',
    component: ReportComponent,
    resolve: {
      preference: CurrentUserPreferencesDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'variable-payout-breakup-summary',
    component: ReportComponent,
    resolve: {
      preference: CurrentUserPreferencesDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'department-wise-revenue',
    component: ReportComponent,
    resolve: {
      preference: CurrentUserPreferencesDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'department-revenue-break-up',
    component: ReportComponent,
    resolve: {
      preference: CurrentUserPreferencesDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
  // {
  //   path: 'item-service-wise-payout-breakup',
  //   component: ReportComponent,
  //   resolve: {
  //     preference: CurrentUserPreferencesDataResolver
  //   },
  //   data: {
  //     authorities: ['ROLE_USER', 'ROLE_ADMIN'],
  //     pageTitle: 'arthaApp.variable-payout.home.title'
  //   },
  //   canActivate: [UserRouteAccessService]
  // }
];
