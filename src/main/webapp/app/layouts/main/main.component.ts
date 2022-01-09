import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';
// import { fromEvent, of, merge } from 'rxjs';
import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { Observable, fromEvent, merge, of } from 'rxjs';
import { mapTo } from 'rxjs/operators';
import { AnotherActiveUserErrorPopupComponent } from './other-user-active-error.modal';
import { NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
  public hideSideMenu: boolean;
  public hideMainMenu: boolean;
  public isConnected: Observable<boolean>;
  public isErrorPopupOpened: boolean;
  private uuidSessionStorageKey = 'uuid_session_storage_key';
  private uuidLocalStorageKey = 'uuid_local_storage_key';
  modalRef: NgbModalRef;
  constructor(
    private jhiLanguageHelper: JhiLanguageHelper,
    private router: Router,
    private modalService: NgbModal,
    private loginService: LoginService,
    private $storageService: StateStorageService,
    private principal: AccountService
  ) {
    this.isConnected = merge(
      of(navigator.onLine),
      fromEvent(window, 'online').pipe(mapTo(true)),
      fromEvent(window, 'offline').pipe(mapTo(false))
    );
    this.hideMainMenu = false;
    this.hideSideMenu = false;
    this.getLoginInfo();
  }
  private getLoginInfo() {
    this.principal.identity().subscribe(() => {});
  }
  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'arthaApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  ngOnInit() {
    let sessionUuid = this.$storageService.getValue(this.uuidSessionStorageKey);
    const localStorageUuid = this.$storageService.getValueFromLocalStorage(this.uuidLocalStorageKey);
    if (!sessionUuid) {
      sessionUuid = this.loginService.generateUUID();
      this.$storageService.setValue(this.uuidSessionStorageKey, sessionUuid);
    }
    if (localStorageUuid && sessionUuid !== localStorageUuid) {
      this.openErrorModal();
    }
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
        this.hideSideMenu = this.checkHideSidebarMenu(this.router.routerState.snapshot.root);
        this.hideMainMenu = this.checkHideMainMenu(this.router.routerState.snapshot.root);
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });
  }
  private checkHideSidebarMenu(routeSnapshot: ActivatedRouteSnapshot) {
    // console.log('-----------------------------', routeSnapshot);
    // if (routeSnapshot.firstChild) {
    //   routeSnapshot.firstChild.data['hideSideMenu'] || false;
    // }
    let hide: boolean = routeSnapshot.data && routeSnapshot.data['hideSideMenu'] ? routeSnapshot.data['hideSideMenu'] : false;
    if (routeSnapshot.firstChild) {
      hide = this.checkHideSidebarMenu(routeSnapshot.firstChild) || hide;
    }
    return hide;
  }

  private checkHideMainMenu(routeSnapshot: ActivatedRouteSnapshot) {
    // if (routeSnapshot.firstChild) {
    //   routeSnapshot.firstChild.data['hideMainMenu'] || false;
    // }
    let hide: boolean = routeSnapshot.data && routeSnapshot.data['hideMainMenu'] ? routeSnapshot.data['hideMainMenu'] : false;
    if (routeSnapshot.firstChild) {
      hide = this.checkHideMainMenu(routeSnapshot.firstChild) || hide;
    }
    return hide;
  }

  openErrorModal() {
    if (this.isErrorPopupOpened) {
      return;
    }
    this.isErrorPopupOpened = true;
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      windowClass: 'athma-modal-dialog vertical-middle-modal xs athma-warning-modal'
    };
    this.modalRef = this.modalService.open(AnotherActiveUserErrorPopupComponent, ngbModalOptions);
    this.modalRef.result.then(
      () => {},
      () => {
        this.isErrorPopupOpened = false;
        this.loginService.logout();
      }
    );
  }
}
