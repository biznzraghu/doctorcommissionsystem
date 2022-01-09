import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { User } from 'app/entities/artha-models/user.model';

@Component({
  selector: 'jhi-reset-password-modal',
  templateUrl: './reset-password-dialog.component.html'
})
export class ResetPasswordDialogComponent implements OnInit {
  @Input() user: User;
  newPwdText = false;
  resetPwdForm = this.fb.group({
    newPassword: ['', [Validators.required, Validators.minLength(6)]]
  });

  constructor(private fb: FormBuilder, private activeModal: NgbActiveModal) {}
  ngOnInit() {}

  cancel() {
    this.activeModal.close();
  }

  get newPassword() {
    return this.resetPwdForm.get('newPassword');
  }

  requestResetPassword() {
    // eslint-disable-next-line no-console
    console.log(this.resetPwdForm.value);
  }
}
