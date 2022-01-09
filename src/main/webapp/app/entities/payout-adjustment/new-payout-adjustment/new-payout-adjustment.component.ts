import { Component, ViewChild, TemplateRef, OnInit, ElementRef } from '@angular/core';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { Router } from '@angular/router';
import { PayoutAdjustmentService } from '../payout-adjustment.service';
import { InsertPayoutDialogComponent } from '../payout-adjustment-list/insert-payout-entry-dialog/insert-payout-entry-dialog.component';
import { PayoutAdjustmentDetail, PayoutAdjustmentModel, MonthList } from 'app/entities/artha-models/payout-adjustment.model';
import { concat, Subject, Observable, of } from 'rxjs';
// import { of } from 'rxjs/observable/of';

import { catchError, distinctUntilChanged, switchMap, debounceTime, map } from 'rxjs/operators';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { ArthaDetailHeaderOptions } from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import * as moment from 'moment';
import { PerfectScrollbarDirective, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService } from 'ng-jhipster';
import { PayoutHelperService } from '../payout-helper.service';
import { SearchTermModify } from 'app/artha-helpers';
import { NgSelectComponent } from '@ng-select/ng-select';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { DepartmentService } from 'app/entities/administration/administration-services/department.service';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-new-payout-adjustment',
  templateUrl: './new-payout-adjustment.component.html'
})
export class NewPayoutAdjustmentComponent implements OnInit {
  @ViewChild('headerView', { static: false }) headerView: TemplateRef<any>;
  public dateFormat = 'dd/mm/yyyy hh:mm';
  public amountFormat = '2.2-2';
  // HEADER OPTION
  public headerOptons: ArthaDetailHeaderOptions;
  public searchText: string;
  public contraSearch: boolean;
  public noRecordFound = true;
  public MonthClassMap = MonthList;
  private modalRef: NgbModalRef;
  // payout type either Employee or Department
  public payoutType: string;
  // payoutBannerDetails contain info about either selected Employee or Department details
  public payoutBannerDetails: any = {};
  // for new adjustment
  public newAdjustment: PayoutAdjustmentDetail = new PayoutAdjustmentDetail();
  public editAdjustment: PayoutAdjustmentDetail = new PayoutAdjustmentDetail();
  public payoutAdjustmentData: PayoutAdjustmentModel = new PayoutAdjustmentModel();
  // for Edit
  public isEditActive: boolean;
  public editIndex: number;
  monthList = [];
  public grandTotal: number;
  public grandTotalSymbol = '+';
  // Perfect Scrollbar Config
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  // ng-select related varibles
  public employeeInput$ = new Subject<string>();
  public employee$: any;

  public departmentInput$ = new Subject<string>();
  public department$: any;
  @ViewChild('searchEmployeeContra', { static: false }) searchEmployeeContra: NgSelectComponent;

  @ViewChild('searchDepartmentContra', { static: false }) searchDepartmentContra: NgSelectComponent;
  comment: any;

