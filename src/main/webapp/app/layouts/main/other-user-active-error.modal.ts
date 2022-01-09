import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
  template: `
    <div class="modal-body athma-modal-popup">
      <div class="modal-popup-container">
        <div class="d-flex align-items-center modal-popup-body">
          <i class="icon-alert modal-alert-icon" aria-hidden="true"></i>
          <p>Another user is already logged in. To disconnect the active user, click on Force login.</p>
        </div>
        <div class="d-flex justify-content-end modal-popup-footer">
          <button type="button" class="athma-btn athma-btn-primary" (click)="logout()">Force Login</button>
        </div>
      </div>
    </div>
  `,
  selector: 'jhi-main-error-modal'
})
export class AnotherActiveUserErrorPopupComponent {
  constructor(public activeModal: NgbActiveModal) {}

  logout() {
    this.activeModal.dismiss('logout success');
  }
}
