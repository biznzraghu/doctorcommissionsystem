import { Routes, Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { VariablePayoutListComponent } from './variable-payout-list/variable-payout-list.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { VariablePayoutDialogComponent } from './variable-payout-dialog.component';
import { VariablePayoutTemplateDialogComponent } from './variable-payout-template/variable-payout-template-dialog.component';
import { Injectable } from '@angular/core';
import { of, Observable } from 'rxjs';
import { VariablePayout } from './../artha-models/variable-payout.model';
import { VariablePayoutService } from './variable-payout.service';
import { VariablePayoutTemplate } from '../artha-models/variable-payout-template.model';
import { NavigationService } from 'app/artha-helpers/pageLeave/navigation.service';
import { VariablePayoutTemplateNameComponent } from './variable-payout-template/template-name-entry-dialog/variable-payout-template-name.component';
import { PreferencesDataResolver } from '../artha-common-services/preference-data-resolver.service';

@Injectable()
export class VariablePayoutDataResolver implements Resolve<any> {
  constructor(private variablePayoutService: VariablePayoutService) {}

  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      const id = route.params['id'];
      if (id) {
        return this.variablePayoutService.find(id);
      }
      return of(new VariablePayout());
    } else {
      return Observable.of(new VariablePayout());
    }
  }
}

@Injectable()
export class VariablePayoutTemplateDataResolver implements Resolve<any> {
  constructor(private variablePayoutService: VariablePayoutService) {}

  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      const id = route.params['id'];
      if (id) {
        return this.variablePayoutService.findVariablePayoutTemplate(id);
      }
      return of(new VariablePayoutTemplate());
    } else {
      return Observable.of(new VariablePayoutTemplate());
    }
  }
}

export const variablePayoutRoute: Routes = [
  {
    path: '',
    component: VariablePayoutListComponent,
    data: {
      authorities: ['103101', '103100', '103102'], // View, All, Modify
      pageTitle: 'arthaApp.variable-payout.home.title',
      module: 'variable-payout',
      menu: 'variable-payouts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'variable-payouts-new',
    component: VariablePayoutDialogComponent,
    data: {
      authorities: ['103100', '103102'],
      pageTitle: 'arthaApp.variable-payout.home.title',
      hideSideMenu: true
    },
    resolve: {
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  },
  {
    path: 'variable-payouts-update/:id',
    component: VariablePayoutDialogComponent,
    data: {
      authorities: ['103101', '103100', '103102'], // same route use for view details
      pageTitle: 'arthaApp.variable-payout.home.title',
      hideSideMenu: true
    },
    resolve: {
      preferences: PreferencesDataResolver,
      VariablePayoutData: VariablePayoutDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  },
  {
    path: 'variable-payouts-template',
    component: VariablePayoutTemplateDialogComponent,
    data: {
      authorities: ['103100', '103102'],
      pageTitle: 'arthaApp.variable-payout.home.variableTemplate',
      hideSideMenu: true
    },
    resolve: {
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  },
  {
    path: 'variable-payouts-template/:id/detail',
    component: VariablePayoutTemplateDialogComponent,
    data: {
      authorities: ['103101', '103100'],
      pageTitle: 'arthaApp.variable-payout.home.variableTemplate',
      hideSideMenu: true,
      pageType: 'Detail'
    },
    resolve: {
      preferences: PreferencesDataResolver,
      PayoutTemplateData: VariablePayoutTemplateDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  },
  {
    path: 'variable-payouts-template/:id/edit',
    component: VariablePayoutTemplateDialogComponent,
    data: {
      authorities: ['103100', '103102'],
      pageTitle: 'arthaApp.variable-payout.home.variableTemplate',
      hideSideMenu: true,
      pageType: 'Edit'
    },
    resolve: {
      preferences: PreferencesDataResolver,
      PayoutTemplateData: VariablePayoutTemplateDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  },
  {
    path: 'variable-payouts-template-popup',
    component: VariablePayoutTemplateNameComponent,
    data: {
      authorities: ['103100', '103102'],
      pageTitle: 'arthaApp.variable-payout.home.variableTemplate',
      hideSideMenu: true
    },
    resolve: {
      preferences: PreferencesDataResolver
    },
    canActivate: [UserRouteAccessService],
    canDeactivate: [NavigationService]
  }
];
