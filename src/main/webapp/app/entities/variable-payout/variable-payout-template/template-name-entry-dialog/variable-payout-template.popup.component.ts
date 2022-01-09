import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { VariablePayoutTemplate } from 'app/entities/artha-models/variable-payout-template.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { JhiAlertService } from 'ng-jhipster';
import { VariablePayoutService } from '../../variable-payout.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-variable-payout-template-popup',
  templateUrl: './variable-payout-template-popup.component.html'
})
export class VariablePayoutTemplatePopupComponent {
  variablePayoutTemplate: VariablePayoutTemplate;
  constructor(
    public activeModal: NgbActiveModal,
    private router: Router,
    private stateStorage: StateStorageService,
    private jhiAlertService: JhiAlertService,
    private variablePayoutService: VariablePayoutService
  ) {
    this.variablePayoutTemplate = new VariablePayoutTemplate();
  }

  createNewTemplate() {
    const date = new Date();
    this.variablePayoutTemplate.createdDate = moment(date).format('YYYY-MM-DDTHH:mm:ss');
    const createdBy = this.stateStorage.getValue('preferences');
    if (createdBy && createdBy.user) {
      this.variablePayoutTemplate.createdBy = createdBy.user;
    }
    this.saveTemplate();
  }

  saveTemplate() {
    this.variablePayoutService.createVariablePayoutTemplate(this.variablePayoutTemplate).subscribe(
      (data: any) => {
        if (data && data.body && data.body.id) {
          this.router.navigate([`artha/variable-payouts/variable-payouts-template/${data.body.id}/edit`], { replaceUrl: true });
          this.activeModal.dismiss();
        }
      },
      (res: HttpErrorResponse) => {
        this.onError(res.error);
      }
    );
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  close() {
    this.activeModal.dismiss();
    this.router.navigate(['artha/variable-payouts'], { queryParams: { currentTabIndex: 2 }, replaceUrl: true });
  }
}
