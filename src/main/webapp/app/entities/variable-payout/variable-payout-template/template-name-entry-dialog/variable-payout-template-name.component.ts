import { Component, OnInit } from '@angular/core';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { VariablePayoutTemplatePopupComponent } from './variable-payout-template.popup.component';

@Component({
    selector: 'jhi-variable-payout-template-name',
    templateUrl: './variable-payout-template-name.component.html'
})

export class VariablePayoutTemplateNameComponent implements OnInit {
    modalRef: NgbModalRef;
    constructor(
        private modalService: NgbModal
    ) {

    }

    ngOnInit() {
        this.openNewPage();
    }

    openNewPage() {
        const ngbModalOptions: NgbModalOptions = {
          backdrop: 'static',
          keyboard: true,
          windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
        };
        this.modalRef = this.modalService.open(VariablePayoutTemplatePopupComponent, ngbModalOptions);
        this.modalRef.result.then(
          result => {
            if (result) {
                // result 
            }
          },
          () => {}
        );
      }
}  