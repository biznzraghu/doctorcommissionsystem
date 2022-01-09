import { UserDTO } from './variable-payout.model';

export class TransactionApprovalDetailsDTO {
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
  ) {}
}

export class UserCommentModel {
  constructor(public createdBy?: UserDTO, public createdDateTime?: string, public comment?: string) {}
}
