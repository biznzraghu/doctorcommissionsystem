import { Component, OnInit, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { AdvanceSearchComponent } from 'app/artha-helpers';
import { NgbModalOptions, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { VariableHelperService } from '../variable-payout-helper.service';
import { JhiEventManager } from 'ng-jhipster';
import { VariablePayoutService } from '../variable-payout.service';
@Component({
  selector: 'jhi-variable-payout-list',
  templateUrl: './variable-payout-list.component.html'
})
export class VariablePayoutListComponent implements OnInit {
  options: ListHeaderOptions;
  createBtnTitle: string;
  title: string;
  tabListItems = [];
  tabIndex: number;
  currentSearch: string;
  preference: any;
  tabMenu = 'PAYOUTS';
  variblePayoutVersionList = [];
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private elementRef: ElementRef,
    private modalService: NgbModal,
    private prifrencesService: PreferenceService,
    private variableHelperService: VariableHelperService,
    private eventManager: JhiEventManager,
    private variablePayoutService: VariablePayoutService
  ) {
    this.initializeTabs();
    this.title = 'Variable Payout';
    this.createBtnTitle = 'CREATE NEW';
    // this.tabIndex = 0;
    this.currentSearch = this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
    this.preference = this.prifrencesService.currentUser();
    this.activatedRoute.queryParams.subscribe(params => {
      const index = params['currentTabIndex'] ? Number(params['currentTabIndex']) : 0;
      this.onTabChange(index);
      this.options = new ListHeaderOptions(
        'artha/administrator/value-set',
        false,
        false,
        false,
        false,
        false,
        true,
        ['103100', '103102'],
        true,
        this.tabListItems,
        index
      );
    });
  }

  ngOnInit() {}

  initializeTabs() {
    this.tabListItems.push({ label: 'PAYOUTS', value: 'PAYOUTS', count: 0 });
    this.tabListItems.push({ label: 'CHANGE REQUESTS', value: 'CHANGE_REQUESTS', count: 0 });
    this.tabListItems.push({ label: 'TEMPLATES', value: 'TEMPLATES', count: 0 });
  }

  onTabChange(index) {
    this.tabIndex = index;
    this.currentSearch = '';
    this.tabMenu = this.tabListItems[index].label;
    this.eventManager.broadcast({ name: 'clearSearch', content: false });
  }

  setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }

  openNewPage() {
    this.router.navigate(['artha/variable-payouts/variable-payouts-new'], { replaceUrl: true });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
  }

  clear() {
    this.currentSearch = '';
  }

  async openAdvanceSearch() {
    let newArray: any[] = [];
    if (this.tabIndex !== 2) {
      const unitCode = this.preference.organization.code;

      await this.variablePayoutService
        .getVariablePayoutVersionsList({ query: unitCode })
        .toPromise()
        .then((data: any) => {
          newArray = data;
          newArray = newArray.sort((a, b) => {
            return a - b;
          });
        });
      this.variblePayoutVersionList = newArray.map(item => {
        return { name: item, value: item };
      });
    }
    const ngModaloption: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary advance-search-modal'
    };

    const moDalData = this.modalService.open(AdvanceSearchComponent, ngModaloption);

    moDalData.componentInstance.pageName = 'variable-payout';
    moDalData.componentInstance.moduleName = 'dashboard';
    moDalData.componentInstance.feature = 'variable-payout';
    moDalData.componentInstance.userLogin = this.preference.user.login;
    moDalData.componentInstance.subFeature = this.tabMenu;
    moDalData.componentInstance.inputformElement = [];
    moDalData.componentInstance.selectformElement = [];
    moDalData.componentInstance.dateformElement = [];

    moDalData.componentInstance.dateformElement = this.variableHelperService.getDateElement();
    moDalData.componentInstance.typeHeadformElement = this.variableHelperService.getTypeAheadformElement(this.tabIndex);
    moDalData.componentInstance.selectformElement = this.variableHelperService.getSelectFormElement(
      this.tabIndex,
      this.variblePayoutVersionList
    );

    const dismiss$: Subscription = moDalData.componentInstance.dismiss.pipe(finalize(() => dismiss$.unsubscribe())).subscribe(reason => {
      moDalData.dismiss(reason);
    });

    const close$: Subscription = moDalData.componentInstance.saveAndClose
      .pipe(finalize(() => close$.unsubscribe()))
      .subscribe(searchQuery => {
        moDalData.close(searchQuery);
      });

    moDalData.result.then(
      result => {
        if (result) {
          this.search(result);
          this.eventManager.broadcast({ name: 'clearSearch', content: true });
        }
      },
      reason => {} // eslint-disable-line @typescript-eslint/no-unused-vars
    );
  }
}
