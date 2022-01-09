// import { Injectable } from '@angular/core';
import { Route } from '@angular/router'; // , ActivatedRouteSnapshot, RouterStateSnapshot, Resolve
import { NavbarComponent } from './navbar.component';
// import { StateStorageService } from 'app/core/auth/state-storage.service';
import { PreferencesDataResolver } from 'app/entities/artha-common-services/preference-data-resolver.service';

// @Injectable()
// export class PreferencesDataResolver implements Resolve<any> {
//   constructor(
//     private stateStorageService: StateStorageService
//   ) { }

//   resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//     return this.stateStorageService.getValue('preferences');
//   }
// }

export const navbarRoute: Route = {
  path: '',
  component: NavbarComponent,
  resolve: {
    preferences: PreferencesDataResolver
  },
  outlet: 'navbar'
};
