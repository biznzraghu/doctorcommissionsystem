import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { SearchTermModify, UtilsHelperService } from 'app/artha-helpers';
import { ArthaDetailHeaderOptions } from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { Department } from 'app/entities/artha-models/department.model';
import { IUser } from 'app/entities/artha-models/user.model';
import { JhiAlertService } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';
import { combineLatest, concat, iif, Observable, of, Subject, Subscription } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, finalize, map, switchMap, tap } from 'rxjs/operators';
import { ResetPasswordDialogComponent } from '../..';
import { DepartmentService } from '../../administration-services/department.service';
import { UserService } from '../../administration-services/user.service';
import { Role } from '../../role/models/role.model';
import { UserOrganizationRoleMappingService } from '../user-organization-role-mapping.service';
import { UserOrganizationRoleMapping } from '../models/user-organization-role-mapping.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';

@Component({
  selector: 'jhi-user-detail',
  templateUrl: './user-detail.component.html'
})
export class UserDetailComponent implements OnInit, OnDestroy {
  private user$: Subscription;
  public user: IUser;
  public options: ArthaDetailHeaderOptions;
  public isImageSelected = false;
  public imgSource: any;
  public roleInput$ = new Subject<string>();
  public roles$: Observable<Role[]>;
  public unitInput$ = new Subject<string>();
  public unit$: Observable<Role[]>;
  public roleLoading = false;
  public selectedUnit: any;
  public unitLoading = false;
  public department$: Observable<Department[]>;
  public departmentInput$ = new Subject<string>();
  public selectedDepartment: Department[];
  public deptLoading = false;
  public unitDepartments: any[] = [];
  public tabs = [
    {
      name: 'Basic details',
      code: 'BASIC_DETAILS',
      active: true
    },
    {
      name: 'Unit & Department',
      code: 'UNIT_AND_DEPARTMENT',
      active: false
    }
  ];
  public activeTab = this.tabs[0];
  userForm = this.fb.group({
    imgSrc: [{ value: '', disabled: true }, [Validators.required]],
    imgSrc2: [{ value: '', disabled: true }, [Validators.required]],
    employeeNumber: [{ value: '', disabled: true }, [Validators.required]],
    displayName: [{ value: '', disabled: true }, [Validators.required]],
    firstName: [{ value: '', disabled: true }, [Validators.required]],
    lastName: [{ value: '', disabled: true }, [Validators.required]],
    email: [{ value: '', disabled: true }, [Validators.required]],
    status: [{ value: '', disabled: true }, [Validators.required]]
  });
  unitDeptHoveredIndex = null;
  unitDeptEditable = false;
  public rowIdx: number = null;
  public preferences: any;
  private unitDepartmentsSave$: Subscription;
  private selectedRoles: UserOrganizationRoleMapping[] = [];
  private deletedRoles: UserOrganizationRoleMapping[] = [];
  private allowedFilesType: string[] = ['image/png', 'image/jpeg', 'image/jpg'];
  public status: boolean;
  public predicate: any;
  public reverse: any;
  page = 0;
  private deleteUserOrgDept$: Subscription;
  private findUserOrgDept$: Subscription;
  constructor(
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private searchTermModify: SearchTermModify,
    private modalService: NgbModal,
    private deptService: DepartmentService,
    private userService: UserService,
    private alertService: JhiAlertService,
    private router: Router,
    private sessionStorageService: SessionStorageService,
    private userOrganizationRoleMappingService: UserOrganizationRoleMappingService,
    private utilsHelperService: UtilsHelperService,
    private httpBlockerService: HttpBlockerService,
    private destroyHelper: DestroyHelperService
  ) {
    this.user$ = this.activatedRoute.data.subscribe(data => {
      [this.user] = data['user'];
      if (this.user.status === 'Active') {
        this.status = true;
      } else {
        this.status = false;
      }
      this.updateForm(this.user);
    });
    this.reverse = true;
    this.predicate = 'organization.name.keyword';
    this.preferences = this.sessionStorageService.retrieve('preferences');
  }

  ngOnInit() {
    this.options = new ArthaDetailHeaderOptions();
    this.loadUnits();
    this.loadDepartments();
    this.loadUnitDept();
  }

  ngOnDestroy() {
    this.destroyHelper.unsubscribe([this.deleteUserOrgDept$]);
  }

