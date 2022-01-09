import { ModuleConfig } from './module';

const enum FeatureType {
  'Master',
  'Transaction',
  'Report',
  'Configuration',
  'Internal',
  'Dashboard'
}
export class Feature {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public active?: boolean,
    public displayOrder?: number,
    public type?: any,
    public module?: ModuleConfig
  ) {
    this.active = true;
  }
}
