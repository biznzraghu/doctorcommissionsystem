import { Component, OnInit, ViewChild, TemplateRef, ElementRef } from '@angular/core';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PayoutAdjustmentModel, MonthList, PayoutAdjustmentDetail } from 'app/entities/artha-models/payout-adjustment.model';
import {
  ArthaDetailHeaderOptions,
  EmployeeDTO,
  DepartmentDTO
} from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { PayoutAdjustmentService } from '../payout-adjustment.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { UserInterface } from 'app/entities/artha-models/user.model';
// import moment from 'moment';
import * as moment from 'moment';
import { PayoutHelperService } from '../payout-helper.service';

@Component({
  selector: 'jhi-detail-payout-adjustment',
  templateUrl: './detail-payout-adjustment.component.html'
})
export class DetailPayoutAdjustmentComponent implements OnInit {
  payoutAdjustmentData = new PayoutAdjustmentModel();
  headerOptons: ArthaDetailHeaderOptions;
  @ViewChild('headerView', { static: false }) headerView: TemplateRef<any>;
  public dateFormat = 'dd/mm/yyyy hh:mm';
  public amountFormat = '2.2-2';
  private modalRef: NgbModalRef;
  public MonthClassMap = MonthList;
  // Perfect Scrollbar Config
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  public grandTotal: number;
  public grandTotalSymbol = '+';
  private preference: any;

