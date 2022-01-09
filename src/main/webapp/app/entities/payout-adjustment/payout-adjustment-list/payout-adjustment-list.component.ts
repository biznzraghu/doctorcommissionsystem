import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InsertPayoutDialogComponent } from './insert-payout-entry-dialog/insert-payout-entry-dialog.component';
import { Router, ActivatedRoute } from '@angular/router';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { PayoutAdjustmentService } from '../payout-adjustment.service';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService, JhiParseLinks, JhiEventManager } from 'ng-jhipster';
import { PayoutAdjustmentModel } from 'app/entities/artha-models/payout-adjustment.model';
import { PayoutStatusClassMap, PayoutStatusCss } from 'app/entities/artha-models/status.enum';
import { PayoutHelperService } from '../payout-helper.service';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { AdvanceSearchComponent } from 'app/artha-helpers';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { TransactionApprovalDetailsDTO } from 'app/entities/artha-models/comment.model';

@Component({
  selector: 'jhi-payout-adjustment-list',
  templateUrl: './payout-adjustment-list.component.html'
})
export class PayoutAdjustmentListComponent implements OnInit {
  options: ListHeaderOptions;
  public statusClassMap = PayoutStatusClassMap;
  public statusCssClassMap = PayoutStatusCss;
  createBtnTitle: string;
  title: string;
  dummyName: string;
  payoutList: PayoutAdjustmentModel[] = [];
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  currentSearch: string;
  dateFormat = 'dd/MM/yyyy hh:mm';
  public amountFormat = '2.2-2';
  modalRef: NgbModalRef;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  preference: any;
  tabMenu = 'All';
  tabStatus = '';
  unitCode: string;
  unitId: string;
  defaultQuery = '*';
  constructor(
    private modalService: NgbModal,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private elementRef: ElementRef,
    private parseLinks: JhiParseLinks,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService,
    private payoutAdjustmentService: PayoutAdjustmentService,
    private payoutHelperService: PayoutHelperService,
    private prifrencesService: PreferenceService,
    private eventManager: JhiEventManager
  ) {
    this.title = 'Payout Adjustment';
    this.createBtnTitle = 'CREATE NEW';
    this.options = new ListHeaderOptions(
      'artha/administrator/value-set',
      false,
      false,
      false,
      false,
      false,
      true,
      ['101102', '101100'],
      false,
      [],
      null,
      false
    );
    this.dummyName = 'Preksha Julia Petersen';
    this.itemsPerPage = 10;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'createdDateTime';
    this.reverse = false;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.preference = this.prifrencesService.currentUser();
    if (this.preference && this.preference.organization && this.preference.organization.code) {
      this.unitCode = this.preference.organization.code;
      this.unitId = this.preference.organization.id;
      this.defaultQuery = `unitCode: ${this.unitCode}`;
    }
  }
  // Header related methods
  ngOnInit() {
    this.loadAll();
  }
  loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.payoutAdjustmentService
      .search({
        query: this.currentSearch ? this.defaultQuery + ' AND ' + this.currentSearch : this.defaultQuery,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => {
          this.onSuccess(res.body, res.headers);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (error: any) => {
          this.onError(error);
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
    this.loadAll();
  }
  loadPage(pageNo) {
    this.page = pageNo;
    this.loadAll();
  }
  search(query) {
    if (!query) {
      return this.clear();
    }
    // this.reset();
    this.page = 0;
    this.currentSearch = query;
    this.eventManager.broadcast({ name: 'clearSearch', content: true });
    this.loadAll();
  }
  clear() {
    // this.reset();
    this.page = 0;
    this.currentSearch = '';
    this.loadAll();
  }
  setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }
  trackId(item: any) {
    return item.id;
  }
  openNewPage() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(InsertPayoutDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.unitId = this.unitId;
    this.modalRef.result.then(
      result => {
        if (result) {
          this.payoutHelperService.sharePayoutData(result).then(success => {
            // eslint-disable-next-line no-console
            console.log('success ', success);
            if (success) {
              this.nevigateTo('artha/payout-adjustment/new');
            }
          });
        }
      },
      () => {}
    );
  }
  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.payoutList = data) : (this.payoutList = [...this.payoutList, ...data]);
  }
  private onError(err) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: err && err.error.detail ? err.error.detail : `Server Error !` });
  }
  public viewDetail(id) {
    const navigationLink = `artha/payout-adjustment/detail/${id}`;
    this.nevigateTo(navigationLink);
  }
  private nevigateTo(pageName: string) {
    this.router.navigate([pageName]);
  }
  openCommentBlock(transactionApprovalDetails: any) {
    if (transactionApprovalDetails && transactionApprovalDetails.length && transactionApprovalDetails.length > 0) {
      const ngbModalOptions: NgbModalOptions = {
        backdrop: 'static',
        keyboard: true,
        windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
      };
      this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
      this.modalRef.componentInstance.transctionDetailList = transactionApprovalDetails;
      this.modalRef.componentInstance.isDisplayMode = true;
      this.modalRef.componentInstance.headerTitle = 'Comments';
      this.modalRef.componentInstance.displayRejectBtn = false;
      this.modalRef.componentInstance.showSendForApprovalBtn = false;
      this.modalRef.componentInstance.primaryBtnLabel = '';
      this.modalRef.result.then(
        () => {},
        () => {}
      );
    }
  }
  openAdvanceSearch() {
    const ngModaloption: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary advance-search-modal'
    };

    const moDalData = this.modalService.open(AdvanceSearchComponent, ngModaloption);

    moDalData.componentInstance.pageName = 'payoutAdjustment';
    moDalData.componentInstance.moduleName = 'dashboard';
    moDalData.componentInstance.feature = 'payout-adjustment';
    moDalData.componentInstance.userLogin = this.preference.user.login;
    moDalData.componentInstance.subFeature = this.tabMenu + ' ' + this.tabStatus;
    moDalData.componentInstance.inputformElement = [];
    moDalData.componentInstance.selectformElement = [];
    moDalData.componentInstance.dateformElement = [];

    moDalData.componentInstance.inputformElement = this.payoutHelperService.getInputFormElement();
    moDalData.componentInstance.dateformElement = this.payoutHelperService.getDateElement();
    moDalData.componentInstance.typeHeadformElement = this.payoutHelperService.getTypeAheadformElement();
    moDalData.componentInstance.selectformElement = this.payoutHelperService.getSelectFormElement();

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
        }
      },
      reason => {} // eslint-disable-line @typescript-eslint/no-unused-vars
    );
  }
}
