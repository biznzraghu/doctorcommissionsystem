export class NewUserRoleMapping {
  unit: any;
  departments: any;
  roles: any = [];
  hscs: any = [];
  roleType: any;
  unitType: any;
  hscType: any;
  deptType: any;

  constructor() {
    this.departments = [];
    this.roles = [];
    this.roleType = [];
    this.hscType = [];
    this.deptType = [];
    this.hscs = [];
  }
}

export class UserHscRoleMapping {
  hscs: any;
  roles: any = [];

  constructor() {
    this.roles = [];
  }
}

export class UserDepartmentRoleMapping {
  departments: any;
  roles: any = [];
  constructor() {
    this.roles = [];
  }
}
