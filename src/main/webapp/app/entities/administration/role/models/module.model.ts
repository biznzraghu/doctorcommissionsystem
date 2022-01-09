export class Module {
  constructor(public id?: number, public code?: string, public name?: string, public active?: boolean, public displayOrder?: number) {
    this.active = true;
  }
}
