import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router'; // , ActivatedRouteSnapshot,  RouterStateSnapshot
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Injectable({
  providedIn: 'root'
})
export class PreferencesDataResolver implements Resolve<any> {
  constructor(private stateStorageService: StateStorageService) {}
  resolve() {
    return this.stateStorageService.getValue('preferences');
  }
}

@Injectable({
  providedIn: 'root'
})
export class CurrentUserPreferencesDataResolver implements Resolve<any> {
  constructor(private preferenceService: PreferenceService) {}

  resolve() {
    // route: ActivatedRouteSnapshot, state: RouterStateSnapshot
    return this.preferenceService.currentUser();
  }
}
