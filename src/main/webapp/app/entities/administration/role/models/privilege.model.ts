import { Feature } from './feature.model';

export class Privilege {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public active?: boolean,
    public displayOrder?: number,
    public feature?: Feature
  ) {
    this.active = true;
  }
}
