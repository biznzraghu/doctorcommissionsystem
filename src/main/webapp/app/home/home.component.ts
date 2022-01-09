import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { FormBuilder, Validators } from '@angular/forms';
import { LoginService } from 'app/core/login/login.service';
import { Router } from '@angular/router';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { Observable } from 'rxjs/Observable';
import { NavbarService } from '../layouts/navbar/navbar.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('username') private _usernameEle: ElementRef;
  account: Account;
  authSubscription: Subscription;
  modalRef: NgbModalRef;
  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });
  resetPwdForm = this.fb.group({
    oldPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]]
  });
  authenticationError: boolean;
  public tempCanReset = false;
  public newPwdText = false;
  public oldPwdText = false;
  public confirmPwdText = false;
  public isPasswordMatch = false;
  private uuidSessionStorageKey = 'uuid_session_storage_key';
  private uuidLocalStorageKey = 'uuid_local_storage_key';
  private userDetailObservable$: Observable<any>;
  private subscriptions: Subscription;
  userIdentity: any;
  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private eventManager: JhiEventManager,
    private mainCommunicationService: MainCommunicationService,
    private router: Router,
    private fb: FormBuilder,
    private navbarService: NavbarService
  ) {}

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
    if (this.stateStorageService.getValueFromLocalStorage('login_user')) {
      this.accountService.identity();
    }
    this.accountService.identity().subscribe(account => {
      this.account = account;
      // this.router.navigate(['/artha/dashboard']);
    });
    this.registerAuthenticationSuccess();
    this.mainCommunicationService.updateHeaderView(null);
    this.onResetFormChanges();
  }

  ngAfterViewInit() {
    // this._usernameEle.nativeElement.focus();
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
    // return false;
  }

  login() {
    this.loginService
      .login({
        username: this.loginForm.get('username').value,
        password: this.loginForm.get('password').value,
        rememberMe: this.loginForm.get('rememberMe').value
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          this.stateStorageService.clearFromSessionStorage('preferences');
          this.stateStorageService.setValueToLocalStorage('login_user', this.loginForm.get('username').value);
          let sessionStorageUuid = this.stateStorageService.getValue(this.uuidSessionStorageKey);
          if (!sessionStorageUuid) {
            sessionStorageUuid = this.loginService.generateUUID();
          }
          this.stateStorageService.setValueToLocalStorage(this.uuidLocalStorageKey, sessionStorageUuid);
          this.stateStorageService.setValue(this.uuidSessionStorageKey, sessionStorageUuid);
          this.setPreference().then(value => {
            if (value) {
              this.router.routeReuseStrategy.shouldReuseRoute = () => false;
              this.router.onSameUrlNavigation = 'reload';
              this.router.navigateByUrl('/dashboard');
              this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
              });
            }
          });
        },
        () => {
          this.authenticationError = true;
        }
      );
  }

  requestResetPassword() {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { oldPassword, newPassword, confirmPassword } = this.resetPwdForm.value;
    if (newPassword !== confirmPassword) {
      alert('Password mismatch');
      return;
    }
  }
  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }

  get newPassword() {
    return this.resetPwdForm.get('newPassword');
  }

  get confirmPassword() {
    return this.resetPwdForm.get('confirmPassword');
  }

  onResetFormChanges(): void {
    this.resetPwdForm.get('confirmPassword').valueChanges.subscribe(val => {
      this.isPasswordMatch = val === this.newPassword.value;
    });
  }

  setPreference() {
    const promise = new Promise((resolve, reject) => {
      this.userDetailObservable$ = this.accountService.getAuthenticationState();
      if (this.userDetailObservable$) {
        this.userDetailObservable$.subscribe((res: any) => {
          this.userIdentity = res;
        });
        if (!this.userIdentity) {
          this.accountService.identity().subscribe((res: any) => {
            this.userIdentity = res;
          });
        }
      }

      this.subscriptions = this.navbarService.getPreference({ userId: this.userIdentity.id }).subscribe(
        (preference: any) => {
          if (preference) {
            this.stateStorageService.setValue('preferences', preference);
            resolve(true);
          }
        },
        error => {
          reject();
        }
      );
    });

    return promise;
  }
}
