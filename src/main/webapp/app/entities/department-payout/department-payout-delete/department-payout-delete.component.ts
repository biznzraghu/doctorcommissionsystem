import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'department-payout-delete',
  templateUrl: './department-payout-delete.component.html'
})
export class DepartmentPayoutDeleteComponent {
  documentNumber: any;

  constructor(public activeModal: NgbActiveModal) {}

  no() {
    this.activeModal.close(false);
  }

  yes() {
    this.activeModal.close(true);
  }
}
