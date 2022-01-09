import { ValueSet } from './value-set.model';
import { Organization } from './organization.model';

export class Group {
  constructor(
    public active?: boolean,
    public actual?: boolean,
    public code?: string,
    public context?: string,
    public id?: number,
    public members?: string,
    public name?: string,
    public partOf?: Organization,
    public type?: GroupType
  ) {}
}

export class GroupType {
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
// Administration/group
export class AdministrationGroupMember {
  constructor(public member?: any, public inactive?: boolean) {
    this.inactive = false;
    this.member = {};
  }
}

export class AdministrationGroupType {
  constructor(public id?: number) {}
}

export class AdministrationGroup {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public active?: boolean,
    public actual?: boolean,
    public members?: AdministrationGroupMember[],
    public type?: AdministrationGroupType,
    public partOf?: Organization,
    public context?: string,
    public parent?: AdministrationGroup,
    public modifiable?: boolean,
    public children?: Array<AdministrationGroup>
  ) {
    this.active = true;
    this.actual = true;
    this.members = [];
    this.modifiable = true;
    this.children = new Array<AdministrationGroup>();
  }
}
