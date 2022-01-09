export class BaseEntity {
  constructor(public id?: number, public code?: string, public name?: string) {}
}
export const enum MisReportStatus {
  'INPROGRESS',
  'COMPLETED',
  'PENDING',
  'ONHOLD',
  'FAILED'
}

export class MisReport implements BaseEntity {
  constructor(
    public id?: number,
    public scheduleDate?: any,
    public createdDate?: any,
    public createdBy?: any,
    public queryParams?: any,
    public error?: string,
    public status?: string,
    public reportName?: string,
    public duplicate?: boolean
  ) {
    // this.configuration = new MisConfiguration();
    this.status = 'PENDING';
    this.duplicate = true;
  }
}

export class MisConfiguration {
  constructor(
    public databaseType?: string,
    public id?: number,
    public misConfigType?: string,
    public optionalParams?: string,
    public parameters?: ParametersDto[],
    public query?: string
  ) {
    this.databaseType = 'SECONDARY';
    this.parameters = [];
  }
}

export class ParametersDto {
  constructor(
    public condition?: string,
    public defaultvalue?: string,
    public optional?: boolean,
    public optionalExpression?: string,
    public paramName?: string,
    public type?: string
  ) {}
}
