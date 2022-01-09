import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Department } from 'app/entities/artha-models/department.model';

@Component({
  selector: 'jhi-department-dialog',
  templateUrl: './department-dialog.component.html'
})
export class DepartmentDialogComponent {
  department: Department = {
    code: null,
    name: '',
    active: true
  };
  isEdit: boolean;
  constructor(public activeModal: NgbActiveModal) {}
  no() {
    this.activeModal.close(false);
  }

  yes() {
    this.activeModal.close(true);
  }

  save() {
    this.activeModal.close(this.department);
  }
}