  onTabChange(tab) {
    this.activeTab = tab;
    this.tabs = this.tabs.map(_tab => {
      if (_tab.code === tab.code) {
        _tab.active = true;
      } else {
        _tab.active = false;
      }
      return _tab;
    });
  }

  onFotoUpload({ target }) {
    const { files } = target;
    if (files && files[0]) {
      const file_ = files[0];
      const fileSize = file_.size / 1024 / 1024;
      if (fileSize > 2) {
        this.alertService.error('gatewayApp.user.messages.error.largerImage', { fileName: file_.name });
        return;
      }
      const typeFound = this.allowedFilesType.find(fileExt => fileExt === files[0].type);
      if (typeFound) {
        const reader = new FileReader();
        reader.onload = ev => {
          this.isImageSelected = true;
          this.imgSource = ev.target.result;
        };
        reader.readAsDataURL(files[0]);
      } else {
        const error = { description: 'Please select png, jpg or jpeg file!' };
        this.onError(error);
      }
    }
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset() {
    // this.page = 0;
    this.loadUnitDept();
  }

  save() {}

  reserPwd() {}

  updateForm(user: IUser) {
    this.userForm.patchValue({
      imgSrc: '',
      imgSrc2: '',
      employeeNumber: user.employeeNumber,
      displayName: user.displayName,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      status: user.status
    });
  }

  trackByFn(item: Role) {
    return item.id;
  }

  cancel() {
    this.router.navigate([`/administrator/users`]);
  }

  openResetPwdPop(user: IUser) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    const modalRef = this.modalService.open(ResetPasswordDialogComponent, ngbModalOptions);
    modalRef.componentInstance.user = user;
    modalRef.result.then(
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      unitRes => {},
      () => {}
    );
  }

  onSave() {
    const body: any[] = this.unitDepartments.filter(dept => !dept.id);
    const roles: UserOrganizationRoleMapping[] = this.selectedRoles.filter(_role => !_role.id);
    const deletedRoles = this.deletedRoles.filter(_role => _role.id);
    if (this.utilsHelperService.isEmpty(body) && this.utilsHelperService.isEmpty(roles) && this.utilsHelperService.isEmpty(deletedRoles)) {
      this.alertService.error('gatewayApp.user.messages.error.noNewMapping');
      return;
    }
    const isEmptyDept = this.utilsHelperService.isEmpty(body)
      ? true
      : body.some(_unitDept => this.utilsHelperService.isEmpty(_unitDept.department));
    this.httpBlockerService.enableHttpBlocker(true);
    const roleObservables: any[] = this.getRoleObservable(roles);
    const deletedRoleObservables: any[] = this.getDeleteRoleObsevable(deletedRoles);
    this.unitDepartmentsSave$ = combineLatest([
      ...roleObservables,
      ...deletedRoleObservables,
      iif(() => !isEmptyDept, this.userService.createUserOrganizationDepartmentMapping(body), of([]))
    ]).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.alertService.success('gatewayApp.user.messages.success.create');
        this.deletedRoles = [];
        this.router.navigate([`/administrator/users`]);
      },
      (err: HttpResponse<any>) => {
        this.onError(err);
        this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }

