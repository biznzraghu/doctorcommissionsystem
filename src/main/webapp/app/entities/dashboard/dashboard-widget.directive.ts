import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[dashboard-widget]'
})
export class DashboardWidgetDirective {
  constructor(public viewContainerRef: ViewContainerRef) {}
}
