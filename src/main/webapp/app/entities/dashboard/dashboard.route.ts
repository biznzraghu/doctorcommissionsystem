import { Routes } from '@angular/router'; // Resolve,
import { DashboardComponent } from './dashboard.component';
// import { AccountService, PreferencesDataResolver } from 'athma-commons';
// import { Injectable } from '@angular/core';

export const dashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.dashboard.home.title',
      module: 'dashboard',
      menu: 'app-title'
    }
    // resolve: {
    //     preferences: PreferencesDataResolver,
    //     'auth': AuthenticationResolver,
    // }
  }
];
