import { Component, ViewChild, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UnitDialogComponent } from './unit-dialog/unit-dialog.component';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { UnitService } from '../administration-services/unit.service';
import { Unit } from 'app/entities/artha-models/unit.model';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DocumentUploadPopUpComponent } from 'app/artha-helpers';
import { ExportHelper } from 'app/shared/util/export.helper';

@Component({
  selector: 'jhi-units',
  templateUrl: './units.component.html'
})
export class UnitsComponent implements OnInit {
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
  units: Unit[] = [
    // {
    //   code: '1754',
    //   name: 'MMI Narayana Multispeciality Hospital, Raipur',
    //   status: true
    // }
  ];
  modalRef: NgbModalRef;
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
    private modalService: NgbModal,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService,
    private unitService: UnitService,
    private router: Router,
    private exportHelper: ExportHelper
  ) {
    this.pageName = 'units';
    this.title = 'Unit List View';
    this.createBtnTitle = 'CREATE NEW';
    this.options = new ListHeaderOptions(
      'artha/administrator/units',
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
    this.predicate = 'code.keyword';
    this.reverse = true;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.isEdit = false;
  }
  ngOnInit() {
    this.loadAll();
  }
  loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.unitService
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

  reset() {
    this.page = 0;
    this.units = [];
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
  onUnitUpdate(index) {
    this.isEdit = true;
    this.editIndex = index;
    const unitData = { ...this.units[this.editIndex] };
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(UnitDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.isEdit = this.isEdit;
    this.modalRef.componentInstance.unit = unitData;
    this.modalRef.result.then(
      unitRes => {
        if (unitRes.code) {
          // TODO : have to call API
          // this.units[this.editIndex] = unitRes;
        }
        this.isEdit = false;
        this.editIndex = undefined;
      },
      () => {}
    );
  }
  createUnit() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(UnitDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.isEdit = this.isEdit;
    this.modalRef.result.then(
      unitRes => {
        if (!this.isEdit && unitRes.code) {
          // TODO : Have to call API for creation units curently uploading from backend
          // this.units.unshift(unitRes);
        }
      },
      () => {}
    );
  }

  createNewUnit() {
    this.router.navigate([`artha/administrator/unit-new`]);
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.units = data) : (this.units = [...this.units, ...data]);
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
      this.subscribeToSaveResponse(this.unitService.importPackages({}, formData));
    }
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

  public goToUnitDetail() {
    alert('go unit detail');
  }

  onClick(id) {
    this.router.navigate([`artha/administrator/unit/${id}/detail`]); // detail page
  }

  export() {
    this.page = 0;
    this.unitService
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

