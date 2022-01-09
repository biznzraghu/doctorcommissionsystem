import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { AddExceptionsDialogComponent } from './../variable-payout-rules-add-exceptions-modal/variable-payout-rules-add-exceptions.component';
import { VariablePayoutService } from 'app/entities/variable-payout/variable-payout.service';

@Component({
    selector: 'jhi-template',
    templateUrl: './template-popup.component.html'
})

export class TemplateComponent implements OnInit{ 
    @Input() templateInformation;
    templateInfo: any;
    modalRef: NgbModalRef;
    serviceItemBenefits: any;

    public config: PerfectScrollbarConfigInterface = {
        minScrollbarLength: 50
    };

    @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

    constructor(
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
        private variablePayoutService: VariablePayoutService
    ) {
  
    }

    ngOnInit() {
        this.templateInfo = this.templateInformation;
        this.getRulesDetails(this.templateInformation);
    }

    getRulesDetails(templateInfo) {
      const reqObj = {
        query: `planTemplate.id:${templateInfo.id}`
      };
      this.variablePayoutService.getServiceItemBenefitsByPayoutId(reqObj).subscribe((res: any) => {
      this.serviceItemBenefits = res && res.body ? res.body : [];
      });
    }

    cancel() {
        this.activeModal.dismiss();
    }

    onAddExceptionsModal(rule) {
        const ngbModalOptions: NgbModalOptions = {
          backdrop: 'static',
          keyboard: true,
          windowClass: 'athma-modal-dialog vertical-middle-modal md primary about-product-popup'
        };
        this.modalRef = this.modalService.open(AddExceptionsDialogComponent, ngbModalOptions);
        this.modalRef.componentInstance.isEdit = false;
        if (rule && rule.exceptionSponsor && rule.exceptionSponsor.applicable) {
          this.modalRef.componentInstance.SponsorType = rule.exceptionSponsor.applicable ? 'applicableSponsor' : 'exemptedSponsor';
        }
    
        if (rule && rule.exceptionSponsor && rule.exceptionSponsor.sponsorType) {
          this.modalRef.componentInstance.sponsorTypeTags = rule.exceptionSponsor.sponsorType;
        }
    
        if (rule && rule.exceptionSponsor && rule.exceptionSponsor.plans) {
          this.modalRef.componentInstance.planLists = rule.exceptionSponsor.plans;
        }
    
        if (rule && rule.onDeathIncentive !== undefined) {
          this.modalRef.componentInstance.onDeathIncentive = rule.onDeathIncentive;
        }
    
        if (rule && rule.serviceItemExceptions) {
          this.modalRef.componentInstance.servicesList = rule.serviceItemExceptions;
        }
    
        this.modalRef.result.then(
          result => {
            if (result) {
              // For use
            }
          },
          () => {}
        );
      }
}