import { Injectable } from '@angular/core';

@Injectable()
export class DepartmentHelperService {
  visitTypeData: any = [
    { name: 'Employee', value: 'EMPLOYEE' },
    { name: 'Department', value: 'DEPARTMENT' }
  ];

  statusCheckout: any = [
    { name: 'Pending Approval', value: 'PENDING_APPROVAL' },
    { name: 'Approved', value: 'APPROVED' },
    { name: 'Rejected', value: 'REJECTED' },
    { name: 'Draft', value: 'DRAFT' },
    { name: 'New', value: 'NEW' }
  ];

  statusPayout: any = [
    { name: 'Active', value: true },
    { name: 'InActive', value: false }
  ];

  constructor() {}

  getTypeAheadformElement() {
    return [
      {
        type: 'typeHead',
        queryName: 'departmentName.sort',
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
        queryName: 'transactionApprovalDetails.approvedBy.displayName.keyword',
        fieldName: 'approved-by',
        labelName: 'approved-by',
        url: 'api/_search/user-masters',
        inputFormat: (x: any) => x.displayName,
        resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
        displayField: 'displayName',
        typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
        sortBasedOn: ['displayName.sort,asc']
      }
      // {
      //   type: 'typeHead',
      //   queryName: 'version',
      //   fieldName: 'version',
      //   labelName: 'version',
      //   url: 'api/_search/department-payouts',
      //   inputFormat: (x: any) => x.version,
      //   resultFormatter: (x: any) => x.version,
      //   displayField: 'version',
      //   typeHeadSearchQuery: 'version:searchValue OR login:searchValue'
      //   // sortBasedOn: ['version.sort,asc']
      // }
    ];
  }

  getSelectFormElement(selectedTab, versionList) {
    if (selectedTab === 'PAYOUT') {
      return [
        {
          type: 'select',
          queryName: 'changeRequestStatus',
          fieldName: 'status',
          optionData: this.statusPayout
        },
        {
          type: 'select',
          queryName: 'version.sort',
          fieldName: 'version',
          optionData: versionList,
          valueTranslation: false
        }
      ];
    } else {
      return [
        {
          type: 'select',
          queryName: 'changeRequestStatus',
          fieldName: 'status',
          optionData: this.statusCheckout
        },
        {
          type: 'select',
          queryName: 'version.sort',
          fieldName: 'version',
          optionData: versionList,
          valueTranslation: false
        }
      ];
    }
  }

  getInputFormElement() {
    return [];
  }

  getDateElement() {
    return [{ type: 'date', queryName: 'createdDate', fieldName: 'date' }];
  }
}

/*  "department": "Department",
        "created-by": "Created By",
        "created-date": "Created Date",
        "approved-by": "Approved By",
        "version": "Version",
        "status": "Status" */
