import { ICreatedBy } from './user.model';
import { Organization } from './organization.model';
import { ServiceItemBenefit, ServiceItemExceptions } from './variable-payout.model';

export class VariablePayoutTemplate {
  constructor(
    public code?: string,
    public createdBy?: ICreatedBy,
    public createdDate?: string,
    public id?: number,
    public lastModifiedBy?: ICreatedBy,
    public organization?: Array<Organization>,
    public serviceItemBenefits?: Array<ServiceItemBenefit>,
    public serviceItemExceptions?: Array<ServiceItemExceptions>,
    public templateName?: string,
    public variablePayouts?: Array<any>,
    public unitCode?: string,
    public status?: Boolean
  ) {
    this.status = true;
  }
}
