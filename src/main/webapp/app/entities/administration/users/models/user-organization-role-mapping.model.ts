import { Role } from '../../role/models/role.model';
import { User } from 'app/entities/artha-models/user.model';
import { Organization } from 'app/entities/artha-models/organization.model';

export class UserOrganizationRoleMapping {
  constructor(
    public id?: number,
    public role?: Role,
    public user?: User,
    public organization?: Organization,
    public createdBy?: any,
    public createdDatetime?: any
  ) {}
}

export class UserHSCRoleMapping {
  constructor(
    public id?: number,
    public role?: Role,
    public user?: User,
    public healthcareServiceCenter?: any,
    public createdBy?: any,
    public createdDatetime?: any
  ) {}
}
