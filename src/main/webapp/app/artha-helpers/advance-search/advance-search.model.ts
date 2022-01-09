export class UserPersonal {
  feature: string;
  subFeature: string;
  module: string;
  settingType: string;
  settingValue = {};
  userName: string;
}

export class InputModel {
  fieldName: string;
  queryName: string;
  isDisplay = false;
  name;
}

export class TypeHeadModel {
  fieldName: string;
  queryName: string;
  isDisplay = false;
  labelName: string;
  name: any;
  tempData: any;
  codeData: any;
  searchData = [];
  url: string;
  resultFormatter: any;
  inputFormat: any;
  displayField: string;
  typeHeadSearchQuery: string;
  typeHeadSearchResultFilter: any;
  sortBasedOn: any;
  constructor() {
    this.tempData = [];
    this.codeData = [];
    this.displayField = 'name';
    this.sortBasedOn = [this.displayField + ',asc'];
  }
}

export class SelectModel {
  fieldName: string;
  queryName: string;
  isDisplay = false;
  name;
  tempData: any;
  translation: boolean;
  optionData = [];
  constructor() {
    this.tempData = [];
  }
}

export class DateModel {
  fieldName: string;
  queryName: string;
  isDisplay = false;
  startDate: any;
  endDate: any;
  isDateModified = false;
  includeTimeInQuery = false;
  constructor() {
    this.startDate = new Date().toISOString().split('T')[0];
    this.endDate = new Date().toISOString().split('T')[0];
  }
}
