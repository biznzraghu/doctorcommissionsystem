import { Routes } from '@angular/router';
import { SchedulerComponent } from './scheduler.component';

export const SchedulerRoute: Routes = [
  {
    path: '',
    component: SchedulerComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'gatewayApp.scheduler.home.title'
    }
  }
];
