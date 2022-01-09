import { Organization } from './organization.model';

export interface DepartmentInterface {
  id?: number;
  code?: string;
  name?: string;
  active?: boolean;
  organization?: Organization;
  custom?: boolean;
}
export class Department {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public active?: boolean,
    public organization?: Organization,
    public custom?: boolean
  ) {
    this.active = true;
    this.custom = false;
  }
}
export class DepartmentMasterDTO {
  constructor(public id?: number, public code?: string, public name?: string, public active?: boolean) {}
}
