export interface ServiceItemValue {
  code: string;
  name: string;
}
export class ServiceItemException {
  constructor(public exceptionType?: string, public id?: number, public level?: string, public value?: ServiceItemValue) {
    this.value = {
      code: '',
      name: ''
    };
  }
}