  preference: any;
  unitId: any;
  constructor(
    private mainCommunicationService: MainCommunicationService,
    private userService: UserService,
    private elementRef: ElementRef,
    private httpBlockerService: HttpBlockerService,
    private router: Router,
    private payoutAdjustmentService: PayoutAdjustmentService,
    private modalService: NgbModal,
    private jhiAlertService: JhiAlertService,
    private payoutHelperService: PayoutHelperService,
    private searchTermModify: SearchTermModify,
    private stateStorage: StateStorageService,
    public departmentService: DepartmentService,
    private prefrencesService: PreferenceService
  ) {
    this.headerOptons = new ArthaDetailHeaderOptions();
    this.headerOptons.status = 'NEW';
    this.headerOptons.showSearchIcon = false;
    this.payoutHelperService.sharePayoutDataTextAction$.subscribe(sharedInfo => {
      if (sharedInfo && sharedInfo.entryType && sharedInfo.data) {
        this.payoutType = sharedInfo.entryType;
        this.payoutBannerDetails = sharedInfo.data;
        if (this.payoutType === 'Employee') {
          this.headerOptons.isHeaderForEmployee = true;
          this.headerOptons.employee = this.payoutBannerDetails;
          this.headerOptons.employee.employeeNo = this.payoutBannerDetails.employeeNumber;
          this.headerOptons.remarksListCount = 0;
          this.headerOptons.showVersionNo = false;
          this.payoutAdjustmentData.employeeDetail = this.payoutBannerDetails;
          this.payoutAdjustmentData.employeeId = this.payoutBannerDetails.id
            ? this.payoutBannerDetails.id
            : this.payoutBannerDetails.employeeNumber;
          this.payoutAdjustmentData.department = null;
          this.payoutAdjustmentData.departmentId = null;
          this.payoutAdjustmentData.type = 'EMPLOYEE';
        } else {
          this.headerOptons.isHeaderForEmployee = false;
          this.headerOptons.department = this.payoutBannerDetails;
          this.headerOptons.remarksListCount = 0;
          this.headerOptons.showVersionNo = false;
          this.payoutAdjustmentData.department = this.payoutBannerDetails;
          this.payoutAdjustmentData.departmentId = this.payoutBannerDetails.id;
          this.payoutAdjustmentData.employeeDetail = null;
          this.payoutAdjustmentData.employeeId = null;
          this.payoutAdjustmentData.type = 'DEPARTMENT';
        }
      } else {
        this.openAndSelectPayoutType();
      }
    }),
      () => {
        this.openAndSelectPayoutType();
      };
    this.monthList = this.payoutHelperService.getYearAndMonth();
  }
  ngOnInit() {
    this.mainCommunicationService.updateHeaderView(this.headerView);
    this.mainCommunicationService.updateSidebarMenu('administration');
    if (this.preference && this.preference.organization && this.preference.organization.id) {
      this.unitId = this.preference.organization.id;
    }
    if (this.payoutAdjustmentData && !this.payoutAdjustmentData.payoutAdjustmentDetails) {
      this.payoutAdjustmentData.payoutAdjustmentDetails = [];
    }
    if (this.payoutType === 'Employee') {
      this.loadEmployeeFromUserMaster();
    } else {
      this.loadDepartmentFromUserMaster();
    }
  }
  openAndSelectPayoutType() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(InsertPayoutDialogComponent, ngbModalOptions);
    this.modalRef.result.then(
      result => {
        if (result && result.entryType && result.data) {
          this.payoutType = result.entryType;
          this.payoutBannerDetails = result.data;
          if (this.payoutType === 'Employee') {
            this.headerOptons.isHeaderForEmployee = true;
            this.headerOptons.employee = this.payoutBannerDetails;
            this.headerOptons.employee.employeeNo = this.payoutBannerDetails.employeeNumber;
            this.headerOptons.remarksListCount = 0;
            this.payoutAdjustmentData.employeeDetail = this.payoutBannerDetails;
            this.payoutAdjustmentData.employeeId = this.payoutBannerDetails.id
              ? this.payoutBannerDetails.id
              : this.payoutBannerDetails.employeeNumber;
            this.payoutAdjustmentData.department = null;
            this.payoutAdjustmentData.departmentId = null;
            this.payoutAdjustmentData.type = 'EMPLOYEE';
          } else {
            this.headerOptons.isHeaderForEmployee = false;
            this.headerOptons.department = this.payoutBannerDetails;
            this.headerOptons.remarksListCount = 0;
            this.payoutAdjustmentData.department = this.payoutBannerDetails;
            this.payoutAdjustmentData.departmentId = this.payoutBannerDetails.id;
            this.payoutAdjustmentData.employeeDetail = null;
            this.payoutAdjustmentData.employeeId = null;
            this.payoutAdjustmentData.type = 'DEPARTMENT';
          }
        }
      },
      () => {
        this.navigateTo('listPage');
      }
    );
  }

  openCommentBlock() {}

  openCommentBoxForsendForApproval() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);

    this.modalRef.result.then(
      result => {
        if (result) {
          if (result.action) {
            const approvedByData: any = result.userComment.createdBy;
            const comment: any = {
              approvedBy: {
                displayName: approvedByData.displayName
                  ? approvedByData.displayName
                  : approvedByData.firstName && approvedByData.lastName
                  ? approvedByData.firstName + ' ' + approvedByData.lastName
                  : approvedByData.firstName,
                employeeNo: approvedByData.employeeNo ? approvedByData.employeeNo : approvedByData.login,
                id: approvedByData.id ? approvedByData.id : null,
                login: approvedByData.login ? approvedByData.login : null
              },
              approvedByDisplayName: approvedByData.displayName
                ? approvedByData.displayName
                : approvedByData.firstName && approvedByData.lastName
                ? approvedByData.firstName + ' ' + approvedByData.lastName
                : approvedByData.firstName,
              approvedByEmployeeNo: approvedByData.employeeNo ? approvedByData.employeeNo : approvedByData.login,
              approvedById: approvedByData.id ? approvedByData.id : '',
              approvedByLogin: approvedByData.login ? approvedByData.login : '',
              approvedDateTime: result.userComment.createdDateTime,
              comments: result.userComment.comment,
              documentType: 'PAYOUT_ADJUSTMENT',
              type: 'Send For Approval'
            };
            this.payoutAdjustmentData.transactionApprovalDetails = [];
            this.payoutAdjustmentData.transactionApprovalDetails.unshift(comment);
            this.sendForApproval();
          }
        }
      },
      () => {}
    );
  }
  search(event) {
    event ? '' : '';
  }
  onDeleteRow(index) {
    if (this.editIndex === undefined) {
      this.payoutAdjustmentData.payoutAdjustmentDetails.splice(index, 1);
      this.calculateGrandTotal();
    }
  }
  onSaveUpdates() {
    // this.isEditActive = false;
  }
  onAddNewRecord() {
    if (this.editIndex === undefined) {
      const newAdjustment = { ...this.newAdjustment };
      newAdjustment.amount = +newAdjustment.amount;
      if (newAdjustment && newAdjustment.month) {
        const month = +newAdjustment.month;
        if (month > 9) {
          newAdjustment.month = '0' + newAdjustment.month;
        }
      }
      this.payoutAdjustmentData.payoutAdjustmentDetails.unshift(newAdjustment);
      this.calculateGrandTotal();
      this.reset();
      this.noRecordFound = false;
    }
  }
  reset() {
    const monthList = this.payoutHelperService.getYearAndMonth();
    this.newAdjustment = new PayoutAdjustmentDetail();
    if (this.newAdjustment && this.newAdjustment.month === '') {
      this.newAdjustment.month = monthList[0].value;
    }
    if (this.newAdjustment && this.newAdjustment.year === '') {
      this.newAdjustment.year = monthList[0].year;
    }
  }
  navigateTo(pageName) {
    switch (pageName) {
      case 'listPage': {
        this.router.navigate(['artha/payout-adjustment'], { replaceUrl: true });
        break;
      }
    }
  }
  clear() {}
  // Update row
  onClickEdit(ind) {
    this.editIndex = ind;
    this.isEditActive = true;
    const rowDetails = this.payoutAdjustmentData.payoutAdjustmentDetails[ind];
    this.editAdjustment = new PayoutAdjustmentDetail();
    this.editAdjustment = { ...rowDetails };
  }
  onClickCancelEdit() {}

  onContraChange() {
    if (this.editAdjustment.contra === 'No') {
      this.editAdjustment.contraEmployeeDetail = undefined;
      this.editAdjustment.contraEmployeeId = undefined;
      this.editAdjustment.contraDepartment = undefined;
    } else {
      if (this.payoutType === 'Employee') {
        this.focusToSelectEmployee();
      } else {
        this.focusToSelectDepartment();
      }
    }
  }
  focusToSelectEmployee() {
    setTimeout(() => {
      this.searchEmployeeContra.focus();
    }, 100);
  }
  focusToSelectDepartment() {
    setTimeout(() => {
      this.searchDepartmentContra.focus();
    }, 100);
  }
  onSelectEmployee(event) {
    if (event) {
      const selectedEmployee = event;
      this.editAdjustment.contraEmployeeDetail = selectedEmployee;
      this.editAdjustment.contraEmployeeId = selectedEmployee.employeeNumber;
    }
  }
  onSelectDepartment(event) {
    if (event) {
      const selectedDepartment = event;
      this.editAdjustment.contraDepartment = selectedDepartment;
    }
  }
  onUpdateCancel() {
    this.editIndex = undefined;
    this.isEditActive = false;
    this.editAdjustment = new PayoutAdjustmentDetail();
  }
  onUpdateDetail() {
    const isPayoutForEmployee = this.payoutType === 'Employee' ? true : false;
    if (this.editAdjustment.amount > 9999999.99) {
      this.jhiAlertService.error('global.messages.response-msg', { msg: `Amount should not be more than-9999999.99` });
      return;
    }
    this.payoutHelperService
      .payoutAdjustmentValidation(this.editAdjustment, isPayoutForEmployee)
      .then(res => {
        if (res) {
          this.editAdjustment.amount = +this.editAdjustment.amount;
          const updatedDetails = { ...this.editAdjustment };
          this.payoutAdjustmentData.payoutAdjustmentDetails[this.editIndex] = updatedDetails;
          this.calculateGrandTotal();
          this.editIndex = undefined;
          this.isEditActive = false;
          this.editAdjustment = new PayoutAdjustmentDetail();
        }
      })
      .catch(err => {
        if (err && err.msg === 'Please select Employee') {
          this.focusToSelectEmployee();
        }
      });
  }
  calculateGrandTotal() {
    this.grandTotal = 0;
    if (
      this.payoutAdjustmentData &&
      this.payoutAdjustmentData.payoutAdjustmentDetails &&
      this.payoutAdjustmentData.payoutAdjustmentDetails.length > 0
    ) {
      let total = 0;
      this.payoutAdjustmentData.payoutAdjustmentDetails.forEach((data: PayoutAdjustmentDetail) => {
        const payoutAdjustment: PayoutAdjustmentDetail = { ...data };
        if (payoutAdjustment.sign === 'NEGATIVE') {
          payoutAdjustment.amount = -payoutAdjustment.amount;
          total = total + payoutAdjustment.amount;
        } else {
          total = total + payoutAdjustment.amount;
        }
        if (total < 0) {
          this.grandTotalSymbol = '-';
        } else {
          this.grandTotalSymbol = '+';
        }
        const noSignTotal = total < 0 ? -total : total;
        this.grandTotal = noSignTotal;
      });
    } else {
      this.grandTotal = 0;
    }
  }

  sendForApproval() {
    this.httpBlockerService.enableHttpBlocker(true);
    const date = new Date();
    this.payoutAdjustmentData.status = 'PENDING_APPROVAL';
    const createdBy = this.stateStorage.getValue('preferences');
    if (createdBy && createdBy.user) {
      // this.payoutAdjustmentData.createdBy = createdBy.user;
      this.payoutAdjustmentData.createdBy = {
        id: createdBy.user.id,
        login: createdBy.user.login,
        displayName: createdBy.user.firstName + ' ' + createdBy.user.lastName
      };
      this.payoutAdjustmentData.createdById = createdBy.user.id;
      this.payoutAdjustmentData.unitCode =
        createdBy && createdBy.organization && createdBy.organization.code ? createdBy.organization.code : '';
    }
    // this.payoutAdjustmentData.createdBy = {
    //   id: 1,
    //   login: 'admin',
    //   displayName: 'Administrator',
    //   employeeNo: 'admin'
    // };
    this.payoutAdjustmentData.netAmount = this.grandTotal;
    this.payoutAdjustmentData.createdDateTime = moment(date).format('YYYY-MM-DDTHH:mm:ss');
    this.payoutAdjustmentService.create(this.payoutAdjustmentData).subscribe(
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.payoutAdjustmentData = new PayoutAdjustmentModel();
        this.navigateTo('listPage');
      },
      (error: any) => {
        this.onError(error);
        this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }
  public setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.patient-header-container').offsetHeight
      ? this.elementRef.nativeElement.querySelector('div.patient-header-container').offsetHeight
      : 56;
    const fixedTabelHeaderHeight = this.elementRef.nativeElement.querySelector('div#fixed-tbl-head').offsetHeight;
    const adaPayoutControls = this.elementRef.nativeElement.querySelector('div.add-payout-controls').offsetHeight;
    const fixedFooter = this.elementRef.nativeElement.querySelector('div#fixed-footer').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedTabelHeaderHeight - fixedFooter - adaPayoutControls - 10;
  }
  private onError(err) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: err && err.error.detail ? err.error.detail : `Server Error !` });
  }

  // ng-select Employee related code
  private loadEmployeeFromUserMaster(): void {
    this.employee$ = concat(
      this.userService
        .search({
          query: `*`,
          size: 20
        })
        .pipe(map((res: any) => res.body)),
      this.employeeInput$.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term =>
          this.departmentService
            .userOrganizationDepartmentMappingSearch({
              query: `organization.id:${this.unitId} AND userMaster.displayName:` + this.searchTermModify.modify(term) + '*'
            })
            .pipe(
              map((res: HttpResponse<any>) => {
                const users: any = [];
                if (res.body && res.body.length > 0) {
                  res.body.forEach(element => {
                    if (element && element.userMaster) {
                      const userAlreadyExist = users.find(user => user.id === element.userMaster.id);
                      if (!userAlreadyExist) {
                        users.push(element.userMaster);
                      }
                    }
                  });
                }
                // const userMaster: any = res.body && res.body.length > 0 && res.body[0].userMaster ? res.body[0].userMaster : [];
                return users;
              })
            )
        ),
        catchError(() => {
          this.onError('serer error');
          return of([]);
        })
      )
    );
  }

  // ng-select for department related code
  private loadDepartmentFromUserMaster(): void {
    this.department$ = concat(
      this.departmentInput$.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term =>
          this.departmentService
            .search({
              limit: 20,
              query: term.trim()
                ? (this.unitId ? `active:true AND organization.id: ${this.unitId}` : '') + ' ' + this.searchTermModify.modify(term)
                : (this.unitId ? `active:true AND organization.id: ${this.unitId}` : '') + ' *'
            })
            .pipe(
              map((res: any) => {
                return res.body;
              })
            )
        ),
        catchError(() => {
          this.onError('serer error');
          return of([]);
        })
      )
    );
  }

  amountChanged() {
    if(this.editAdjustment.amount) {
      const payoutAmountChanged = this.editAdjustment.amount.split('.');
      if(payoutAmountChanged[0].length > 7) {
        this.jhiAlertService.error('arthaApp.payoutAdjustment.validation.payoutAmountValidity');
        this.editAdjustment.amount = undefined;
        this.editAdjustment.amount = null;
        return;
      }
    }
  }

  checkUpdates() {
    if (
      this.newAdjustment.description ||
      this.editAdjustment.description ||
      (this.payoutAdjustmentData.payoutAdjustmentDetails && this.payoutAdjustmentData.payoutAdjustmentDetails.length > 0)
    ) {
      return false;
    } else {
      return true;
    }
  }

  canDeactivate() {
    return Observable.of(this.checkUpdates());
  }
}