  modifiedBy: UserInterface;
  modifiedById: number;
  modifiedDateTime: string;

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;
  constructor(
    private mainCommunicationService: MainCommunicationService,
    private modalService: NgbModal,
    private router: Router,
    private payoutAdjustmentService: PayoutAdjustmentService,
    private httpBlockerService: HttpBlockerService,
    private elementRef: ElementRef,
    private activatedRoute: ActivatedRoute,
    private prifrencesService: PreferenceService,
    private payoutHelperService: PayoutHelperService
  ) {
    this.headerOptons = new ArthaDetailHeaderOptions();
    this.activatedRoute.data.subscribe(data => {
      if (data['payoutAdjustmentData'] && data['payoutAdjustmentData'].body) {
        this.payoutAdjustmentData = data['payoutAdjustmentData'].body;
        // eslint-disable-next-line no-console
        console.log('payoutAdjustmentData --- ', this.payoutAdjustmentData);
        // eslint-disable-next-line no-console
        console.log('payoutAdjustmentDetails --- ', this.payoutAdjustmentData.payoutAdjustmentDetails);

        if (
          this.payoutAdjustmentData &&
          this.payoutAdjustmentData.payoutAdjustmentDetails &&
          this.payoutAdjustmentData.payoutAdjustmentDetails.length > 0
        ) {
          this.calculateGrandTotal();
        }
        this.headerOptons.status = this.payoutAdjustmentData.status ? this.payoutAdjustmentData.status : 'NEW';
        this.headerOptons.showSearchIcon = false;
        this.headerOptons.remarksListCount =
          this.payoutAdjustmentData.transactionApprovalDetails && this.payoutAdjustmentData.transactionApprovalDetails.length > 0
            ? this.payoutAdjustmentData.transactionApprovalDetails.length
            : 0;
        this.headerOptons.createdBy = new EmployeeDTO();
        this.headerOptons.createdBy = this.payoutAdjustmentData.createdBy;
        this.headerOptons.createdDate = this.payoutAdjustmentData.createdDateTime;
        this.headerOptons.documentNo = this.payoutAdjustmentData.documentNumber ? this.payoutAdjustmentData.documentNumber : '-';
        if (this.payoutAdjustmentData.type === 'EMPLOYEE') {
          this.headerOptons.isHeaderForEmployee = true;
          this.headerOptons.employee = new EmployeeDTO();
          this.headerOptons.employee.displayName = this.payoutAdjustmentData.employeeDetail.displayName;
          this.headerOptons.employee.employeeNo = this.payoutAdjustmentData.employeeDetail.employeeNo
            ? this.payoutAdjustmentData.employeeDetail.employeeNo
            : '-';
        } else {
          this.headerOptons.isHeaderForEmployee = false;
          this.headerOptons.department = new DepartmentDTO();
          this.headerOptons.department.name = this.payoutAdjustmentData.department.name;
          this.headerOptons.department.code = this.payoutAdjustmentData.department.code;
        }
      }
    });
    this.preference = this.prifrencesService.currentUser();
    if (this.preference && this.preference.user) {
      this.modifiedBy = this.preference.user;
      this.modifiedById = this.preference.user.id ? this.preference.user.id : undefined;
    }
  }
  ngOnInit() {
    this.mainCommunicationService.updateHeaderView(this.headerView);
    this.mainCommunicationService.updateSidebarMenu('administration');
  }
  public setInfiniteScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.patient-header-container').offsetHeight
      ? this.elementRef.nativeElement.querySelector('div.patient-header-container').offsetHeight
      : 56;
    const fixedTabelHeaderHeight = this.elementRef.nativeElement.querySelector('div#fixed-tbl-head').offsetHeight;
    const fixedFooter = this.elementRef.nativeElement.querySelector('div#fixed-footer').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight - fixedTabelHeaderHeight - fixedFooter - 10;
  }

  public navigateTo(pageName) {
    switch (pageName) {
      case 'listPage': {
        this.router.navigate(['artha/payout-adjustment']);
        break;
      }
    }
  }

  openCommentBlock() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.transctionDetailList = this.payoutAdjustmentData.transactionApprovalDetails;
    this.modalRef.componentInstance.isDisplayMode = true;
    this.modalRef.componentInstance.headerTitle = 'Comments';
    this.modalRef.componentInstance.displayRejectBtn = false;
    this.modalRef.componentInstance.primaryBtnLabel = '';
    this.modalRef.result.then(
      () => {},
      () => {}
    );
  }

  private calculateGrandTotal() {
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

  openUserCommentBlock(status) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.transctionDetailList = this.payoutAdjustmentData.transactionApprovalDetails;
    this.modalRef.componentInstance.isDisplayMode = false;
    this.modalRef.componentInstance.headerTitle = 'Add Comment';
    this.modalRef.componentInstance.displayRejectBtn = false;
    this.modalRef.componentInstance.primaryBtnLabel = status === 'REJECTED' ? 'Reject' : 'Approve';
    this.modalRef.result.then(
      result => {
        if (result) {
          result.action = status;
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
            type: result.action
          };
          // this.payoutHelperService.setComment(comment);
          this.payoutAdjustmentData &&
          this.payoutAdjustmentData.transactionApprovalDetails &&
          this.payoutAdjustmentData.transactionApprovalDetails.length > 0
            ? ''
            : (this.payoutAdjustmentData.transactionApprovalDetails = []);
          // eslint-disable-next-line no-console
          console.log('transactionApprovalDetails --- ', this.payoutAdjustmentData.transactionApprovalDetails);
          this.payoutAdjustmentData.transactionApprovalDetails.unshift(comment);
          this.headerOptons.remarksListCount =
            this.payoutAdjustmentData.transactionApprovalDetails && this.payoutAdjustmentData.transactionApprovalDetails.length > 0
              ? this.payoutAdjustmentData.transactionApprovalDetails.length
              : 0;
          this.updateWorkFlow(status);
        }
      },
      () => {}
    );
  }

  public updateWorkFlow(status) {
    const payoutData = { ...this.payoutAdjustmentData };
    payoutData.status = status;
    payoutData.modifiedBy = this.modifiedBy;
    payoutData.modifiedById = this.modifiedById;
    const date = new Date();
    payoutData.modifiedDateTime = moment(date).format('YYYY-MM-DDTHH:mm:ss');
    this.httpBlockerService.enableHttpBlocker(true);
    this.payoutAdjustmentService.update(payoutData).subscribe(
      () => {
        this.payoutAdjustmentData.status = status;
        this.headerOptons.status = status;
        this.httpBlockerService.enableHttpBlocker(false);
      },
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }
}
