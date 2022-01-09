import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { PreferencesDataResolver } from 'app/entities/artha-common-services/preference-data-resolver.service';
import { ReportComponent } from '../../report.component';

export const misReportRoute: Routes = [
  {
    path: 'item-service-wise-payout-breakup',
    component: ReportComponent,
    resolve: {
      preference: PreferencesDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.report.home.title',
      misReport: true,
      reportName: 'item-service-wise-payout-breakup'
    },
    canActivate: [UserRouteAccessService]
  }
];
