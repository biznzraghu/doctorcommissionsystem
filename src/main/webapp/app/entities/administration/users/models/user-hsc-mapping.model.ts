import { User } from 'app/entities/artha-models/user.model';

export class UserHSCMapping {
  constructor(
    public id?: number,
    public user?: User,
    public healthcareServiceCenter?: any,
    public createdBy?: any,
    public createdDatetime?: any
  ) {}
}
