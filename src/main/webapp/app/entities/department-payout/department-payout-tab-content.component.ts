import { Component } from '@angular/core';

@Component({
  selector: 'jhi-department-payout-tab-content',
  template: `
    <div style="min-height: calc(100vh - 200px);">
      <ng-content></ng-content>
    </div>
  `,
  styles: [
    `
      :host {
        display: block;
      }
    `
  ]
})
export class DepartmentPayoutTabComponent {}