  private onError(error) {
    this.alertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  editUnitDept(idx) {
    this.unitDeptEditable = this.rowIdx === idx ? !this.unitDeptEditable : true;
    this.rowIdx = idx;
    this.selectedUnit = this.unitDepartments[idx].organization;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  unEdit(userDept, idx) {
    this.selectedUnit = undefined;
  }

  onDeptSelectInRow(event, idx) {
    if (!event) {
      return;
    }
    const has = this.unitDepartments[idx].department.some(dept => dept.id === event.id);
    if (has) {
      this.alertService.error('gatewayApp.user.messages.error.duplicateDept', { department: event.name });
      return;
    }
    this.unitDepartments[idx].department = [...this.unitDepartments[idx].department, event];
  }

  removeSelectedDept(idx, deptIdx) {
    this.unitDepartments[idx].department = this.unitDepartments[idx].department.filter((item, index) => index !== deptIdx);
  }

  saveEditableData(unitDept) {
    this.selectedUnit = undefined;
    if (!unitDept.id) {
      return;
    }
    if (this.utilsHelperService.isEmpty(unitDept.department)) {
      this.alertService.error('gatewayApp.user.messages.error.emptyDept');
      this.findUserOrgDept$ = this.userService.findUserOrgDept(unitDept.id).subscribe((newUnitDept: any) => {
        this.unitDepartments = this.unitDepartments.map(_unitDept => {
          if (_unitDept.id === unitDept.id) {
            _unitDept = newUnitDept;
          }
          return _unitDept;
        });
      });
      return;
    }
    const body = this.unitDepartments.find(({ id }) => id === unitDept.id);
    if (body) {
      this.unitDepartmentsSave$ = this.userService.updateUserOrganizationDepartmentMapping(body).subscribe(() => {
        this.alertService.success('gatewayApp.user.messages.success.update');
      });
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onUnitChange(ev) {
    if (!ev) {
      this.selectedUnit = {};
    }
    this.selectedDepartment = [];
  }

  onRoleDelete(roles) {
    this.deletedRoles = roles;
  }

  private loadUnits() {
    this.unit$ = concat(
      of([]),
      this.unitInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.unitLoading = true)),
        switchMap((text: string) =>
          this.userService.getOrganization({ query: `${text ? this.searchTermModify.modify(text) : '*'}` }).pipe(
            catchError(() => of([])),
            tap(() => (this.unitLoading = false))
          )
        )
      )
    );
  }

  private loadDepartments() {
    this.department$ = concat(
      of([]),
      this.departmentInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.deptLoading = true)),
        switchMap((text: string) =>
          this.deptService
            .searchDepartment({
              query: `organization.code:${this.selectedUnit.code} AND ${text ? this.searchTermModify.modify(text) : '*'}`,
              sort: ['name,asc']
            })
            .pipe(
              map(res => res.body),
              catchError(() => of([])),
              tap(() => (this.deptLoading = false))
            )
        )
      )
    );
  }

  addUserDepartment() {
    if (this.selectedUnit === undefined || this.selectedUnit === null || this.selectedUnit === '') {
      this.alertService.error('gatewayApp.user.messages.error.validateUnit');
    } else if (this.selectedDepartment.length === 0) {
      this.alertService.error('gatewayApp.user.messages.error.validatePayoutDepartment');
    } else {
      const has = this.unitDepartments.some(ele => ele.organization.id === this.selectedUnit.id);
      if (has) {
        this.alertService.error('gatewayApp.user.messages.error.duplicateOrg', { organization: this.selectedUnit.name });
        return;
      }
      // const preferences = this.sessionStorage.retrieve('preferences');
      const userDept = {
        organization: this.selectedUnit,
        department: this.selectedDepartment,
        id: null,
        userMaster: this.user // preferences.user
      };
      this.unitDepartments = [...this.unitDepartments, userDept];
      this.selectedDepartment = [];
      this.selectedUnit = {};
    }
  }

  onRoleSelected(role) {
    this.selectedRoles = role;
  }
  public onStatusChange() {
    if (this.status) {
      this.user.status = 'Active';
    } else {
      this.user.status = 'Inactive';
    }
    this.httpBlockerService.enableHttpBlocker(true);
    this.userService.update(this.user).subscribe(
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.alertService.success('gatewayApp.user.messages.success.userUpdate');
      },
      (err: HttpResponse<any>) => {
        this.onError(err);
        this.status = !this.status;
        if (this.status) {
          this.user.status = 'Active';
        } else {
          this.user.status = 'Inactive';
        }
        this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }

  deleteUnitDept(id) {
    this.httpBlockerService.enableHttpBlocker(true);
    this.deleteUserOrgDept$ = this.userService
      .deleteUserOrgDept(id)
      .pipe(finalize(() => this.httpBlockerService.enableHttpBlocker(false)))
      .subscribe(res => {
        this.alertService.success('gatewayApp.user.messages.success.deleteUserOrgept');
        this.loadUnitDept();
      });
  }

  private loadUnitDept() {
    this.userService
      .getUserOrgDeptSearch({ query: `userMaster.id:${this.user.id}`, sort: this.sort() })
      .pipe(map((res: HttpResponse<any>) => res.body))
      .subscribe(data => {
        this.unitDepartments = data;
      });
  }

  private getRoleObservable(roles): any[] {
    if (this.utilsHelperService.isEmpty(roles)) {
      return [of([])];
    }
    return roles.map(role => {
      return this.userOrganizationRoleMappingService.create(role);
    });
  }

  private getDeleteRoleObsevable(roles: UserOrganizationRoleMapping[]): any[] {
    if (this.utilsHelperService.isEmpty(roles)) {
      return [of([])];
    }
    return roles.map(role => {
      return this.userOrganizationRoleMappingService.delete(role.id);
    });
  }
}
