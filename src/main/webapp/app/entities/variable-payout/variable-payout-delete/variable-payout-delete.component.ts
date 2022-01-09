import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    // tslint:disable-next-line:component-selector
    selector: 'variable-payout-delete',
    templateUrl: './variable-payout-delete.component.html'
})
        
export class VariablePayoutDeleteComponent {
    documentNumber: any;
    variablePayoutInformation: any;

    constructor(
        public activeModal: NgbActiveModal
    ) {}

    no() {
        this.activeModal.close(false);
    }

    yes() {
        this.activeModal.close(true);
    }

}