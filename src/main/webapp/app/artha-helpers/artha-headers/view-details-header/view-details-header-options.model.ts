export interface IViewDetailsHeaderOptions {
  headerName?: string;
  disableEdit?: boolean;
  disableAuditLog?: boolean;
  disableBacktoList?: boolean;
  editPageLink?: string;
  editPrivileges?: string;
  entityname?: string;
  createPrivilege?: string;
  searchURL?: string;
  displayformatSearchTypeheadData?: any;
  inputformatSearchTypeheadData?: any;
  inputPlaceholderText?: string;
  disableCreate?: boolean;
  emitCreateNew?: boolean;
  searchParams?: string;
  module?: string;
  auditEntityType?: string;
  showSearchBox?: boolean;
}

export class ViewDetailsHeaderOptions implements IViewDetailsHeaderOptions {
  constructor(
    public viewHeading?: string,
    public headerName?: string,
    public disableEdit?: boolean,
    public disableAuditLog?: boolean,
    public disableBacktoList?: boolean,
    public editPageLink?: string,
    public editPrivileges?: string,
    public entityname?: string,
    public createPrivilege?: string,
    public searchURL?: string,
    public displayformatSearchTypeheadData?: any,
    public inputformatSearchTypeheadData?: any,
    public inputPlaceholderText?: string,
    public disableCreate?: boolean,
    public emitCreateNew?: boolean,
    public searchParams?: string,
    public module?: string,
    public auditEntityType?: string,
    public showSearchBox?: boolean
  ) {
    this.inputPlaceholderText = 'Search';
    this.showSearchBox = true;
  }
}
