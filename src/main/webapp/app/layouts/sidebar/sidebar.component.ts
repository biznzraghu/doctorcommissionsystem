import { Component, OnInit, OnDestroy, ViewChild, ChangeDetectorRef } from '@angular/core';
import { PerfectScrollbarConfigInterface, PerfectScrollbarComponent, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { AccountService } from 'app/core/auth/account.service';
import { NavigationEnd, Router } from '@angular/router';
import { SidebarService } from './sidebar.service';
import { MainCommunicationService } from '../main/main-communication.service';
@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, OnDestroy {
  modifiedMenuItems: any[];
  selectedModule: string;
  sidebarExpand: boolean;
  totalContentHeight: number;
  itemsInSidebar: number;
  authSubscription: any;
  isRefresh: boolean;
  selectedFeatureType: string;
  selectedMenu: string;
  navSubscription: any;
  submenuStringLength: number;
  submenuStringLengthChoices: any = {
    high: 21,
    low: 11
  };

  hideMenu: boolean;

  isMenuSearchOpened: boolean;
  isPatientSearchOpened: boolean;
  isItemSearchOpened: boolean;
  userMenus: Array<any> = [];
  headerViewSubscription$;

  imagePath = {
    administration: 'athma-administration-module-icon',
    'variable-payout': 'athma-menu-variable-icon',
    'department-payout': 'athma-menu-department-icon',
    'payout-adjustment': 'athma-menu-payout-icon',
    'report': 'athma-menu-report-icon',
    Ambulatory: 'athma-ambulatory-module-icon',
    EHR: 'athma-ehr-module-icon',
    ADT: 'athma-adt-module-icon',
    OTM: 'athma-ot-module-icon',
    Billing: 'athma-billing-module-icon',
    reports: 'athma-menu-report-icon',
    'Blood Bank': 'athma-blood-bank-module-icon'
  };

  public config: PerfectScrollbarConfigInterface = {};

  @ViewChild(PerfectScrollbarComponent, { static: false }) componentScroll: PerfectScrollbarComponent;
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  userDetails: any = {
    id: null
  };

  constructor(
    private accountService: AccountService,
    private router: Router,
    private sideBarService: SidebarService,
    private mainCommunicationService: MainCommunicationService,
    private ref: ChangeDetectorRef
  ) {
    // this.loadMenu();
  }

  ngOnInit() {
    this.isMenuSearchOpened = false;
    this.isPatientSearchOpened = false;
    this.isItemSearchOpened = false;
    this.sidebarExpand = false;
    this.submenuStringLength = this.submenuStringLengthChoices.low;
    this.isRefresh = false;
    this.selectedModule = 'billing';
    this.selectedMenu = 'amb-refund';
    this.selectedFeatureType = '';
    this.totalContentHeight = 0;
    // this.loadMenu();
    this.navSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (this.router.routerState.snapshot.root && this.router.routerState.snapshot.root.firstChild) {
          this.hideMenu = this.router.routerState.snapshot.root.firstChild.data.hideMenu || false;
          this.selectedModule = this.router.routerState.snapshot.root.firstChild.data.module;
          this.selectedMenu = this.router.routerState.snapshot.root.firstChild.data.menu;
          this.setSelectedModuleOnLoad();
        } else {
          this.hideMenu = false;
        }
      }
    });
    // if (this.isAuthenticated() && this.userMenus.length > 0 && this.router.url) {
    //   this.loadMenu();
    //   this.setSelectedModuleOnLoad();
    // }
    // --------------------------------------------------------------------------- //
    // if (this.isAuthenticated()) {
    //   this.loadMenu();
    //   this.setSelectedModuleOnLoad(); // working
    // }
    // =========================================================================== //
    // if(this.accountService.isAuthenticated()) {
    //   this.sideBarService.load().subscribe((result: any) => {
    //     this.userMenus = result;
    //     this.setSelectedModuleOnLoad();
    //   });
    // }

    this.accountService.getAuthenticationState().subscribe(res => {
      // // eslint-disable-next-line no-console
      // console.clear();
      // // eslint-disable-next-line no-console
      // console.log('getAuthenticationState ' , res);
      if (res !== null) {
        this.sideBarService.load().subscribe((result: any) => {
          this.userMenus = result;

          // // eslint-disable-next-line no-console
          // console.log('if res userMenus ' , this.userMenus);
          this.setSelectedModuleOnLoad();
        });
        // this.loadMenu();
        // this.setSelectedModuleOnLoad();
      } else {
        this.userMenus = [];
      }
    });

    this.headerViewSubscription$ = this.mainCommunicationService.headerView$.subscribe(template => {
      // this.ref.detach();
      if (template) {
        // this.headerView = template;
        this.ref.detectChanges();
      } else {
        // this.headerView = this.defaultHeaderView;
        this.ref.detectChanges();
      }
    });
  }
  ngOnDestroy() {
    // this.authSubscription.unsubscribe();
    // this.navSubscription.unsubscribe();
    this.headerViewSubscription$.unsubscribe();
  }

  loadMenu() {
    this.sideBarService.load().subscribe(result => {
      this.userMenus = result;
    });
  }

  isCurrentUrl(menu) {
    if (menu === this.selectedMenu) {
      return true;
    } else {
      return false;
    }
  }
  private setSelectedModuleOnLoad() {
    for (let i = 0; i < this.userMenus.length; i++) {
      for (let j = 0; j < this.userMenus[i].childs.length; j++) {
        for (let k = 0; k < this.userMenus[i].childs[j].childs.length; k++) {
          if ('/' + this.userMenus[i].childs[j].childs[k].src === this.router.url) {
            this.selectedMenu = this.userMenus[i].childs[j].childs[k].src;
            this.selectedFeatureType = this.userMenus[i].childs[j].src;
            this.selectedModule = this.userMenus[i].src;
            this.onPageRefresh(this.userMenus[i].src);
            return;
          }
        }
      }
    }
  }

  private onPageRefresh(selectedModule: string) {
    this.routerSelectionManipulation(selectedModule, false);
  }

  private routerSelectionManipulation(selectedModule: string, isFromSubcribe: boolean) {
    // console.log('userMenus:', this.userMenus);
  }

  alignActiveMenu(e: Event, selectedModule: string, index: number, featureType: string) {
    e ? ' ' : ' ';
    const selectedIndex = index + this.itemsInSidebar;
    for (let i = 0; i < this.userMenus.length; i++) {
      if (selectedModule === this.userMenus[i].src) {
        const selectedFeatureIndex = this.userMenus[i].childs.findIndex(function(element) {
          return element.title === featureType;
        });
        // Removing item from position
        const clickedObj = this.userMenus[i].childs[selectedFeatureIndex].childs.splice(selectedIndex, 1);
        // Adding last poistion of visible area
        this.userMenus[i].childs[selectedFeatureIndex].childs.splice(this.itemsInSidebar - 1, 0, clickedObj[0]);
      }
    }
    this.hideMoreItems();
  }

  itemWasToggled(event) {
    event ? ' ' : ' ';
    this.hideMoreItems();
  }

  hideMoreItems() {
    const moreItems = document.getElementsByClassName('more-items');
    if (moreItems.length > 0) {
      for (let i = 0; i < moreItems.length; i++) {
        moreItems[i].classList.add('d-none');
      }
    }
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }
  public enableInnerMenu(event) {
    if (event.currentTarget) {
      event.currentTarget.classList.toggle('open');
    }
  }

  public disableInnerMenu(event) {
    if (event.currentTarget) {
      event.currentTarget.parentElement.parentElement.firstElementChild.classList.toggle('open');
    }
  }
  mainMenuOverHandler(event) {
    event ? ' ' : ' ';
    const openedElement = document.querySelector('div.nontransaction-category.open');
    if (openedElement) {
      openedElement.classList.remove('open');
    }
  }
  navigateToModule(urlLink, moduleSrc, subModuleSrc?) {
    if (this.selectedMenu !== urlLink) {
      this.selectedMenu = urlLink;
      this.selectedModule = moduleSrc;
      this.selectedFeatureType = subModuleSrc ? subModuleSrc : '';
      // this.setSelectedModuleOnLoad();
      this.router.navigate([`${urlLink}`]);
    }
  }

  openDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
