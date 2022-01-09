import { Component, OnInit, ElementRef, ViewChild, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiParseLinks } from 'ng-jhipster';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { ServiceItemBenefitTemplate } from 'app/entities/artha-models/variable-payout.model';
import { VariablePayoutService } from 'app/entities/variable-payout/variable-payout.service';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';

@Component({
  selector: 'jhi-templates-tab-list',
  templateUrl: './templates-tab.component.html'
})
export class TemplatesTabListComponent implements OnInit, OnChanges {
  currentSearch: string;
  @Input() parentSearch;
  page: any;
  itemsPerPage: any;
  links: any;
  totalItems: any;
  queryCount: any;
  variablePayoutDatas: ServiceItemBenefitTemplate[] = [];
  noRecordFound: boolean;
  dateFormat: any;
  dateTimeFormat: any;
  predicate: any;
  reverse: any;
  dateTimeBothFormat: any;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  preference: any;
  unitCode: string;
  defaultQuery = '*';
  constructor(
    private variablePayoutService: VariablePayoutService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private parseLinks: JhiParseLinks,
    private errorParser: ErrorParser,
    private elementRef: ElementRef,
    private httpBlockerService: HttpBlockerService,
    private prefrencesService: PreferenceService
  ) {
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.page = 0;
    this.itemsPerPage = 20;
    this.links = {
      last: 0
    };
    this.noRecordFound = false;
    this.dateFormat = 'dd/MM/yyyy';
    this.dateTimeFormat = 'HH:mm';
    this.dateTimeBothFormat = 'dd/MM/yyyy HH:mm';
    this.predicate = 'lastModifiedBy.lastModifiedDate';
    this.reverse = false;
    this.preference = this.prefrencesService.currentUser();
    if (this.preference && this.preference.organization && this.preference.organization.code) {
      this.unitCode = this.preference.organization.code;
    }
  }

  ngOnInit() {
    // this.loadAll();
  }

  loadAll(onTabClick?: boolean) {
    this.httpBlockerService.enableHttpBlocker(true);
    this.variablePayoutService
      .getResourceServiceItemBenefitTemplates({
        query: this.currentSearch ? this.defaultQuery + ' AND ' + this.currentSearch : this.defaultQuery,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.onSuccess(res.body, res.headers, onTabClick);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (res: HttpErrorResponse) => {
          this.onError(res.error);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
  }

  private onSuccess(data, headers, onTabClick) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;

    if (onTabClick) {
      this.variablePayoutDatas = [];
    }

    for (let i = 0; i < data.length; i++) {
      this.variablePayoutDatas.push(data[i]);
    }

    if (onTabClick && this.directiveScroll) {
      this.directiveScroll.scrollToTop();
    }

    if (data.length === 0) {
      this.noRecordFound = true;
    }
  }

  private onError(error) {
    this.errorParser.parse(error);
  }

  setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = 71;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }

  loadPage(page) {
    this.page = page;
    this.loadAll(false);
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.loadAll(true);
  }

  clear() {
    this.page = 0;
    this.currentSearch = '';
    this.loadAll(true);
  }

  trackId(index: number, item: ServiceItemBenefitTemplate) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset() {
    this.page = 0;
    this.loadAll(true);
  }

  fixedHeaderWidth() {
    const ele = this.elementRef.nativeElement.querySelector('div.infinite-scroll-table');
    return ele.clientWidth;
  }

  ngOnChanges(changes: SimpleChanges) {
    this.currentSearch = changes.parentSearch.currentValue || '';
    this.search(this.currentSearch);
  }

  viewDetail(id) {
    this.router.navigate([`artha/variable-payouts/variable-payouts-template/${id}/detail`], { replaceUrl: true });
  }
}
