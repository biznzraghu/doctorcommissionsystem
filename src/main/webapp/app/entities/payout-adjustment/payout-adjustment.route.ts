import { Routes, Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { PayoutAdjustmentListComponent } from './payout-adjustment-list/payout-adjustment-list.component';
import { NewPayoutAdjustmentComponent } from './new-payout-adjustment/new-payout-adjustment.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { DetailPayoutAdjustmentComponent } from './detail-payout-adjustment/detail-payout-adjustment.component';
import { Injectable } from '@angular/core';
import { PayoutAdjustmentModel } from '../artha-models/payout-adjustment.model';
import { of, Observable } from 'rxjs';
import { PayoutAdjustmentService } from './payout-adjustment.service';
// import { NavigationService } from 'app/artha-helpers/pageLeave/navigation.service';

@Injectable()
export class PayoutAdjustmentDataResolver implements Resolve<any> {
  constructor(private payoutAdjustmentService: PayoutAdjustmentService) {}

  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      const id = route.params['id'];
      if (id) {
        return this.payoutAdjustmentService.find(id);
      }
      return of(new PayoutAdjustmentModel());
    } else {
      return Observable.of(new PayoutAdjustmentModel());
    }
  }
}
export const payoutAdjustmentRoute: Routes = [
  {
    path: '',
    component: PayoutAdjustmentListComponent,
    data: {
      authorities: ['101101', '101100', '101102'], // View, All, Modify
      pageTitle: 'arthaApp.payoutAdjustment.home.title',
      module: 'payout-adjustment',
      menu: 'artha/payout-adjustment'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NewPayoutAdjustmentComponent,
    data: {
      authorities: ['101100', '101102'],
      pageTitle: 'arthaApp.payoutAdjustment.home.title',
      hideSideMenu: true
    },
    canActivate: [UserRouteAccessService]
    // canDeactivate: [NavigationService]
  },
  {
    path: 'detail/:id',
    component: DetailPayoutAdjustmentComponent,
    data: {
      authorities: ['101101', '101100'],
      pageTitle: 'arthaApp.payoutAdjustment.home.title',
      hideSideMenu: true
    },
    resolve: {
      payoutAdjustmentData: PayoutAdjustmentDataResolver
    },
    canActivate: [UserRouteAccessService]
  }
];
