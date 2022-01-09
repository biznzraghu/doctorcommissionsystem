import { Group } from './group.model';
import { ValueSet } from './value-set.model';
import { Organization } from './organization.model';
import { DepartmentInterface } from './department.model';
import { ValueSetCode } from './value-set-code.model';
import { Plan } from './plan.model';
import { TransactionApprovalDetailsDTO } from './comment.model';
import { ServiceItemValue } from './service-item-exception.model';
export class VariablePayout {
  constructor(
    public changeRequestStatus?: string,
    public commencementDate?: string,
    public contractEndDate?: string,
    public createdBy?: UserDTO,
    public approvedBy?: UserDTO,
    public createdDate?: string,
    public currentVersion?: number,
    public departmentRevenueBenefits?: Array<DepartmentRevenueBenefit>,
    public employee?: EmployeeDTO,
    public employeeId?: number,
    public id?: number,
    public lengthOfStayBenefits?: Array<LengthOfStayBenefit>,
    public maxPayoutAmount?: number,
    public minAssuredAmount?: any,
    public minAssuredValidityDate?: string,
    public remarks?: string,
    public serviceItemBenefitTemplates?: Array<ServiceItemBenefitTemplate>,
    // public serviceItemBenefits?: Array<ServiceItemBenefit>,
    public startingVersion?: number,
    public status?: boolean,
    public unitCode?: string,
    public uploadContract?: string,
    public version?: number,
    public variablePayoutBaseId?: number,
    public variablePayoutId?: any,
    public serviceItemExceptions?: Array<ServiceItemExceptions>,
    public transactionApprovalDetails?: Array<TransactionApprovalDetailsDTO>
  ) {
    this.departmentRevenueBenefits = new Array<DepartmentRevenueBenefit>();
    this.status = true;
    this.lengthOfStayBenefits = new Array<LengthOfStayBenefit>();
    this.currentVersion = 1;
    this.startingVersion = 1;
    this.version = 1;
    this.minAssuredAmount = 0;
    this.maxPayoutAmount = 0;
    // this.serviceItemBenefits = new Array<ServiceItemBenefit>();
    this.serviceItemExceptions = new Array<ServiceItemExceptions>();
    this.serviceItemBenefitTemplates = new Array<ServiceItemBenefitTemplate>();
    this.transactionApprovalDetails = new Array<TransactionApprovalDetailsDTO>();
  }
}

export class UserDTO {
  constructor(
    public firstName?: string,
    public lastName?: string,
    public displayName?: string,
    public employeeNo?: string,
    public id?: number,
    public login?: string
  ) {}
}
export class EmployeeDTO {
  constructor(
    public firstName?: string,
    public lastName?: string,
    public displayName?: string,
    public employeeNo?: string,
    public gender?: string,
    public status?: string,
    public id?: number
  ) {}
}
export class DepartmentRevenueBenefit {
  constructor(
    public currentVersion?: number,
    public department?: Department,
    public id?: number,
    public payoutPercentage?: any,
    public revenueBenefitType?: string,
    public startingVersion?: number,
    public upperLimit?: any,
    public variablePayout?: VariablePayout
  ) {
    this.currentVersion = 1;
    this.startingVersion = 1;
  }
}

export class LengthOfStayBenefit {
  constructor(
    public currentVersion?: number,
    public days?: any,
    public department?: Department,
    public id?: number,
    public lengthOfStayCriteria?: string,
    public perDayPayoutAmount?: any,
    public startingVersion?: number,
    public stayBenefitServices?: Array<StayBenefitService>,
    public variablePayout?: VariablePayout
  ) {
    this.stayBenefitServices = new Array<StayBenefitService>();
    this.currentVersion = 1;
    this.startingVersion = 1;
  }
}
export class ExceptionSponsor {
  constructor(public applicable?: boolean, public id?: number, public plans?: Array<Plan>, public sponsorType?: Array<ValueSet>) {}
}
export class ServiceItemExceptions {
  constructor(public exceptionType?: string, public id?: number, public level?: string, public value?: ServiceItemValue) {}
}
export class CodeNameDto {
  constructor(public code?: string, public name?: string) {}
}
export class ServiceItemBenefit {
  constructor(
    public amount?: any,
    public applicableOn?: string,
    public applicableSponsor?: number,
    public beneficiary?: string,
    public components?: CodeNameDto,
    public department?: Array<DepartmentInterface>,
    // public department?: number,
    public templateValueDisplay?: string, //  this key is only for front-end display purpose use
    public exemptedSponsor?: number,
    public exceptionSponsor?: ExceptionSponsor,
    public groupKey?: string,
    public id?: number,
    public itemCategory?: ItemCategory,
    public itemException?: number,
    public itemGroup?: ItemGroup,
    public materialAmount?: string,
    public maxQuantity?: number,
    public minQuantity?: number,
    public onDeathIncentive?: boolean,
    public packageCategory?: string,
    public invoiceValue?: string,
    public packageMaster?: PackageMaster,
    public patientCategory?: string,
    public paymentMode?: string,
    public paymentValue?: number,
    public planTemplate?: ServiceItemBenefitTemplate,
    public priorityOrder?: number,
    public ruleKeyObjects?: string,
    public rule_key?: RuleKeyDTO,
    public serviceException?: number,
    public serviceGroup?: Array<Group>,
    public serviceItemExceptions?: Array<ServiceItemExceptions>,
    public serviceMaster?: ServiceMaster,
    public serviceType?: ServiceType,
    // public tariffClass?: string,
    public tariffClass?: Array<ValueSetCode>,
    public type?: string,
    public typeKey?: string,
    public variablePayout?: VariablePayout,
    public variablePayoutBaseId?: number,
    // public visitType?: string,
    public visitType?: Array<string>,
    public visitTypeTariffClassKey?: string,
    public validTo?: any
  ) {
    this.applicableOn = 'NET';
    this.patientCategory = 'CASH_CREDIT';
    this.materialAmount = 'SALE';
  }
}

