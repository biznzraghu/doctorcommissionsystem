import { Privilege } from './privilege.model';

export class Role {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public active?: boolean,
    public createdBy?: any,
    public creationDate?: any,
    public modifiedBy?: any,
    public modifiedDate?: any,
    public partOf?: string,
    public privileges?: Privilege[],
    public roles?: Role[],
    public createdDatetime?: any
  ) {
    this.active = true;
    this.partOf = '';
    this.privileges = [];
    this.roles = [];
  }
}
