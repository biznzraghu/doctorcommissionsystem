import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AthmaDateConverter, DialogService, SearchTermModify } from 'app/artha-helpers';
import {
  ApprovedByDTO,
  ArthaDetailHeaderOptions,
  DepartmentDTO,
  EmployeeDTO
} from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { AccountService } from 'app/core/auth/account.service';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { AddCustomDepartmentComponent } from 'app/entities/department-payout/add-custome-department/add-custome-department.component';
import {
  DepartmentPayout,
  ServiceItemException,
  TransactionApprovalDetails,
  UserDTO
} from 'app/entities/department-payout/department-payout.model';
import { DepartmentPayoutService } from 'app/entities/department-payout/department-payout.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { fromPromise } from 'rxjs/internal-compatibility';
import { Observable } from 'rxjs/Rx';
import { DepartmentService } from '../administration/administration-services/department.service';
import { DepartmentPayoutDeleteComponent } from './department-payout-delete/department-payout-delete.component';

@Component({
  selector: 'jhi-department-payout-main',
  templateUrl: './department-payout-main.component.html'
})
export class DepartmentPayoutMainComponent implements OnInit {
  tabStatus: any;
  smTabDetail = [];
  defaultTabStatusValue: string;
  options: ArthaDetailHeaderOptions;
  depList: any;
  selectedDept: any;
  formDisabled = true;
  deptConsumption: any;
  private dataSubscription: any;
  departmentPayout: DepartmentPayout;
  initdepartmentPayout: DepartmentPayout;
  departmentDTO: DepartmentDTO;
  modalRef: NgbModalRef;
  serviceItemExceptionList: Array<ServiceItemException> = [];
  preferences: Preferences;
  userDTO: UserDTO;
  tabMenu = 'PAYOUTS';
  transactionApprovalDetails: TransactionApprovalDetails;
  approvedByDTO: Array<ApprovedByDTO> = [];
  currentStatus: any;
  modifyPrivileges: Array<string>;
  searchText: string;
  allVersionList: Array<DepartmentPayout>;
  isEditMode: Boolean;
  showAllBtns: Boolean;
  allowToEditDocument: boolean;
  buttonHeading: any;
  isEmployeeSelected: boolean;

