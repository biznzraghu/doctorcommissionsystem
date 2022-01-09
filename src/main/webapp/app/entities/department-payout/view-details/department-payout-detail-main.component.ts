import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {
  ApprovedByDTO,
  ArthaDetailHeaderOptions,
  DepartmentDTO,
  EmployeeDTO
} from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import {
  DepartmentPayout,
  ServiceItemException,
  TransactionApprovalDetails,
  UserDTO
} from 'app/entities/department-payout/department-payout.model';
import { DepartmentPayoutService } from 'app/entities/department-payout/department-payout.service';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';

@Component({
  selector: 'jhi-department-payout-detail-main',
  templateUrl: './department-payout-detail-main.component.html'
})
export class DepartmentPayoutDetailMainComponent implements OnInit {
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
  departmentDTO: DepartmentDTO;
  modalRef: NgbModalRef;
  serviceItemExceptionList: Array<ServiceItemException> = [];
  preferences: Preferences;
  userDTO: UserDTO;
  approvedByDTO: Array<ApprovedByDTO> = [];
  transactionApprovalDetails: TransactionApprovalDetails;
  currentStatus: any;

  // createPrivileges = ['1001101'];
  // rejectPrivileges = ['110101101'];
  modifyPrivileges: Array<String>;
  constructor(
    private router: Router,
    public jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private departmentPayoutService: DepartmentPayoutService
  ) {
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.departmentPayout = data['departmentPayout'];
      this.preferences = data['preferences'];
      this.modifyPrivileges = ['102100', '102102'];
    });

    if (this.departmentPayout.id) {
      if (this.departmentPayout.changeRequestStatus !== 'PENDING_APPROVAL' && this.departmentPayout.changeRequestStatus !== 'REJECTED') {
        this.currentStatus = 'PENDING_APPROVAL';
      } else {
        this.currentStatus = this.departmentPayout.changeRequestStatus;
      }
      this.formDisabled = false;
      this.departmentDTO = new DepartmentDTO();
      this.departmentDTO.name = this.departmentPayout.departmentName;
      this.departmentDTO.custom = this.departmentPayout.customDepartment;
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
      this.options.department.name = this.departmentDTO.name;
      this.options.department.code = this.departmentDTO.code;
      this.options.versionNo = this.departmentPayout.version;
      this.options.approvedBy = [];
      if (this.departmentPayout.transactionApprovalDetails.length > 0) {
        for (let i = 0; i < this.departmentPayout.transactionApprovalDetails.length; i++) {
          this.options.approvedBy.push(this.departmentPayout.transactionApprovalDetails[i].approvedBy);
          this.options.approvedBy[i].onDate = this.departmentPayout.transactionApprovalDetails[i].approvedDateTime;
          this.options.approvedBy[i].status = this.departmentPayout.transactionApprovalDetails[i].type;
        }
      }
    } else {
      this.formDisabled = true;
      this.options = new ArthaDetailHeaderOptions(false, false, null, null, null, null, null, null, null, null, 'true', 0, null);
    }
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

  getException(event) {
    this.serviceItemExceptionList = event;
    this.departmentPayout.serviceItemExceptions = this.serviceItemExceptionList;
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

    if (this.departmentPayout.departmentId) {
      this.openCommentBlock();
    }
  }

  private subscribeToSaveResponse(result: Observable<DepartmentPayout>) {
    result.subscribe(
      res => this.onSaveSuccess(res),
      (res: HttpErrorResponse) => this.onError(res)
    );
  }

  private onSaveSuccess(result) {
    if (result.changeRequestStatus === 'APPROVED') {
      this.jhiAlertService.success('arthaApp.department-payout.approved');
    } else if (result.changeRequestStatus === 'REJECTED') {
      this.jhiAlertService.success('arthaApp.department-payout.rejected');
    } else {
      this.jhiAlertService.success('arthaApp.department-payout.updated');
    }
    this.cancel();
  }

  cancel() {
    this.router.navigate(['artha/department-payout'], { replaceUrl: true });
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
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
    this.modalRef.result.then(() => {});
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

  approveAndReject(comments) {
    this.transactionApprovalDetails = new TransactionApprovalDetails();
    this.transactionApprovalDetails.approvedBy = this.userDTO;
    this.transactionApprovalDetails.approvedByDisplayName = this.userDTO.displayName;
    this.transactionApprovalDetails.approvedByEmployeeNo = this.userDTO.employeeNo;
    this.transactionApprovalDetails.approvedById = this.userDTO.id;
    this.transactionApprovalDetails.comments = comments;
    this.transactionApprovalDetails.type = this.currentStatus;
    this.departmentPayout.transactionApprovalDetails.push(this.transactionApprovalDetails);
    this.subscribeToSaveResponse(this.departmentPayoutService.update(this.departmentPayout));
  }
}
