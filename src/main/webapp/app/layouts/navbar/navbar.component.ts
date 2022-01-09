import { Component, OnInit, ViewChild, TemplateRef, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModalRef, NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { AccountService } from 'app/core/auth/account.service';
import { NavbarService } from './navbar.service';
import { LoginService } from 'app/core/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { AboutDialogComponent } from './about-dialog/about-dialog.component';
import { UnitChangeComponent } from './dialogs/unit-change-dialog/unit-change-dialog.component';
import { MainCommunicationService } from '../main/main-communication.service';
import { Preferences } from '../../entities/artha-models/preferences.model';
import { Observable } from 'rxjs/Observable';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  taskIcon = '../../../../content/images/navbar-images/task_icon.svg';
  userIcon = '../../../../content/images/navbar-images/user_icon.png'; // Demo User Icon
  isAppMenuVisible: boolean;
  hideMenu: boolean;
  inProduction: boolean;
  isNavbarCollapsed: boolean;
  languages: any[];
  swaggerEnabled: boolean;
  modalRef: NgbModalRef;
  version: string;
  hospital: any = {
    name: ''
  };
  headerView: TemplateRef<any>;
  isHeaderView: boolean;
  @ViewChild('defaultHeaderView', { static: false }) defaultHeaderView: TemplateRef<any>;
  headerViewSubscription$: any;
  userDetailObservable$: any;
  userIdentity: any;
  subscriptions;
  state$: Observable<any>;
  preferences: Preferences;
  createPrivileges = ' '; // empty string for ['ROLE_ADMIN', '000000000'];
  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private mainCommunicationService: MainCommunicationService,
    private ref: ChangeDetectorRef,
    // private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private modalService: NgbModal,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private navbarService: NavbarService,
    private stateSessionStorage: StateStorageService
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
    this.headerView = this.defaultHeaderView;
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
    this.languages = this.languageHelper.getAll();
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });

    this.getSessionStorageValue();
    this.headerView = this.defaultHeaderView;
    this.headerViewSubscription$ = this.mainCommunicationService.headerView$.subscribe(template => {
      // this.ref.detach();
      if (template) {
        this.headerView = template;
        this.ref.detectChanges();
      } else {
        this.headerView = this.defaultHeaderView;
        this.ref.detectChanges();
      }
    });
  }

  getSessionStorageValue() {
    const sessionData = this.stateSessionStorage.getValue('preferences');
    if (sessionData) {
      this.userIdentity = sessionData.user;
      this.hospital = sessionData.organization;
    }
  }

  fetchPreference() {
    this.subscriptions = this.navbarService.getPreference({ userId: this.userIdentity.id }).subscribe(
      (preference: Preferences) => {
        if (preference) {
          this.stateSessionStorage.setValue('preferences', preference);
          this.userIdentity = preference.user;
          this.hospital = preference.organization;
        }
      },
      () => {},
      () => {
        this.reloadRouter();
      }
    );
  }

  reloadRouter() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([this.router.url]);
  }

  changeLanguage(languageKey: string) {
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    // this.modalRef = this.loginModalService.open();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  logout() {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl() {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : null;
  }
  openHelp() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(AboutDialogComponent, ngbModalOptions);
    this.modalRef.result.then(
      () => {},
      () => {}
    );
  }
  openChangeUnitDialog() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(UnitChangeComponent, ngbModalOptions);
    this.modalRef.result.then(
      ok => {
        this.fetchPreference();
      },
      () => {}
    );
  }
  onClickedOutside(event) {
    event ? '' : '';
  }
  updateUserMenu(module) {
    this.mainCommunicationService.navigateToTopMenu(module);
  }
  openAbout() {
    this.openHelp();
  }
  navigateTo(pageName) {
    this.router.navigate([pageName]);
  }
  ngOnDestroy() {
    // this.headerViewSubscription$.unsubscribe();
  }
}
