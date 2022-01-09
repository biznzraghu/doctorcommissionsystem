import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RolePrivilegesSearchComponent } from '../role-privileges-search/role-privileges-search.component';
import { Role } from '../models/role.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-assigned-roles',
  templateUrl: 'assigned-roles.component.html'
})
export class AssignedRolesComponent {
  @Input() public roles: Role[] = [];
  @Output() roleAdded: EventEmitter<Role[]> = new EventEmitter<Role[]>();
  @Input() showAddRoleBtn = true;
  public isEditable = true;
  constructor(private modalService: NgbModal, private alertService: JhiAlertService) {}
  addNewRole(): void {
    this.showRolePrvilegesModal(null);
  }

  private showRolePrvilegesModal(role: Role): void {
    const modalRef = this.modalService.open(RolePrivilegesSearchComponent, {
      windowClass: 'vertical-middle-modal athma-modal-dialog sm primary',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.findDetOfRole = role;
    modalRef.result.then(result => {
      this.assignRole(result);
    });
  }

  assignRole(role: Role): void {
    if (!role) {
      return;
    }
    const has = this.roles.some(_role => role.id !== _role.id);
    if (has) {
      this.alertService.error('error.default');
      return;
    }
    this.roles = [...this.roles, role];
    this.roleAdded.emit(this.roles);
  }

  unAssignRole(idx) {
    this.roles = this.roles.filter((item, index) => index !== idx);
    this.roleAdded.emit(this.roles);
  }

  showPrivileges(role: Role) {
    this.showRolePrvilegesModal(role);
  }
}
