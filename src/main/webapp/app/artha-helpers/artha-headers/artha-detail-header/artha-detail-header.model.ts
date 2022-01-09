export class DepartmentDTO {
  constructor(public name?: string, public code?: any, public custom?: boolean) {
    this.custom = false;
  }
}

export class EmployeeDTO {
  constructor(public displayName?: string, public employeeNumber?: string, public employeeNo?: string, public gender?: string) {}
}

export class ApprovedByDTO extends EmployeeDTO {
  constructor(public status?: string, public onDate?: string) {
    super();
  }
}

export class ArthaDetailHeaderOptions {
  constructor(
    public isHeaderForEmployee?: boolean, // Header For Employee or Department,
    public showSearchIcon?: boolean, // search icon in header
    public employee?: EmployeeDTO,
    public department?: DepartmentDTO,
    public departmentList?: Array<DepartmentDTO>, // Employee Header
    public versionNo?: number, // Department Header
    public createdBy?: EmployeeDTO,
    public createdDate?: string,
    public documentNo?: string, // Employee Header
    public approvedBy?: Array<ApprovedByDTO>,
    public status?: string,
    public remarksListCount?: number,
    public versionList?: Array<any>,
    public showVersionNo?: boolean
  ) {
    //  this.userSearch = false ;
    //  this.isHeaderForEmployee = true ;
    this.status = 'NEW';
    // this.employee = new EmployeeDTO();
    // this.department = new DepartmentDTO();
    this.createdBy = new EmployeeDTO();
  }
}
