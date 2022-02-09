import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { User } from 'app/entities/artha-models/user.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { UserService } from '../administration-services/user.service';
import { Observable, Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { DocumentUploadPopUpComponent } from 'app/artha-helpers';
import { NgbModalOptions, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ExportHelper } from 'app/shared/util/export.helper';

@Component({
  selector: 'jhi-users',
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  options: ListHeaderOptions;
  title: string;
  createBtnTitle: string;
  pageName: string;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  currentSearch: string;
  users: User[] = [
    // {
    //   code: '1754',
    //   name: 'MMI Narayana Multispeciality Hospital, Raipur',
    //   status: true
    // }
  ];
  isEdit: boolean;
  editIndex: number;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;
  constructor(
    private activatedRoute: ActivatedRoute,
    private parseLinks: JhiParseLinks,
    private elementRef: ElementRef,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService,
    private userService: UserService,
    private router: Router,
    private modalService: NgbModal,
    private exportHelper: ExportHelper
  ) {
    this.pageName = 'users';
    this.title = 'Users';
    this.createBtnTitle = 'CREATE NEW';
    this.options = new ListHeaderOptions(
      'artha/administrator/users',
      false,
      false,
      false,
      false,
      true,
      true,
      null,
      false,
      null,
      null,
      false
    );
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'displayName.raw';
    this.reverse = true;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.isEdit = false;
  }
  ngOnInit() {
    this.loadAll();
  }
  loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.userService
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

  loadPage(pageNo) {
    //  this.page =  pageNo ? pageNo : pageNo;
    this.page = pageNo;
    this.loadAll();
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.reset();
    // this.page = 0;
    // this.loadAll();
  }
  clear() {
    this.currentSearch = '';
    this.reset();
    // this.page = 0;
    // this.loadAll();
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

  onUserUpdate({ id }: User) {
    this.isEdit = true;
    this.router.navigate([`/administrator/user/${id}`]);
  }

  createUser() {}

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.users = data) : (this.users = [...this.users, ...data]);
  }
  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  openDocumentUpload() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    const modalData = this.modalService.open(DocumentUploadPopUpComponent, ngbModalOptions);

    modalData.componentInstance.headerTitle = 'Please Select Document';
    modalData.componentInstance.isMultipleFiles = false;
    modalData.componentInstance.acceptFiles = '.csv';

    const dismiss$: Subscription = modalData.componentInstance.dismiss.pipe(finalize(() => dismiss$.unsubscribe())).subscribe(reason => {
      modalData.dismiss(reason);
    });

    const close$: Subscription = modalData.componentInstance.importAction
      .pipe(finalize(() => close$.unsubscribe()))
      .subscribe(searchQuery => {
        modalData.close(searchQuery);
      });

    modalData.result.then(
      result => {
        if (result) {
          this.importFromFile(result);
        }
      },
      () => {}
    );
  }

  importFromFile(fileData: any): void {
    const formData: FormData = new FormData();
    this.httpBlockerService.enableHttpBlocker(true);
    if (fileData) {
      for (let i = 0; i < fileData.length; i++) {
        formData.append('files', fileData[i], fileData[i].name);
      }
      this.subscribeToSaveResponse(this.userService.importPackages({}, formData));
    }
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset() {
    this.page = 0;
    this.loadAll();
  }

  private subscribeToSaveResponse(result: Observable<any>): void {
    result.subscribe(
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        // this.alertService.success('gatewayApp.packageMaster.importSucess');
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onError(res.error);
      }
    );
  }

  export() {
    this.page = 0;
    this.userService
      .export({
        query: this.currentSearch ? this.currentSearch : '*',
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => this.exportHelper.openFile(res.body),
        (res: HttpErrorResponse) => this.onError(res.error)
      );
  }
}