export class Department {
  constructor(
    public active?: boolean,
    public code?: string,
    public id?: number,
    public name?: string,
    public custom?: boolean,
    public organization?: Organization
  ) {}
}

export class StayBenefitService {
  constructor(
    public id?: number,
    public lengthOfStayBenefit?: LengthOfStayBenefit,
    public serviceCode?: string,
    public serviceId?: number,
    public serviceName?: string,
    public stayBenefitId?: number
  ) {}
}

export class ItemCategory {
  constructor(
    public active?: boolean,
    public code?: string,
    public description?: string,
    public group?: boolean,
    public id?: number,
    public partOf?: ItemCategory
  ) {}
}

export class ItemGroup {
  constructor(
    public active?: boolean,
    public code?: string,
    public comments?: string,
    public definition?: string,
    public display?: string,
    public displayOrder?: number,
    public id?: number,
    public level?: string,
    public source?: string,
    public valueSet?: ValueSet
  ) {}
}

export class PackageMaster {
  constructor(
    public abbreviation?: string,
    public active?: boolean,
    public billingGroup?: Group,
    public code?: string,
    public comments?: string,
    public duration?: number,
    public durationUnit?: string,
    public endDate?: string,
    public financialGroup?: Group,
    public id?: number,
    public mainProcedure?: ServiceMaster,
    public name?: string,
    public numberOfAllowedVisit?: number,
    public packageCategory?: string,
    public partOf?: PackageMaster,
    public planValidation?: boolean,
    public serviceGroup?: Group,
    public startDate?: string,
    public template?: boolean,
    public visitType?: string
  ) {}
}

export class ServiceItemBenefitTemplate {
  constructor(
    public id?: number,
    public serviceItemBenefits?: Array<ServiceItemBenefit>,
    public templateName?: string,
    public code?: string,
    public createdBy?: UserDTO,
    public createdDate?: string,
    public lastModifiedBy?: UserDTO,
    public variablePayouts?: Array<VariablePayout>,
    public organization?: Array<Organization>
  ) {}
}

export class RuleKeyDTO {
  constructor(
    public department?: string,
    public serviceType?: string,
    public tariffClass?: string,
    public type?: string,
    public typeBeneficiary?: string,
    public visitType?: string
  ) {}
}

export class ServiceMaster {
  constructor(
    public active?: boolean,
    public autoProcess?: boolean,
    public category?: ServiceCategory,
    public code?: string,
    public consentRequired?: boolean,
    public id?: number,
    public individuallyOrderable?: boolean,
    public insuranceExempted?: boolean,
    public minReOrderDuration?: string,
    public name?: string,
    public profileService?: boolean,
    public serviceDuration?: string,
    public serviceGroup?: Group,
    public serviceType?: ServiceType,
    public shortName?: string
  ) {}
}

export class ServiceType {
  constructor(
    public active?: boolean,
    public code?: string,
    public comments?: string,
    public definition?: string,
    public display?: string,
    public displayOrder?: number,
    public id?: number,
    public level?: string,
    public source?: string,
    public valueSet?: ValueSet
  ) {}
}
export class ServiceCategory {
  constructor(
    public active?: boolean,
    public code?: string,
    public comments?: string,
    public definition?: string,
    public display?: string,
    public displayOrder?: number,
    public id?: number,
    public level?: string,
    public source?: string,
    public valueSet?: ValueSet
  ) {}
}
