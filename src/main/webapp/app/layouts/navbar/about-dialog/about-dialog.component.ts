import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-about-dialog',
  templateUrl: './about-dialog.component.html'
})
export class AboutDialogComponent {
  constructor(public activeModal: NgbActiveModal) {}

  no() {
    this.activeModal.close(false);
  }

  yes() {
    this.activeModal.close(true);
  }
}
