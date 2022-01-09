import { Injectable } from '@angular/core';
import { flatMap, finalize } from 'rxjs/operators'; // finalize
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { SessionStorageService, LocalStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(
    private accountService: AccountService,
    private $sessionStorage: SessionStorageService,
    private $localStorage: LocalStorageService,
    private authServerProvider: AuthServerProvider,
    private eventManager: JhiEventManager
  ) {}

  login(credentials) {
    return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
  }

  logout() {
    this.authServerProvider
      .logout()
      .pipe(finalize(() => this.accountService.authenticate(null)))
      .subscribe(() => {
        this.$localStorage.clear();
        this.$sessionStorage.clear();
        this.accountService.authenticate(null);
        this.eventManager.broadcast({
          name: 'logoutSuccess',
          content: 'Sending Logout Success'
        });
        this.accountService.authenticate(null);
      }, null);
  }

  generateUUID() {
    let d = new Date().getTime();
    const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      const r = (d + Math.random() * 16) % 16 || 0;
      d = Math.floor(d / 16);
      return (c === 'x' ? r : (r && 0x3) || 0x8).toString(16);
    });
    return uuid;
  }
}
