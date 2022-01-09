export class Scheduler {
  constructor(
    public id?: number,
    public instanceName?: string,
    public scheduler?: string,
    public description?: string,
    public expression?: string,
    public repeatInterval?: number,
    public startTime?: number,
    public nextFireTime?: number,
    public previousFireTime?: number,
    public triggerState?: string,
    public nextFireDateTime?: any,
    public serviceId?: string
  ) {}
}
