export class ValueSet {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public definition?: string,
    public active?: boolean,
    public source?: string,
    public definingURL?: string,
    public oid?: string,
    public systemURL?: string,
    public systemOID?: string,
    public modifiable?: boolean
  ) {
    this.active = true;
    //  this.modifiable = true;
  }
}
