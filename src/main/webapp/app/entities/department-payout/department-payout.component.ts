import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { DepartmentPayoutService } from './department-payout.service';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { PayoutStatusClassMap, PayoutStatusCss } from '../artha-models/status.enum';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { AdvanceSearchComponent, SearchTermModify } from 'app/artha-helpers';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DepartmentHelperService } from './department-helper.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ExportHelper } from 'app/shared/util/export.helper';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';

@Component({
  selector: 'jhi-department-payout',
  templateUrl: './department-payout.component.html'
})
export class DepartmentPayoutComponent implements OnInit {
  options: ListHeaderOptions;
  public statusClassMap = PayoutStatusClassMap;
  public statusCssClassMap = PayoutStatusCss;
  createBtnTitle: string;
  title: string;
  dummyName: string;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  links: any;
  previousPage: any;
  reverse: any;
  currentSearch: string;
  departmentPayoutList: any[] = [];
  dateFormat = 'dd/MM/yyyy ';
  timeFirmate = 'hh:mm';
  isAdvanceSearch = false;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  public tablist: any;
  public selectedTab = 0;
  prefrence: any;
  tabMenu = 'PAYOUTS';
  tabStatus = '';
  unitCode: string;
  defaultQuery = '*';
  departmentPayoutVersionList: any[] = [];
  qurText: string;
  qurExport: string;

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;
  constructor(
    private modalService: NgbModal,
    private router: Router,
    private departmentPayoutService: DepartmentPayoutService,
    private activatedRoute: ActivatedRoute,
    private jhiAlertService: JhiAlertService,
    private elementRef: ElementRef,
    private parseLinks: JhiParseLinks,
    private prifrencesService: PreferenceService,
    private departmentHelperService: DepartmentHelperService,
    private eventManager: JhiEventManager,
    private searchTermModify: SearchTermModify,
    private exportHelper: ExportHelper,
    private httpBlockerService: HttpBlockerService
  ) {
    this.title = 'Department Payout';
    this.createBtnTitle = 'CREATE NEW';
    this.tablist = [
      { label: 'PAYOUTS', value: 'PAYOUTS' },
      { label: 'CHANGE REQUESTS', value: 'CHANGE REQUESTS' }
    ];
    this.options = new ListHeaderOptions(
      'artha/administrator/value-set',
      false,
      false,
      false,
      false,
      false,
      true,
      ['102100', '102102'],
      true,
      this.tablist,
      this.selectedTab
    );
    this.dummyName = 'Preksha Julia Petersen';
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'createdDate';
    this.reverse = false;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.prefrence = this.prifrencesService.currentUser();
    if (this.prefrence && this.prefrence.organization && this.prefrence.organization.code) {
      this.unitCode = this.prefrence.organization.code;
      this.defaultQuery = `unitCode: ${this.unitCode}`;
    }
  }

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    const q = this.tabMenu === 'PAYOUTS' ? this.defaultQuery + ' AND changeRequestStatus: (APPROVED)' : this.defaultQuery;
    this.qurExport = q;
    this.httpBlockerService.enableHttpBlocker(true);
    this.departmentPayoutService
      .search(this.tabMenu, {
        query: this.currentSearch ? q + ' AND ' + this.currentSearch : q,
        page: this.page,
        size: this.tabMenu === 'PAYOUTS' ? 9999 : this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => {
          this.onSuccess(res.body, res.headers);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (error: any) => {
          this.onError(error.json);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset() {
    this.page = 0;
    this.directiveScroll.scrollToTop();
    this.loadAll();
  }

  search(searchTerm) {
    if (!searchTerm) {
      return this.clear();
    }
    this.page = 0;
    if (searchTerm) {
      if (searchTerm.trim().length === 0) {
        searchTerm = '*';
      } else {
        if (this.isAdvanceSearch === false) {
          searchTerm = this.searchTermModify.modify(searchTerm);
        }
      }
    }
    this.currentSearch = searchTerm;
    this.eventManager.broadcast({ name: 'clearSearch', content: true });
    this.loadAll();
  }

  loadPage(pageNo) {
    this.page = pageNo;
    this.loadAll();
  }

  setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }

  onTabChange(tabIndex) {
    this.selectedTab = tabIndex;
    this.tabMenu = this.tablist[this.selectedTab].value;
    this.page = 0;
    this.loadAll();
  }

  createNewPage(): any {
    this.router.navigate(['artha/department-payout/department-payout-new'], { replaceUrl: true });
  }

  clear() {
    this.currentSearch = '';
    this.reset();
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.departmentPayoutList = data) : (this.departmentPayoutList = [...this.departmentPayoutList, ...data]);
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  editPayoutRange(payout) {
    const params = {};
    params['id'] = payout.id;

    this.router.navigate([`artha/department-payout/department-payout-edit`, params], { replaceUrl: true });
    // if (payout.changeRequestStatus === 'PENDING_APPROVAL') {
    //   this.router.navigate([`artha/department-payout/department-payout-view`, params], { replaceUrl: true });
    // } else {
    //   this.router.navigate([`artha/department-payout/department-payout-edit`, params], { replaceUrl: true });
    // }
  }

  viewDepPayout(payout) {
    const params = {};
    params['id'] = payout.id;
    this.router.navigate([`artha/department-payout/department-payout-view`, params], { replaceUrl: true });
  }

  async openAdvanceSearch() {
    let newArray: any[] = [];

    const unitCode = this.prefrence.organization.code;

    await this.departmentPayoutService
      .getDepartmentPayoutVersionsList({ query: unitCode })
      .toPromise()
      .then((data: any) => {
        newArray = data;
        newArray = newArray.sort((a, b) => {
          return a - b;
        });
      })
      .catch(error => {
        // console.log(error)
      });
    this.departmentPayoutVersionList = newArray.map(item => {
      return { name: item, value: item };
    });

    const ngModaloption: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary advance-search-modal'
    };

    const moDalData = this.modalService.open(AdvanceSearchComponent, ngModaloption);

    moDalData.componentInstance.pageName = 'department-payout';
    moDalData.componentInstance.moduleName = 'dashboard';
    moDalData.componentInstance.feature = 'department-payout';
    moDalData.componentInstance.userLogin = this.prefrence.user.login;
    moDalData.componentInstance.subFeature = this.tabMenu + ' ' + this.tabStatus;
    moDalData.componentInstance.inputformElement = [];
    moDalData.componentInstance.selectformElement = [];
    moDalData.componentInstance.dateformElement = [];

    moDalData.componentInstance.inputformElement = this.departmentHelperService.getInputFormElement();
    moDalData.componentInstance.dateformElement = this.departmentHelperService.getDateElement();
    moDalData.componentInstance.typeHeadformElement = this.departmentHelperService.getTypeAheadformElement();
    moDalData.componentInstance.selectformElement = this.departmentHelperService.getSelectFormElement(
      this.tabMenu,
      this.departmentPayoutVersionList
    );

    const dismiss$: Subscription = moDalData.componentInstance.dismiss.pipe(finalize(() => dismiss$.unsubscribe())).subscribe(reason => {
      moDalData.dismiss(reason);
    });

    const close$: Subscription = moDalData.componentInstance.saveAndClose
      .pipe(finalize(() => close$.unsubscribe()))
      .subscribe(searchQuery => {
        moDalData.close(searchQuery);
      });

    moDalData.result.then(result => {
      if (result) {
        this.isAdvanceSearch = true;
        this.search(result);
      } else {
        this.isAdvanceSearch = false;
      }
    });
  }

  export(): any {
    this.page = 0;
    if (this.tabMenu === 'PAYOUTS') {
      this.qurText = 'true';
    } else {
      this.qurText = 'false';
    }
    this.departmentPayoutService
      .exportListPage(this.qurText, {
        query: this.currentSearch ? this.qurExport + ' AND ' + this.currentSearch : this.qurExport,
        page: this.page,
        size: this.tabMenu === 'PAYOUTS' ? 9999 : this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => this.exportHelper.openFile(res.body),
        (res: HttpErrorResponse) => this.onError(res.error)
      );
  }
}
