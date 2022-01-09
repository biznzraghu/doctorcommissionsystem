import { ValueSet } from './value-set.model';
export class ValueSetCode {
  constructor(
    public id?: number,
    public code?: string,
    public display?: string,
    public active?: boolean,
    public definition?: string,
    public source?: string,
    public level?: string,
    public comments?: string,
    public displayOrder?: number,
    public valueSet?: ValueSet
  ) {
    this.active = true;
  }
}
