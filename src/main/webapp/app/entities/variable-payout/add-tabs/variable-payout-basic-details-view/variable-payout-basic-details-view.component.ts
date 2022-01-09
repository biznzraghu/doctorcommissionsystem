import { Component, OnInit, Input } from '@angular/core';
import { NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ApplicableServicesComponent } from '../applicable-services-modal/applicable-services.component';

@Component({
  selector: 'jhi-variable-payout-basic-details-view',
  templateUrl: './variable-payout-basic-details-view.component.html'
})
export class VariablePayoutBasicDetailsViewComponent implements OnInit {
  @Input() variablePayoutObj;
  dateFormat: any;
  modalRef: NgbModalRef;
  constructor(private modalService: NgbModal) {
    this.dateFormat = 'dd/MM/yyyy';
  }
  ngOnInit() {}

  applicableServicesModal(i) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary about-product-popup'
    };
    this.modalRef = this.modalService.open(ApplicableServicesComponent, ngbModalOptions);
    this.modalRef.componentInstance.stayBenefitServicesListFromParent =
      this.variablePayoutObj &&
      this.variablePayoutObj.lengthOfStayBenefits &&
      this.variablePayoutObj.lengthOfStayBenefits[i] &&
      this.variablePayoutObj.lengthOfStayBenefits[i].stayBenefitServices;
    this.modalRef.result.then(
      res => {
        this.variablePayoutObj.lengthOfStayBenefits[i].stayBenefitServices = res;
      },
      () => {}
    );
  }
}
