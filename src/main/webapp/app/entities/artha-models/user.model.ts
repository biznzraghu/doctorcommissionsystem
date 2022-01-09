export interface UserInterface {
  displayName?: string;
  employeeNo?: string;
  id?: number;
  login?: string;
}
export class User {
  constructor(
    public id?: number,
    public displayName?: string,
    public firstName?: string,
    public lastName?: string,
    public employeeNumber?: string,
    public departmentName?: string,
    public status?: string
  ) {}
}

export interface IUser {
  id?: any;
  login?: string;
  firstName?: string;
  displayName?: string;
  lastName?: string;
  email?: string;
  activated?: Boolean;
  langKey?: string;
  authorities?: any[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  password?: string;
  employeeNo?: string;
  status?: string;
  employeeNumber?: string;
}

export interface ICreatedBy {
  activated?: Boolean;
  authorities?: any[];
  createdBy?: string;
  createdDate?: Date;
  email?: string;
  firstName?: string;
  id?: any;
  imageUrl?: string;
  langKey?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  lastName?: string;
  login?: string;
  displayName?: string;
}
