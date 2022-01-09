import { Injectable } from '@angular/core';
import { DepartmentTrendWidgetComponent } from './department-trend-widget.component';
import { DoctorsDepartmentWidgetComponent } from './doctors-department-widget.component';

@Injectable()
// register your component
export class DynamicComponentService {
  public componentMapping = new Map();
  constructor() {
    this.componentMapping.set('DoctorWiseRevenueTrend', DoctorsDepartmentWidgetComponent);
    this.componentMapping.set('DepartmentWiseRevenueTrend', DepartmentTrendWidgetComponent);
  }

  getComponent(componentName: string) {
    return this.componentMapping.get(componentName);
  }
}
