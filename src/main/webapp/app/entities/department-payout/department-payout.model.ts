export class DepartmentPayout {
  constructor(
    public allMaterials?: boolean,
    public applicableConsultants?: Array<ApplicableConsultant>,
    public applicableInvoice?: string,
    public changeRequestStatus?: string,
    public consumables?: boolean,
    public createdBy?: UserDTO,
    public createdDate?: any,
    public departmentConsumptionMaterialReductions?: Array<DepartmentConsumptionMaterialReduction>,
    public departmentId?: number,
    public departmentName?: string,
    public deptConsumption?: boolean,
    public drugs?: boolean,
    public hscConsumption?: boolean,
    public hscConsumptionMaterialReductions?: Array<HscConsumptionMaterialReduction>,
    public id?: number,
    public implants?: boolean,
    public latest?: boolean,
    public netGross?: string,
    public onCostSale?: string,
    public payoutRanges?: Array<PayoutRange>,
    public status?: boolean,
    public unitCode?: string,
    public version?: any,
    public visitType?: string,
    public approvedBy?: any, // will import from detail header
    public serviceItemExceptions?: Array<ServiceItemException>,
    public transactionApprovalDetails?: Array<TransactionApprovalDetails>,
    public customDepartment?: boolean,
    public startingVersion?: any
  ) {
    this.applicableConsultants = [];
    this.departmentConsumptionMaterialReductions = [];
    this.hscConsumptionMaterialReductions = [];
    this.payoutRanges = [];
    this.consumables = false;
    this.allMaterials = false;
    this.status = true;
    this.deptConsumption = false;
    this.hscConsumption = false;
    this.serviceItemExceptions = [];
    this.transactionApprovalDetails = [];
    this.customDepartment = false;
    this.netGross = 'NET';
    this.onCostSale = 'COST';
    this.applicableInvoice = 'ALL_INVOICES';
  }
}
export class ApplicableConsultant {
  constructor(
    public id?: number,
    public include?: boolean,
    public userMasterId?: number,
    public displayName?: string,
    public employeeCode?: string
  ) {}
}
export class UserDTO {
  constructor(public displayName?: string, public employeeNo?: string, public id?: number, public login?: number) {}
}

export class DepartmentConsumptionMaterialReduction {
  constructor(public departmentId?: number, public id?: number, public departmentName?: string) {}
}
export class HscConsumptionMaterialReduction {
  constructor(public hscId?: number, public id?: number, public hscName?: string) {}
}
export class PayoutRange {
  constructor(public fromAmount?: number, public id?: number, public percentage?: number, public toAmount?: number) {}
}

export class ServiceItemException {
  constructor(public exceptionType?: string, public id?: number, public level?: string, public value?: ServiceItemValue) {
    this.value = {
      code: '',
      name: ''
    };
  }
}

export interface ServiceItemValue {
  code: string;
  name: string;
}
export class TransactionApprovalDetails {
  constructor(
    public approvedBy?: UserDTO,
    public approvedByDisplayName?: string,
    public approvedByEmployeeNo?: string,
    public approvedById?: number,
    public approvedByLogin?: string,
    public approvedDateTime?: any,
    public documentType?: string,
    public comments?: any,
    public id?: number,
    public type?: string
  ) {
    this.documentType = 'DEPARTMENT_PAYOUT';
  }
}
