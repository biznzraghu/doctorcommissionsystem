import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DepartmentService } from 'app/entities/administration/administration-services/department.service';
import { Department } from 'app/entities/artha-models/department.model';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';

@Component({
  selector: 'jhi-add-custome-department',
  templateUrl: './add-custome-department.component.html'
})
export class AddCustomDepartmentComponent {
  public contraSearch: boolean;
  searchingConsultant = false;
  selectedConsultant: any;
  consultantAddedList: any;
  depName: any;
  department: Department;

  @Input() organization;

  constructor(public activeModal: NgbActiveModal, public departmentService: DepartmentService, public jhiAlertService: JhiAlertService) {
    this.department = new Department();
  }

  process() {
    this.department.organization = this.organization;
    this.department.custom = true;
    this.department.code = this.organization.code;
    this.subscribeToSaveResponse(this.departmentService.create(this.department));
  }

  private subscribeToSaveResponse(result: Observable<Department>) {
    result.subscribe(
      res => this.onSaveSuccess(res),
      (res: HttpErrorResponse) => this.onError(res)
    );
  }

  private onSaveSuccess(result) {
    this.jhiAlertService.success('arthaApp.department-payout.customdepartment');
    this.activeModal.close(result);
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
  }
}
