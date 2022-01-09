import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchTermModify } from 'app/artha-helpers';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { Observable } from 'rxjs';
import { UnitService } from '../administration-services/unit.service';
import { VariablePayoutService } from 'app/entities/variable-payout/variable-payout.service';
import { JhiAlertService } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { DepartmentService } from '../administration-services/department.service';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DepartmentPopupComponent } from './department-dialog/department-popup.component';
import { UnitDepartmentModel } from './department.model';

@Component({
  selector: 'jhi-unit-detail',
  templateUrl: './unit-detail.component.html'
})
export class UnitDetailComponent implements OnInit {
  options: ListHeaderOptions;
  pageName: string;
  title: string;
  editBtnTitle: string;
  searchText: string;
  isSearching: boolean;
  unitDetail: any;
  tablist: any;
  selectedTab = 0;
  departmentList: any[] = [];
  defaultQuery = '';
  predicate = 'createdDate';
  reverse = false;
  searchDeptText = '';
  currentSearch = '';
  isDepartmentList = false;
  departmentParams: any = {};
  organizationId: string;
  page = 0;
  modalRef: NgbModalRef;
  selectedDepartment: UnitDepartmentModel;
  constructor(
    private elementRef: ElementRef,
    private router: Router,
    private route: ActivatedRoute,
    private unitService: UnitService,
    private searchTermModify: SearchTermModify,
    private variablePayoutService: VariablePayoutService,
    private jhiAlertService: JhiAlertService,
    private deptService: DepartmentService,
    private modalService: NgbModal
  ) {
    this.selectedDepartment = new UnitDepartmentModel();
    this.route.params.subscribe(params => {
      this.organizationId = params.id;
      this.isDepartmentList = params['department'] === 'y' ? true : false;
      if (this.isDepartmentList) {
        this.selectedTab = 1;
        this.departmentParams = { department: 'y', departmentId: params['departmentId'] };
        this.loadOrganization(params['departmentId']);
      } else {
        this.departmentParams = {};
      }
    });

    this.route.data.subscribe(data => {
      if (data['unit']) {
        this.unitDetail = data['unit'];
        this.title = `${this.unitDetail.name}`;
        this.defaultQuery = `organization.code:${this.unitDetail.code}`;
      }
    });
  }

  private loadOrganization(id) {
    this.deptService.find(id).subscribe(
      (res: HttpResponse<any>) => {
        this.unitDetail = res.body;
        this.title = `${this.unitDetail.organization.name}`;
      },
      error => {
        this.onError(error.json);
      }
    );
  }

  ngOnInit() {
    this.tablist = [
      { label: 'Unit Details', value: 'Unit Details' },
      { label: 'Departments', value: 'Departments' }
    ];
    this.pageName = 'unit';
    this.editBtnTitle = 'Edit';
    this.options = new ListHeaderOptions(
      'artha/administrator/unit',
      false,
      true,
      false,
      true,
      false,
      false,
      null,
      true,
      this.tablist,
      this.selectedTab,
      true
    );
  }

  // search -NgbTypeaheadConfig
  searchViewHeaderTypehead = (text: Observable<string>) =>
    text
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => (this.isSearching = term.length >= 1))
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.unitService
              .search({
                page: 0,
                size: 1000,
                query: this.searchTermModify.modify(term)
              })
              .map(res => {
                return res.body;
              })
      )
      .do(() => (this.isSearching = false));

  inputformatSearchTypeheadData = (x: any) => x.name;
  displayformatSearchTypeheadData = (x: any) => x.code + ' | ' + x.name;

  onSelectSearchResultItem($event, searchValueSet) {
    const data = $event && $event.item;
    searchValueSet ? '' : '';
    this.searchResult(data);
  }

  searchResult(result: any) {
    this.router.navigate([`artha/administrator/unit/${result.id}/detail`], { replaceUrl: true });
  }

  public setScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight;
  }

  public clear() {
    this.searchText = '';
  }

  public editValueSet() {}
  public backToList() {
    this.router.navigate([`artha/administrator/units`]);
  }

  public onTabChange(tabIndex) {
    this.selectedTab = tabIndex;
    this.isDepartmentList = false;
    this.currentSearch = '';
    if (this.selectedTab === 1) {
      this.editBtnTitle = 'Add Department';
      this.loadDepartment();
    } else {
      this.editBtnTitle = 'Edit';
      this.router.navigate([`artha/administrator/unit/${this.organizationId}/detail`], { replaceUrl: true });
    }
  }

  private loadDepartment() {
    this.variablePayoutService
      .getDepartment({
        query: this.currentSearch === '' ? this.defaultQuery : this.currentSearch + ` AND organization.code:${this.unitDetail.code}`,
        page: this.page,
        size: 9999,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.departmentList = res.body;
        },
        (error: any) => {
          this.onError(error.json);
        }
      );
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  public onSearchDepartment() {
    if (this.searchDeptText.length > 1) {
      this.currentSearch = `name:*${this.searchDeptText.trim()}*`;
    } else {
      this.currentSearch = '';
    }
    this.loadDepartment();
  }

  public clearDepartmentSearch() {
    this.searchDeptText = '';
    this.currentSearch = '';
    this.loadDepartment();
  }

  public goToEdit() {
    this.router.navigate([`artha/administrator/unit-edit`, this.organizationId]);
  }

  public editDepartment(departmentDetail) {
    this.selectedDepartment = departmentDetail;
    this.openDepartmentPopUpBlock('Update');
  }

  public goToDepartmentDetail(id) {
    this.departmentParams = { department: 'y', departmentId: id };
    this.router.navigate(['artha/administrator/unit-dept', this.organizationId, this.departmentParams]);
  }

  reset() {
    this.page = 0;
    this.departmentList = [];
    this.loadDepartment();
  }

  openDepartmentPopUpBlock(title) {
    if (title === 'Add') {
      this.selectedDepartment = new UnitDepartmentModel();
      this.selectedDepartment.organization = this.unitDetail;
    }
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(DepartmentPopupComponent, ngbModalOptions);
    this.modalRef.componentInstance.title = title;
    this.modalRef.componentInstance.department = this.selectedDepartment;

    this.modalRef.result.then(
      result => {
        if (result) {
          this.loadDepartment();
        }
      },
      () => {}
    );
  }
}
