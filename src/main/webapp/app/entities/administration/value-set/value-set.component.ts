import { Component, ViewChild, OnInit, ElementRef } from '@angular/core';
import { Subscription } from 'rxjs';
import { ValueSet } from 'app/entities/artha-models/value-set.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { ValueSetService } from '../administration-services/value-set.service';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'jhi-value-set',
  templateUrl: './value-set.component.html'
})
export class ValueSetComponent implements OnInit {
  options: ListHeaderOptions;

  currentAccount: any;
  valueSets: ValueSet[] = [];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  title: string;
  createBtnTitle: string;
  pageName: string;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  constructor(
    private activatedRoute: ActivatedRoute,
    private parseLinks: JhiParseLinks,
    private elementRef: ElementRef,
    private router: Router,
    private httpBlockerService: HttpBlockerService,
    private valueSetService: ValueSetService,
    private jhiAlertService: JhiAlertService
  ) {
    this.pageName = 'valueSet';
    this.title = 'Value Sets';
    this.createBtnTitle = 'CREATE NEW';
    this.options = new ListHeaderOptions('artha/administrator/value-set', false, false, false, false, true, false, null, false);
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'name.sort';
    this.reverse = false;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }
  ngOnInit() {
    this.loadAll();
  }
  editValueSetPage(valueSetId: number) {
    this.router.navigate([`artha/administrator/value-set/${valueSetId}/detail`]); // /edit
  }
  loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.valueSetService
      .search({
        query: this.currentSearch ? this.currentSearch : '*',
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => {
          this.onSuccess(res.body, res.headers);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (res: HttpErrorResponse) => {
          this.onError(res.error);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
  }
  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }
  loadPage(page: number) {
    this.page = page;
    this.loadAll();
  }
  reset() {
    this.page = 0;
    this.valueSets = [];
    if (this.page === 0 && this.directiveScroll) {
      this.directiveScroll.scrollToTop();
    }
    this.loadAll();
  }

  clear() {
    // this.reset();
    this.page = 0;
    this.currentSearch = '';
    this.loadAll();
  }
  search(query) {
    if (!query) {
      return this.clear();
    }
    // this.reset();
    this.page = 0;
    this.currentSearch = query;
    this.loadAll();
  }
  auditLog() {}
  trackId(item: ValueSet) {
    return item.id;
  }
  export() {}
  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.valueSets = data) : (this.valueSets = [...this.valueSets, ...data]);
  }
  private onError(error) {
    this.jhiAlertService.error(
      error.message ? error.message : 'global.messages.response-msg',
      error.description ? { msg: error.description } : null,
      null
    );
  }
  setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }
}
