import { Component, ViewChild, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DepartmentDialogComponent } from './department-dialog/department-dialog.component';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Department } from 'app/entities/artha-models/department.model';
import { DepartmentService } from '../administration-services/department.service';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';

@Component({
  selector: 'jhi-departments',
  templateUrl: './departments.component.html'
})
export class DepartmentsComponent implements OnInit {
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
  departments: Department[] = [];
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
    private departmentService: DepartmentService
  ) {
    this.pageName = 'departments';
    this.title = 'Departments';
    this.createBtnTitle = 'Create New';
    this.options = new ListHeaderOptions('artha/administrator/departments', false, false, false, false, true, true, null, false);
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.previousPage = 0;
    this.predicate = 'active';
    this.reverse = false;
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.isEdit = false;
  }
  ngOnInit() {
    this.loadAll();
  }
  loadAll() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.departmentService
      .search({
        query: this.currentSearch ? this.currentSearch : '*',
        page: this.page,
        size: this.itemsPerPage
        // sort: this.sort()
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
  reset() {}
  loadPage(pageNo) {
    pageNo ? pageNo : pageNo;
  }
  search(query) {
    if (!query) {
      return this.clear();
    }
    this.reset();
    this.page = 0;
    this.currentSearch = query;
    this.loadAll();
  }
  clear() {
    this.reset();
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
  onDepartmentUpdate(index) {
    this.isEdit = true;
    this.editIndex = index;
    const departmentData = { ...this.departments[this.editIndex] };
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(DepartmentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.isEdit = this.isEdit;
    this.modalRef.componentInstance.department = departmentData;
    this.modalRef.result.then(
      departmentRes => {
        if (departmentRes.code) {
          this.departments[this.editIndex] = departmentRes;
        }
        this.isEdit = false;
        this.editIndex = undefined;
      },
      () => {}
    );
  }
  createDepartment() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(DepartmentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.isEdit = this.isEdit;
    this.modalRef.result.then(
      departmentRes => {
        if (!this.isEdit && departmentRes.code) {
          this.departments.unshift(departmentRes);
        }
      },
      () => {}
    );
  }
  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    // this.links = this.parseLinks.parse(headers.get('link'));
    this.queryCount = headers.get('X-Total-Count');
    this.page === 0 ? (this.departments = data) : (this.departments = [...this.departments, ...data]);
  }
  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }
}
