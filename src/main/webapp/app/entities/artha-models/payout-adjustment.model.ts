import { UserInterface } from './user.model';
import { DepartmentInterface, DepartmentMasterDTO } from './department.model';
import { TransactionApprovalDetailsDTO } from './comment.model';
export enum MonthList {
  '01' = 'January',
  '02' = 'February',
  '03' = 'March',
  '04' = 'April',
  '05' = 'May',
  '06' = 'June',
  '07' = 'July',
  '08' = 'August',
  '09' = 'September',
  '010' = 'October',
  '011' = 'November',
  '012' = 'December'
}

export interface PayoutAdjustmentDetailInterface {
  id?: number;
  description?: string;
  year?: string;
  month?: string;
  sign?: string;
  amount?: number;
  contraEmployeeId?: number;
  contraEmployeeDetail?: UserInterface;
  contraDepartment?: DepartmentMasterDTO;
  contra?: string; // Temp field for front-end only
}

export class PayoutAdjustmentDetail {
  constructor(
    public id?: number,
    public description?: string,
    public year?: string,
    public month?: string,
    public sign?: string,
    public amount?: any,
    public contra?: string, // Temp field for front-end only
    public contraEmployeeId?: number,
    public contraEmployeeDetail?: UserInterface,
    public contraDepartment?: DepartmentMasterDTO
  ) {
    this.contra = 'No';
    this.year = '';
    this.month = '';
    // this.sign = 'POSITIVE';
  }
}
export interface TransactionApprovalInterface {
  approvedBy?: UserInterface;
  approvedByDisplayName: string;
  approvedByEmployeeNo: string;
  approvedById: number;
  approvedByLogin: string;
  approvedDateTime: string;
  documentType: string;
  id: number;
  type: string;
}

export class PayoutAdjustmentModel {
  constructor(
    public id?: number,
    public documentNumber?: string,
    public unitCode?: string,
    public type?: string, // EMPLOYEE or DEPARTMENT
    public employeeId?: number,
    public employeeDetail?: UserInterface,
    public departmentId?: number,
    public department?: DepartmentInterface,
    public netAmount?: number,
    public createdById?: number,
    public createdBy?: UserInterface,
    public createdDateTime?: string,
    public status?: string,
    public modifiedById?: number,
    public modifiedBy?: UserInterface,
    public modifiedDateTime?: string,
    public payoutAdjustmentDetails?: Array<PayoutAdjustmentDetailInterface>,
    public transactionApprovalDetails?: Array<TransactionApprovalDetailsDTO>
  ) {}
}
