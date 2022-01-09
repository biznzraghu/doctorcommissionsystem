import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { PayoutAdjustmentDetail } from '../artha-models/payout-adjustment.model';
import { JhiAlertService } from 'ng-jhipster';

@Injectable()
export class PayoutHelperService {
  commentList: any[] = [];
  private sharePayoutDataSubject = new BehaviorSubject<any>(null);
  sharePayoutDataTextAction$ = this.sharePayoutDataSubject.asObservable();

  visitTypeData: any = [
    { name: 'Employee', value: 'EMPLOYEE' },
    { name: 'Department', value: 'DEPARTMENT' }
  ];

  statusdata: any = [
    { name: 'Pending Approval', value: 'PENDING_APPROVAL' },
    { name: 'Approved', value: 'APPROVED' },
    { name: 'Rejected', value: 'REJECTED' }
  ];

  constructor(private jhiAlertService: JhiAlertService) {}
  sharePayoutData(insertPayoutData: any) {
    // insertPayoutData either Employee data or Department data
    return new Promise(resolve => {
      this.sharePayoutDataSubject.next(insertPayoutData);
      resolve(true);
    });
  }

  getYearAndMonth(forMonths = 3) {
    const monthNames = [
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December'
    ];
    const today = new Date();
    const last3Months = [];
    const fromMonth = today.getMonth() - 1;
    for (let i = 0; i < forMonths; i++) {
      if (fromMonth - i < 0) {
        // last3Months.push(monthNames[( (fromMonth + 12) - i)] + ' - ' + (today.getFullYear() -1)  );
        last3Months.push({
          name: monthNames[fromMonth + 12 - i],
          value: fromMonth + 12 - i + 1 < 10 ? '0' + (fromMonth + 12 - i + 1) : '' + (fromMonth + 12 - i + 1),
          year: today.getFullYear() - 1
        });
      } else {
        // last3Months.push(monthNames[( fromMonth - i)] + ' - ' + today.getFullYear()  );
        last3Months.push({
          name: monthNames[fromMonth - i],
          value: fromMonth - i + 1 < 10 ? '0' + (fromMonth - i + 1) : '' + (fromMonth - i + 1),
          year: today.getFullYear()
        });
      }
    }
    return last3Months;
  }

  payoutAdjustmentValidation(payoutAdjustmentDetailObj: PayoutAdjustmentDetail, isPayoutForEmployee = false) {
    return new Promise((resolve, reject) => {
      const checkAmount = +payoutAdjustmentDetailObj.amount;
      payoutAdjustmentDetailObj.amount = isNaN(checkAmount) ? payoutAdjustmentDetailObj.amount : checkAmount;
      if (payoutAdjustmentDetailObj.description.length === 0 || payoutAdjustmentDetailObj.description.trim().length === 0) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Description!` });
        reject();
        return;
      }
      if (payoutAdjustmentDetailObj.amount === undefined || payoutAdjustmentDetailObj.amount <= 0) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Amount !` });
        reject();
        return;
      }

      if (typeof payoutAdjustmentDetailObj.amount !== 'number') {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter valid Amount !` });
        reject();
        return;
      }
      if (payoutAdjustmentDetailObj.amount > 9999999.99) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Amount should not be more than-9999999.99` });
        reject();
        return;
      }
      if (!payoutAdjustmentDetailObj.sign) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please select sign !` });
        reject();
        return;
      }
      if (
        payoutAdjustmentDetailObj.contra === 'Yes' &&
        isPayoutForEmployee === true &&
        payoutAdjustmentDetailObj.contraEmployeeDetail === undefined
      ) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please select Employee !` });
        reject({ msg: 'Please select Employee' });
        return;
      }
      if (
        payoutAdjustmentDetailObj.contra === 'Yes' &&
        isPayoutForEmployee === false &&
        payoutAdjustmentDetailObj.contraDepartment === undefined
      ) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: `Please select Department !` });
        reject({ msg: 'Please select Department' });
        return;
      }
      resolve(true);
    });

    /**
     * if(this.dataInstance.description.trim().length === 0) {
          this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Description!` });
      } else {
        if(this.dataInstance.amount <= 0) {
          this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Positive Amount !` });
        }
      }

     */
  }

  setComment(comment: any) {
    const commentData = {
      comments: comment.comment,
      approvedBy: comment.createdBy,
      approvedDateTime: comment.createdBy
    };
    this.commentList.unshift(commentData);
  }

  // show list of comment
  getComments() {
    return this.commentList.length !== 0
      ? this.commentList
      : [
          {
            comments: `Lorem ipsum dolor sit amet, and consectetur adipiscing elit,
      sed do eiusmod tempor consecaa tetur adipiscing elit.
      Lorem ipsum a dolor Zaxasit amet, consectetur that adipiscing elit.`,
            approvedBy: {
              displayName: 'Ramachandra Krishnamurthy'
            },
            approvedDateTime: '12/01/2019 12:29'
          }
        ];
  }

  getTypeAheadformElement() {
    return [
      {
        type: 'typeHead',
        queryName: 'employeeDetail.displayName',
        fieldName: 'employee-details',
        labelName: 'employee-details',
        url: 'api/_search/user-masters',
        inputFormat: (x: any) => x.displayName,
        resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
        displayField: 'displayName',
        typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
        sortBasedOn: ['displayName.sort,asc']
      },
      {
        type: 'typeHead',
        queryName: 'department.name',
        fieldName: 'department',
        labelName: 'department',
        url: 'api/_search/departments',
        inputFormat: (x: any) => x.name,
        resultFormatter: (x: any) => x.code + ' | ' + x.name,
        displayField: 'name',
        typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
        sortBasedOn: ['displayName.sort,asc']
      },
      {
        type: 'typeHead',
        queryName: 'createdBy.displayName',
        fieldName: 'created-by',
        labelName: 'created-by',
        url: 'api/_search/user-masters',
        inputFormat: (x: any) => x.displayName,
        resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
        displayField: 'displayName',
        typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
        sortBasedOn: ['displayName.sort,asc']
      },
      {
        type: 'typeHead',
        queryName: 'transactionApprovalDetails.approvedBy.displayName',
        fieldName: 'approved-by',
        labelName: 'approved-by',
        url: 'api/_search/user-masters',
        inputFormat: (x: any) => x.displayName,
        resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
        displayField: 'displayName',
        typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
        sortBasedOn: ['displayName.sort,asc']
      }
    ];
  }

  getSelectFormElement() {
    return [
      {
        type: 'select',
        queryName: 'type',
        fieldName: 'type',
        optionData: this.visitTypeData
      },
      {
        type: 'select',
        queryName: 'status',
        fieldName: 'status',
        optionData: this.statusdata
      }
    ];
  }

  getInputFormElement() {
    return [
      { queryName: 'documentNumber', labelName: 'Document No', fieldName: 'document-no' },
      { queryName: 'netAmount', labelName: 'net-amount', fieldName: 'net-amount' }
    ];
  }

  getDateElement() {
    return [{ type: 'date', queryName: 'createdDateTime', fieldName: 'date' }];
  }
}
