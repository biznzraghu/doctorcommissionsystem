import { Component, Input, OnInit, ComponentFactoryResolver } from '@angular/core';
import { DynamicComponentService } from './dynamic-component.service';

@Component({
  selector: 'jhi-dashboard-widget',
  template: `
    <ndc-dynamic [ndcDynamicComponent]="component" [ndcDynamicInputs]="data" [ndcDynamicOutputs]=""></ndc-dynamic>
  `
})
export class DashboardWidgetComponent implements OnInit {
  component: any;

  @Input() item: any;
  data: any;
  constructor(public _componentFactoryResolver: ComponentFactoryResolver, private dynamicComponentService: DynamicComponentService) {}

  ngOnInit() {
    this.renderComponents();
  }

  renderComponents() {
    this.data = { data: JSON.stringify(this.item) };
    this.component = this.dynamicComponentService.getComponent(this.item.widgetMaster.properties.componentName);
  }
}
