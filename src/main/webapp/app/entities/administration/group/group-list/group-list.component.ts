import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { GroupService } from '../group.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-group-list',
  templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit {
  options: ListHeaderOptions;
  title: string;
  createBtnTitle: string;
  pageName: string;
  groups: any[] = [];
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
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;
  constructor(
    private elementRef: ElementRef,
    private parseLinks: JhiParseLinks,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService,
    private groupService: GroupService
  ) {
    this.pageName = 'group';
    this.title = 'Group';
    this.createBtnTitle = 'CREATE NEW';
    this.options = new ListHeaderOptions('artha/administrator/departments', false, false, true, false, true, true, null, false, null, 0);
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'name.sort';
    this.reverse = false;
    this.currentSearch = this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }
  ngOnInit(): void {
    this.loadAll();
  }
  private loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.groupService
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
  public createGroup(): void {
    this.nevigateTo('administrator/group/new');
  }
  private nevigateTo(pageName: string) {
    this.router.navigate([pageName]);
  }
  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }
  public reset() {
    this.page = 0;
    this.loadAll();
  }
  public loadPage(pageNo) {
    this.page = pageNo;
    this.loadAll();
  }
  public search(query) {
    if (!query) {
      return this.clear();
    }
    // this.reset();
    this.page = 0;
    this.currentSearch = query;
    this.loadAll();
  }
  public clear() {
    // this.reset();
    this.page = 0;
    this.currentSearch = '';
    this.loadAll();
  }
  public trackId(item: any) {
    return item.id;
  }
  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.groups = data) : (this.groups = [...this.groups, ...data]);
  }
  private onError(err) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: err && err.error.detail ? err.error.detail : `Server Error !` });
  }
  public setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    const fixedHeaderHeight = this.elementRef.nativeElement.querySelector('div.fixed-header-table-head-container').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedHeaderHeight - 10;
  }
  public goToDetailPage(groupId) {
    // group/edit/:id artha/payout-adjustment/detail/${id} group/detail
    // this.router.navigate([`administrator/group/detail/${groupId}`]);
    this.router.navigate([`artha/administrator/group/${groupId}`]);
  }
}
