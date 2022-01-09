import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AccountService } from './../../../app/core/auth/account.service';

@Injectable()
export class Principal {
  private userIdentity: any;
  private authenticated = false;
  private authenticationState = new Subject<any>();

  constructor(private account: AccountService) {}

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[]): Promise<boolean> {
    return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
  }

  hasAnyAuthorityDirect(authorities: string[]): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return false;
    }

    for (let i = 0; i < authorities.length; i++) {
      if (this.userIdentity.authorities.indexOf(authorities[i]) !== -1) {
        return true;
      }
    }

    return false;
  }

  hasAnyChildAuthority(authorities: string[]): Promise<boolean> {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return Promise.resolve(false);
    }

    for (let i = 0; i < this.userIdentity.authorities.length; i++) {
      const authority = this.userIdentity.authorities[i];
      for (let j = 0; j < authorities.length; j++) {
        // console.log(authority+'      '+authorities[j]);
        if (authority.startsWith(authorities[j])) {
          return Promise.resolve(true);
        }
      }
    }

    return Promise.resolve(false);
  }

  hasAuthority(authority: string): Promise<boolean> {
    if (!this.authenticated) {
      return Promise.resolve(false);
    }

    return this.identity().then(
      id => {
        return Promise.resolve(id.authorities && id.authorities.indexOf(authority) !== -1);
      },
      () => {
        return Promise.resolve(false);
      }
    );
  }

  identity(force?: boolean): Promise<any> {
    if (force === true) {
      this.userIdentity = undefined;
    }

    // check and see if we have retrieved the userIdentity data from the server.
    // if we have, reuse it by immediately resolving
    if (this.userIdentity) {
      return Promise.resolve(this.userIdentity);
    }

    // retrieve the userIdentity data from the server, update the identity object, and then resolve.
    return this.account
      .fetch()
      .toPromise()
      .then(account => {
        if (account) {
          this.userIdentity = account;
          this.authenticated = true;
          // this.socketService.connect();
        } else {
          this.userIdentity = null;
          this.authenticated = false;
        }
        this.authenticationState.next(this.userIdentity);
        return this.userIdentity;
      })
      .catch(() => {
        this.userIdentity = null;
        this.authenticated = false;
        this.authenticationState.next(this.userIdentity);
        return null;
      });
  }

  checkPrivilege(privileges: string[]): Promise<boolean> {
    const authority = privileges[0];
    if (authority.length === 9) {
      privileges.push(authority.substr(0, 6) + '000');
      privileges.push(authority.substr(0, 3) + '000000');
    }
    privileges.push('000000000');
    return Promise.resolve(this.hasAnyAuthorityDirect(privileges));
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any> {
    return this.authenticationState.asObservable();
  }

  getImageUrl(): String {
    return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
  }
}
