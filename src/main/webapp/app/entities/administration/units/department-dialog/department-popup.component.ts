import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService } from 'ng-jhipster';
import { DepartmentService } from '../../administration-services/department.service';
import { UnitDepartmentModel } from '../department.model';

@Component({
  selector: 'jhi-department-popup',
  templateUrl: './department-popup.component.html'
})
export class DepartmentPopupComponent {
  @Input() title;
  @Input() department: UnitDepartmentModel;

  constructor(
    public activeModal: NgbActiveModal,
    private deptService: DepartmentService,
    public jhiAlertService: JhiAlertService,
    private httpBlockerService: HttpBlockerService
  ) {}

  process() {
    this.department.active = this.department.active ? this.department.active : true;
    this.httpBlockerService.enableHttpBlocker(true);
    if (this.title === 'Update') {
      this.deptService.update(this.department).subscribe(
        (res: any) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.activeModal.close(res);
        },
        error => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.onError(error.json);
        }
      );
    } else {
      this.department.custom = true;
      this.department.code = `${this.department.organization.code}-${this.department.code}`;
      this.deptService.create(this.department).subscribe(
        (res: any) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.activeModal.close(res);
        },
        error => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.onError(error.json);
        }
      );
    }
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
