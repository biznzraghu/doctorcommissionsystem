import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { RoleService } from '../role.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Role } from '../models/role.model';
import { Privilege } from '../models/privilege.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { UtilsHelperService } from 'app/artha-helpers';
@Component({
  selector: 'jhi-new-role',
  templateUrl: './new-role.component.html'
})
export class NewRoleComponent implements OnInit {
  roleForm = this.fb.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
    status: [true, [Validators.required]]
  });
  private route$: Subscription;
  public role: Role;
  constructor(
    private fb: FormBuilder,
    private roleService: RoleService,
    private route: ActivatedRoute,
    private router: Router,
    private stateStorage: StateStorageService,
    private alertService: JhiAlertService,
    private utils: UtilsHelperService
  ) {}

  ngOnInit() {
    this.route$ = this.route.data.subscribe(data => {
      this.role = data['role'];
      if (this.utils.hasPropertyAndValue(this.role, 'id')) {
        this.roleForm.patchValue({
          name: this.role.name,
          description: this.role.description,
          status: this.role.active
        });
      }
    });
  }

  save() {
    const date = new Date();
    this.role = {
      ...this.role,
      name: this.roleForm.get('name').value,
      description: this.roleForm.get('description').value,
      active: this.roleForm.get('status').value,
      createdDatetime: moment(date).format('YYYY-MM-DDTHH:mm:ss')
    };
    if (this.role.id) {
      this.roleService.updateRole(this.role).subscribe(this.onSaveSuccess.bind(this));
      return;
    }
    this.roleService.createNewRole(this.role).subscribe(this.onSaveSuccess.bind(this));
  }

  onPrivilegesAdd(privileges: Privilege[]) {
    this.role.privileges = privileges;
  }

  onRoleAdded(role: Role[]) {
    this.role.roles = role;
  }

  clear() {
    this.router.navigate(['/administrator/role'], { replaceUrl: true });
  }

  private onSaveSuccess(role: Role) {
    if (this.role.id) {
      this.alertService.success('gatewayApp.role.message.success.updated');
    } else {
      this.alertService.success('gatewayApp.role.message.success.created');
    }
    this.role = role;
    window.history.back();
  }
}
