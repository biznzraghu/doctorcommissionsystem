import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'administrator',
        loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)
      },
      {
        path: 'payout-adjustment',
        loadChildren: () => import('./payout-adjustment/payout-adjustment.module').then(m => m.PayoutAdjustmentModule)
      },
      {
        path: 'department-payout',
        loadChildren: () => import('./department-payout/department-payout.module').then(m => m.DepartmentPayoutModule)
      },
      {
        path: 'variable-payouts',
        loadChildren: () => import('./variable-payout/variable-payout.module').then(m => m.VariablePayoutModule)
      },
      {
        path: 'report',
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule)
      },
      {
        path: 'scheduler',
        loadChildren: () => import('./scheduler/scheduler.module').then(m => m.SchedulerModule)
      }
    ])
  ],
  exports: [RouterModule]
})
export class EntityRoutingModule {}
