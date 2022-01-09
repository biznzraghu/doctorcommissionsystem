import { Organization } from 'app/entities/artha-models/organization.model';

export class UnitDepartmentModel {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public active?: boolean,
    public custom?: boolean,
    public description?: string,
    public organization?: Organization
  ) {}
}