  constructor(
    private router: Router,
    public departmentService: DepartmentService,
    public jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private departmentPayoutService: DepartmentPayoutService,
    private searchTermModify: SearchTermModify,
    private dialogService: DialogService,
    private accountService: AccountService,
    private httpBlockerService: HttpBlockerService,
    private dateConverter: AthmaDateConverter
  ) {
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.departmentPayout = data['departmentPayout'];
      this.preferences = data['preferences'];
      this.modifyPrivileges = data['authorities'] ? data['authorities'] : [];
    });
    this.isEditMode = true;
    this.showAllBtns = true;

    if (this.departmentPayout.id === undefined || this.departmentPayout.id === null) {
      this.buttonHeading = 'Save Draft';
    } else {
      this.buttonHeading = 'Update Draft';
    }
    if (this.departmentPayout.id) {
      this.formDisabled = false;
      this.departmentDTO = new DepartmentDTO();
      this.departmentDTO.name = this.departmentPayout.departmentName;
      this.departmentDTO.custom = this.departmentPayout.customDepartment;
      this.setHeaderDetails(this.departmentDTO);
      if (this.departmentPayout.changeRequestStatus !== 'PENDING_APPROVAL') {
        this.currentStatus = 'PENDING_APPROVAL';
      }
      this.isEmployeeSelected = true;
      /* version list starts */
      this.loadVersionList();
      /* version list ends */
    } else {
      this.departmentPayout = new DepartmentPayout();
      this.formDisabled = true;
      this.options = new ArthaDetailHeaderOptions(false, false, null, null, null, null, null, null, null, null, 'true', 0, null);
    }

    // take copy
    const shadowObj = JSON.stringify(this.departmentPayout);
    this.initdepartmentPayout = JSON.parse(shadowObj);
  }

  ngOnInit(): void {
    this.smTabDetail.push({ label: 'DETAILS', value: 'detail' });
    this.smTabDetail.push({ label: 'EXCEPTIONS', value: 'excption' });
    if (this.defaultTabStatusValue) {
      this.tabStatus = { value: this.defaultTabStatusValue };
    } else {
      this.tabStatus = { label: 'DETAILS', value: 'detail' };
    }
  }

  selectTab(status): void {
    this.tabStatus = {};
    this.tabStatus = status;
  }

  onDepartmentSearch(searchItem) {
    this.depList = [];
    if (searchItem !== null && searchItem !== undefined) {
      this.getDepartmentList(searchItem);
    } else {
      this.eventManager.broadcast({ name: 'department', content: 'OK' });
      this.formDisabled = true;
      this.options = new ArthaDetailHeaderOptions(false, false, null, null, null, null, null, null, null, null, 'true', 0, null);
    }
  }

  selectedDepartment(event) {
    const data: any = event;
    this.selectedDept = data;
    this.formDisabled = false;
    if (data.id) {
      this.isEmployeeSelected = true;
      this.getDepartmentById(data.id);
    } else {
      this.setHeaderDetails(this.selectedDept);
      this.options.showSearchIcon = false;
    }
  }

  setHeaderDetails(selectedDept) {
    this.options = new ArthaDetailHeaderOptions();
    this.options.status = this.departmentPayout.changeRequestStatus ? this.departmentPayout.changeRequestStatus : 'NEW';
    this.options.showSearchIcon = false;
    this.options.remarksListCount =
      this.departmentPayout.transactionApprovalDetails && this.departmentPayout.transactionApprovalDetails.length > 0
        ? this.departmentPayout.transactionApprovalDetails.length
        : 0;
    this.options.createdBy = new EmployeeDTO();
    this.options.createdBy = this.departmentPayout.createdBy;
    this.options.createdDate = this.departmentPayout.createdDate;
    this.options.documentNo = null;
    this.options.employee = new EmployeeDTO();
    this.options.employee.displayName = null;
    this.options.employee.employeeNo = null;
    this.options.isHeaderForEmployee = false;
    this.options.department = new DepartmentDTO();
    this.options.department.name = selectedDept.name;
    this.options.department.code = selectedDept.code;
    this.options.department.custom = selectedDept.custom;
    this.options.versionNo = this.departmentPayout.version;
    this.options.showVersionNo = true;
    this.options.approvedBy = [];
    if (this.departmentPayout.transactionApprovalDetails.length > 0) {
      for (let i = 0; i < this.departmentPayout.transactionApprovalDetails.length; i++) {
        this.options.approvedBy.push(this.departmentPayout.transactionApprovalDetails[i].approvedBy);
        this.options.approvedBy[i].onDate = this.departmentPayout.transactionApprovalDetails[i].approvedDateTime;
        this.options.approvedBy[i].status = this.departmentPayout.transactionApprovalDetails[i].type;
      }
    }
  }

  onAddNewDepartment() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary department-payout'
    };
    this.modalRef = this.modalService.open(AddCustomDepartmentComponent, ngbModalOptions);
    this.modalRef.componentInstance.organization = this.preferences.organization;
    this.modalRef.result.then(
      res => {
        const data = res;
        if (data != null && data !== undefined) {
          this.selectedDepartment(data);
        }
      },
      () => {}
    );
  }

  getDepartmentList(term) {
    this.departmentService
      .search({
        // query: 'active:true ' + this.searchTermModify.modify(term)
        query: 'active:true AND organization.id:' + this.preferences.organization.id + ' ' + this.searchTermModify.modify(term)
      })
      .subscribe(
        (res: any) => {
          this.depList = res.body;
        },
        (error: any) => {
          this.onError(error.error);
        }
      );
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  getDepartmentById(id) {
    this.departmentPayoutService
      .search(this.tabMenu, {
        active: true,
        query: 'departmentId :' + id
      })
      .subscribe(
        (res: any) => {
          const data = res.body;
          if (data.length > 0) {
            this.departmentPayout = data[0];
            this.eventManager.broadcast({ name: 'existDepartment', content: data[0] });
            if (this.departmentPayout.changeRequestStatus === 'PENDING_APPROVAL') {
              const params = {};
              params['id'] = this.departmentPayout.id;
              this.router.navigate([`artha/department-payout/department-payout-view`, params], { replaceUrl: true });
            }
          } else {
            this.departmentPayout = new DepartmentPayout();
            this.eventManager.broadcast({ name: 'existDepartment', content: data[0] });
          }
          this.setHeaderDetails(this.selectedDept);
          this.options.showSearchIcon = false;
        },
        (error: any) => {
          this.onError(error.error);
        }
      );
  }

  getException(event) {
    this.serviceItemExceptionList = event;
    this.departmentPayout.serviceItemExceptions = this.serviceItemExceptionList;
  }

  callApi() {
    if (this.departmentPayout.id) {
      const finalObj = JSON.parse(JSON.stringify(this.departmentPayout));

      if (this.isNewFlow()) {
        delete finalObj.id;
        if (finalObj.applicableConsultants) {
          finalObj.applicableConsultants.forEach(dept => {
            delete dept.id;
          });
        }
        if (finalObj.departmentConsumptionMaterialReductions) {
          finalObj.departmentConsumptionMaterialReductions.forEach(stay => {
            delete stay.id;
          });
        }
        if (finalObj.transactionApprovalDetails) {
          finalObj.transactionApprovalDetails.forEach(transaction => {
            delete transaction.id;
          });
        }
        if (finalObj.hscConsumptionMaterialReductions) {
          finalObj.hscConsumptionMaterialReductions.forEach(hsc => {
            delete hsc.id;
          });
        }
        if (finalObj.payoutRanges) {
          finalObj.payoutRanges.forEach(pay => {
            delete pay.id;
          });
        }
        if (finalObj.serviceItemExceptions) {
          finalObj.serviceItemExceptions.forEach(exception => {
            delete exception.id;
          });
        }
      }
      // this.httpBlockerService.enableHttpBlocker(true);
      this.subscribeToUpdateResponse(this.departmentPayoutService.update(finalObj));
    } else {
      // this.httpBlockerService.enableHttpBlocker(true);
      this.departmentPayout.departmentId = this.selectedDept.id;
      this.departmentPayout.departmentName = this.selectedDept.name;
      this.departmentPayout.customDepartment = this.selectedDept.custom;
      this.departmentPayout.createdBy = this.userDTO;
      this.departmentPayout.unitCode = this.preferences && this.preferences.organization && this.preferences.organization.code;
      this.subscribeToSaveResponse(this.departmentPayoutService.create(this.departmentPayout));
    }
  }

  onSuccess(res) {
    if (res.version === 0) {
      this.jhiAlertService.success('arthaApp.department-payout.created');
    } else if (res.changeRequestStatus === 'PENDING_APPROVAL') {
      this.jhiAlertService.success('arthaApp.department-payout.sendforapproval');
    } else if (res.changeRequestStatus === 'APPROVED') {
      this.jhiAlertService.success('arthaApp.department-payout.approved');
    } else if (res.changeRequestStatus === 'REJECTED') {
      this.jhiAlertService.error('arthaApp.department-payout.rejected');
    } else if (res.changeRequestStatus === 'DRAFT') {
      this.jhiAlertService.success('arthaApp.department-payout.draft');
    } else {
      this.jhiAlertService.success('arthaApp.department-payout.updated');
    }
    this.departmentPayout = res;
    // update header data
    this.setHeaderDetails(this.departmentDTO);
    this.loadVersionList();
    // Reset form dirty flags
    // this.resetFormData();
  }

  isNewFlow() {
    let newFlow = false;
    // new flow start when current status is approved / rejected and status should not be in draft or pending for approval
    if (
      this.allowToEditDocument ||
      this.initdepartmentPayout.changeRequestStatus === 'APPROVED' ||
      this.initdepartmentPayout.changeRequestStatus === 'REJECTED'
    ) {
      newFlow = true;
    }
    return newFlow;
  }

  save(action) {
    this.currentStatus = action;

    if (this.preferences.user) {
      this.userDTO = new UserDTO();
      const firstName = this.preferences.user.firstName;
      const lastName = this.preferences.user.lastName;
      this.userDTO.displayName = firstName.concat(' ', lastName);
      this.userDTO.employeeNo = this.preferences.user.employeeNumber;
      this.userDTO.id = this.preferences.user.id;
    }

    if (action === 'DRAFT') {
      this.departmentPayout.changeRequestStatus = this.currentStatus;
      this.callApi();
    } else if (this.currentStatus === 'PENDING_APPROVAL' || this.currentStatus === 'APPROVED' || this.currentStatus === 'REJECTED') {
      // TODO: we need to enable below line while implementing comments
      this.openCommentBlock();
    }
  }

  approveAndReject(comments) {
    this.transactionApprovalDetails = new TransactionApprovalDetails();
    this.transactionApprovalDetails.approvedBy = this.userDTO;
    this.transactionApprovalDetails.approvedByDisplayName = this.userDTO.displayName;
    this.transactionApprovalDetails.approvedByEmployeeNo = this.userDTO.employeeNo;
    this.transactionApprovalDetails.approvedById = this.userDTO.id;
    this.transactionApprovalDetails.comments = comments;
    this.transactionApprovalDetails.type = this.currentStatus;
    this.departmentPayout.transactionApprovalDetails.push(this.transactionApprovalDetails);
    if (!this.departmentPayout.unitCode) {
      this.departmentPayout.unitCode = this.preferences.organization.code;
    }
    this.callApi();
  }

  private subscribeToSaveResponse(result: Observable<DepartmentPayout>) {
    result.subscribe(
      (res: any) => {
        this.departmentDTO = new DepartmentDTO();
        this.departmentDTO.name = res?.departmentName;
        this.departmentDTO.custom = res?.customDepartment;
        this.onSuccess(res);
        // this.httpBlockerService.enableHttpBlocker(false);
      },
      (err: HttpResponse<any>) => {
        this.onError(err);
        // this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }
  private subscribeToUpdateResponse(result: Observable<any>) {
    result.subscribe(
      res => {
        if (this.isNewFlow() && res && res.id) {
          // this.resetFormData();
          // this.httpBlockerService.enableHttpBlocker(false);
          if (res.changeRequestStatus === 'PENDING_APPROVAL') {
            this.jhiAlertService.success('arthaApp.department-payout.sendforapproval');
          } else if (res.changeRequestStatus === 'APPROVED') {
            this.jhiAlertService.success('arthaApp.department-payout.approved');
          } else if (res.changeRequestStatus === 'DRAFT') {
            this.jhiAlertService.success('arthaApp.department-payout.draft');
          } else if (res.changeRequestStatus === 'REJECTED') {
            this.jhiAlertService.error('arthaApp.department-payout.rejected');
          } else {
            this.jhiAlertService.success('arthaApp.department-payout.updated');
          }
          const params = {};
          params['id'] = res.id;
          this.router.navigate(['artha/department-payout/department-payout-edit', params], {
            replaceUrl: true
          });
        } else {
          this.onSuccess(res);
          // this.httpBlockerService.enableHttpBlocker(false);
        }
      },
      (err: HttpResponse<any>) => {
        this.onError(err);
        // this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }

  openCommentBlock() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.transctionDetailList = this.departmentPayout.transactionApprovalDetails;
    if (this.currentStatus === 'PENDING_APPROVAL') {
      this.modalRef.componentInstance.primaryBtnLabel = 'Send for Approval';
    } else if (this.currentStatus === 'REJECTED') {
      this.modalRef.componentInstance.primaryBtnLabel = 'Reject';
    } else {
      this.modalRef.componentInstance.primaryBtnLabel = 'Approve';
    }

    this.modalRef.result.then(
      result => {
        if (result) {
          if (result.action) {
            this.departmentPayout.changeRequestStatus = this.currentStatus;
            this.approveAndReject(result.userComment.comment);
          }
        }
      },
      () => {}
    );
  }

  openViewCommentBlock() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.transctionDetailList = this.departmentPayout.transactionApprovalDetails;
    this.modalRef.componentInstance.isDisplayMode = true;
    this.modalRef.componentInstance.headerTitle = 'Comments';
    this.modalRef.componentInstance.displayRejectBtn = false;
    this.modalRef.componentInstance.primaryBtnLabel = '';
    this.modalRef.result.then(
      () => {},
      () => {}
    );
  }

  cancel() {
    if (JSON.stringify(this.initdepartmentPayout) !== JSON.stringify(this.departmentPayout)) {
      // alert('You have some unsaved changes');
      const modalPromise = this.dialogService.confirm();
      const newObservable = fromPromise(modalPromise);
      newObservable.subscribe(respose => {
        if (respose === true) {
          this.router.navigate(['artha/department-payout'], { replaceUrl: true });
        }
      });
    } else {
      this.router.navigate(['artha/department-payout'], { replaceUrl: true });
    }
  }

  deleteDep() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(DepartmentPayoutDeleteComponent, ngbModalOptions);
    this.modalRef.result.then(
      result => {
        if (result) {
          if (this.departmentPayout.departmentId) {
            this.departmentPayoutService.delete(this.departmentPayout.id).subscribe(() => {
              // this.resetFormData();
              this.cancel();
              this.jhiAlertService.success('global.messages.response-msg', {
                msg: 'Department Payout document : ' + this.departmentPayout.departmentName + ' deleted successfully.'
              });
            });
          }
        }
      },
      () => {}
    );
  }

  /** Search Rule */

  search(searchTerm) {
    if (searchTerm !== undefined) {
      this.searchText = searchTerm;
      if (this.searchText === undefined) {
        this.searchExpectItem(this.searchText);
      } else if (this.searchText.trim().length === 0) {
        this.clearSearch();
      } else if (this.searchText.trim().length >= 3) {
        this.searchExpectItem(this.searchText);
      }
    }
  }

  searchExpectItem(term) {
    const reqObj: any = {
      // query: term + 'AND ' + `variablePayout.id: ${this.departmentPayout.id}` //  AND version: ${this.departmentPayout.version}
    };
  }

  clearSearch() {
    this.searchText = '';
  }

  loadVersionList() {
    if (this.preferences && this.preferences.organization && this.preferences.organization.code) {
      const defultQuery = `unitCode: ${this.preferences.organization.code}`;
      if (this.departmentPayout && this.departmentPayout.startingVersion) {
        const getversionListQry = defultQuery + ` AND startingVersion: ${this.departmentPayout.startingVersion}`;
        this.departmentPayoutService
          .searchByVersionNo({
            query: getversionListQry,
            size: 200
          })
          .subscribe(
            (res: HttpResponse<any>) => {
              this.allVersionList = res.body || [];
              const getVersionList = [];
              res.body.forEach(item => {
                getVersionList.push(item.version);
              });
              if (getVersionList) {
                getVersionList.sort(function(a, b) {
                  return a - b;
                });
              }
              this.options.versionList = getVersionList || [];
              this.checkViewMode();
            },
            (res: HttpErrorResponse) => {
              this.onError(res.error);
            }
          );
      }
    }
  }

  onVersionChange(data) {
    if (JSON.stringify(this.initdepartmentPayout) !== JSON.stringify(this.departmentPayout)) {
      const modalPromise = this.dialogService.confirm();
      const newObservable = fromPromise(modalPromise);
      newObservable.subscribe(respose => {
        if (respose === true) {
          this.switchVersion(data);
        }
      });
    } else {
      this.switchVersion(data);
    }
  }

  switchVersion(data) {
    const dataObj = this.allVersionList.find(item => {
      return item.version === data;
    });
    const shadowDataObj = JSON.parse(JSON.stringify(dataObj));
    this.departmentPayout = shadowDataObj;
    this.setHeaderDetails(this.departmentDTO);
    // this.getPayoutDepartmentById(this.departmentPayout.id);
    this.loadVersionList();

    this.checkViewMode();
    // Reset form dirty flags
  }

  getPayoutDepartmentById(id) {
    if (id) {
      this.departmentPayoutService.find(id).subscribe(depPayout => {
        this.departmentPayout = depPayout.body;
        this.loadVersionList();
      });
    }
  }

  checkViewMode() {
    if (this.options.versionList && this.options.versionList.length) {
      if (
        this.departmentPayout.changeRequestStatus !== 'PENDING_APPROVAL' &&
        this.departmentPayout.changeRequestStatus !== 'APPROVED' &&
        this.departmentPayout.changeRequestStatus !== 'REJECTED' &&
        this.options.versionList[this.options.versionList.length - 1] === this.departmentPayout.version
      ) {
        this.hasAuthority()
          .then(data => {
            if (data) {
              this.isEditMode = true;
            } else {
              this.isEditMode = false;
            }
          })
          .catch(() => {
            this.isEditMode = false;
          });
      } else {
        this.isEditMode = false;
      }

      if (
        (this.departmentPayout.changeRequestStatus === 'APPROVED' || this.departmentPayout.changeRequestStatus === 'REJECTED') &&
        this.options.versionList[this.options.versionList.length - 1] === this.departmentPayout.version
      ) {
        this.allowToEditDocument = true;
      } else {
        this.allowToEditDocument = false;
      }
    }
  }

  private hasAuthority(): Promise<boolean> {
    const auth: Promise<boolean> = this.accountService
      .checkHasAuthority(this.modifyPrivileges)
      .then(data => {
        if (data) {
          return data;
        }
      })
      .catch(() => {
        return false;
      });
    return auth;
  }

  onClickEditDocument() {
    this.hasAuthority()
      .then(data => {
        if (data) {
          const userDTO = new UserDTO();
          if (this.preferences.user) {
            const firstName = this.preferences.user.firstName;
            const lastName = this.preferences.user.lastName;
            userDTO.displayName = firstName.concat(' ', lastName);
            userDTO.employeeNo = this.preferences.user.employeeNumber;
            userDTO.id = this.preferences.user.id;
          }
          this.isEditMode = true;
          this.departmentPayout.changeRequestStatus = 'DRAFT';
          this.departmentPayout.createdDate = this.dateConverter.toDateTimeString(new Date());
          this.departmentPayout.createdBy = userDTO;
          this.options.status = this.departmentPayout.changeRequestStatus;
        } else {
          this.customError('Unauthorized to access this resource');
        }
      })
      .catch(() => {
        this.customError('Unauthorized to access this resource');
      });
  }

  private customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }
}
