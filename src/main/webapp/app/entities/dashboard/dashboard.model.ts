import { User } from '../artha-models/user.model';
import { DashboardWidget } from '../dashboard-widget';

export class Dashboard {
  constructor(
    public id?: number,
    public name?: string,
    public layout?: string,
    public user?: User,
    public displayOrder?: number,
    public dashboardWidgets?: DashboardWidget[]
  ) {}
}

export class Widget {
  constructor(public customProperties?: any, public customSettings?: any, public userDashboard?: any, public widgetMaster?: any) {
    this.customProperties = {};
    this.customSettings = {};
    this.userDashboard = {};
    this.widgetMaster = {};
  }
}
