import { Injectable } from '@angular/core';

@Injectable()
export class VariableHelperService {
  departmentTypeAhead: any = [
    {
      type: 'typeHead',
      queryName: 'departmentRevenueBenefits.department.name.keyword',
      fieldName: 'department',
      labelName: 'department',
      url: 'api/_search/departments',
      inputFormat: (x: any) => x.name,
      resultFormatter: (x: any) => x.code + ' | ' + x.name,
      displayField: 'name',
      typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
      sortBasedOn: ['displayName.sort,asc']
    }
  ];

  employeeTypeAhead: any = [
    {
      type: 'typeHead',
      queryName: 'employee.displayName.sort',
      fieldName: 'employee',
      labelName: 'employee',
      url: 'api/_search/user-masters',
      inputFormat: (x: any) => x.displayName,
      resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
      displayField: 'displayName',
      typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
      sortBasedOn: ['displayName.sort,asc']
    }
  ];

  createdByTypeAhead: any = [
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
    }
  ];

  approvedByTypeAhead: any = [
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
  ];

  templateTypeAhead: any = [
    {
      type: 'typeHead',
      queryName: 'templateName.sort',
      fieldName: 'templateName',
      labelName: 'templateName',
      url: 'api/_search/service-item-benefit-templates',
      inputFormat: (x: any) => x.templateName,
      resultFormatter: (x: any) => x.templateName,
      displayField: 'templateName',
      typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
      sortBasedOn: ['templateName.sort,asc']
    }
  ];

  lastModifiedTypeAhead = [
    {
      type: 'typeHead',
      queryName: 'lastModifiedBy.firstName.keyword',
      fieldName: 'lastModified-by',
      labelName: 'lastModified-by',
      url: 'api/_search/user-masters',
      inputFormat: (x: any) => x.displayName,
      resultFormatter: (x: any) => x.employeeNumber + ' | ' + x.displayName,
      displayField: 'displayName',
      typeHeadSearchQuery: 'displayName:searchValue OR login:searchValue',
      sortBasedOn: ['displayName.sort,asc']
    }
  ];

  versionTypeAhead = [
    {
      type: 'typeHead',
      queryName: 'version.sort',
      fieldName: 'version',
      labelName: 'version',
      url: 'api/_search/variable-payouts',
      inputFormat: (x: any) => x.version,
      resultFormatter: (x: any) => x.version,
      displayField: 'version',
      typeHeadSearchQuery: 'version:searchValue OR login:searchValue'
      // sortBasedOn: ['version.sort,asc']
    }
  ];

  statusChangeRequestdata: any = [
    { name: 'Pending Approval', value: 'PENDING_APPROVAL' },
    { name: 'Approved', value: 'APPROVED' },
    { name: 'Rejected', value: 'REJECTED' },
    { name: 'Draft', value: 'DRAFT' }
  ];

  statusPayoutdata: any = [
    { name: 'Active', value: 'true' },
    { name: 'InActive', value: 'false' }
  ];

  constructor() {}

  getTypeAheadformElement(index) {
    switch (index) {
      case 0:
        return [this.employeeTypeAhead[0], this.createdByTypeAhead[0]];
        break;
      case 1:
        return [this.employeeTypeAhead[0], this.createdByTypeAhead[0], this.approvedByTypeAhead[0]];
        break;
      case 2:
        return [this.templateTypeAhead[0], this.createdByTypeAhead[0], this.lastModifiedTypeAhead[0]];
        break;
    }
  }

  getSelectFormElement(index, versionList) {
    if (index === 0) {
      return [
        {
          type: 'select',
          queryName: 'status.sort',
          fieldName: 'status',
          optionData: this.statusPayoutdata
        },
        {
          type: 'select',
          queryName: 'version.sort',
          fieldName: 'version',
          optionData: versionList,
          valueTranslation: false
        }
      ];
    } else if (index === 1) {
      return [
        {
          type: 'select',
          queryName: 'changeRequestStatus.sort',
          fieldName: 'status',
          optionData: this.statusChangeRequestdata
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

  getDateElement() {
    return [{ type: 'date', queryName: 'createdDate', fieldName: 'date' }];
  }

  getServiceItemExceptionValueApiBasedOnType(type) {
    const apiObj: any = {};
    if (type === 'Service') {
      apiObj.api = 'api/_search/all-services';
      apiObj.query = `active:true AND (name:`;
    } else if (type === 'ServiceGroup') {
      // apiObj.api = 'api/_search/';
      // apiObj.query = `active:true AND (display:`;
      apiObj.api = 'api/_search/groups';
      apiObj.query = `context:Service_Group AND (name:`;
    } else if (type === 'ItemWithBrand') {
      apiObj.api = 'api/_search/items-with-brand';
      apiObj.query = `active:true AND (name:`;
    } else if (type === 'ItemWithGeneric') {
      apiObj.api = 'api/_search/distinctganericname'; // AR-583 and AR-537
      apiObj.query = `active:true AND (name:`;
    } else if (type === 'Sponsor') {
      apiObj.api = 'api/_search/all-sponsors';
      // apiObj.query = `active:true AND type.code.raw:(prov) AND (name:`;
      apiObj.query = `active:true AND (name:`;
    } else if (type === 'Plan') {
      apiObj.api = 'api/_search/plans';
      apiObj.query = `active:true AND (name:`;
    }
    return apiObj;
  }
}
