export class TabListDTO {
  constructor(public label?: string, public value?: string, public count?: number) {}
}

export class ListHeaderOptions {
  constructor(
    public entityname?: string,
    public disableExport?: boolean,
    public disableCreate?: boolean,
    public disableAuditLog?: boolean,
    public disableSearch?: boolean,
    public disableAdvanceSearch?: boolean,
    public openNew?: boolean,
    public createPrivileges?: Array<string>,
    public displayTabView?: boolean,
    public displayTabViewList?: Array<TabListDTO>,
    public selectedTabIndex?: number,
    public disableImport?: boolean
  ) {}
}
/* new ListHeaderOptions(
                              'entityname: sting for create route link', 
                              'disableExport' : boolean for EXPORT btn disable, 
                              'disableCreate' : boolean for CREATE btn disable,
                              'disableAuditLog' : boolean for AuditLog btn disable,
                              'disableSearch' : boolean for Search disable ,
                              'openNew' : boolean for Checking create new by modal or page disable,
                              'createPrivileges' : string or array for create privilage,
                              'displayTabView' : boolean for Display Tab View or not); */
