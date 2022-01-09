import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { AthmaDateConverter, SearchTermModify } from 'app/artha-helpers';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';
import { IUser, User } from 'app/entities/artha-models/user.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { concat, Observable, of, Subject, Subscription } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { textChangeRangeNewSpan } from 'typescript';
import { DepartmentService } from '../../administration-services/department.service';
import { UserService } from '../../administration-services/user.service';
import { Role } from '../../role/models/role.model';
import { RoleService } from '../../role/role.service';
import { UserOrganizationRoleMapping } from '../models/user-organization-role-mapping.model';
import { UserOrganizationRoleMappingService } from '../user-organization-role-mapping.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-app-user-role-mapping',
  templateUrl: './user-role-mapping.component.html'
})
export class UserRoleMappingComponent implements OnInit, OnDestroy {
  @Input() user: User;
  public userRoleMappings: UserOrganizationRoleMapping[] = [];
  private masterUserRoleMappings: UserOrganizationRoleMapping[] = [];
  userUnitRoleMappings: any[];
  @Input() preferences;
  @Output() roleSelected: EventEmitter<UserOrganizationRoleMapping[]> = new EventEmitter<UserOrganizationRoleMapping[]>();
  @Output() roleDeleted: EventEmitter<UserOrganizationRoleMapping[]> = new EventEmitter<UserOrganizationRoleMapping[]>();
  public unitInput$ = new Subject<string>();
  public unit$: Observable<any[]>;
  public unitLoading;
  public roleInput$ = new Subject<string>();
  public roles$: Observable<Role[]>;
  public roleLoading;
  public selectedRole;
  selectedUnit: any;
  private deletedRoles: UserOrganizationRoleMapping[] = [];
  private userOrgRoleMapping$: Subscription;
  constructor(
    private userOrganizationRoleMappingService: UserOrganizationRoleMappingService,
    private userService: UserService,
    private searchTermModify: SearchTermModify,
    private roleService: RoleService,
    private dateConverter: AthmaDateConverter,
    private destroyHelper: DestroyHelperService,
    public departmentService: DepartmentService,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService
  ) {}

  ngOnInit() {
    this.loadUnits();
    this.loadRoles();
    this.loadDefaultUnits();
    // this.selectedUnit = this.preferences.organization;
    // this.loadUserOrgRoleMapping();
  }

  ngOnDestroy() {
    this.destroyHelper.unsubscribe([this.userOrgRoleMapping$]);
  }
  loadDefaultUnits() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.departmentService
      .userOrganizationDepartmentMappingSearch({
        query: `userMaster.id:${this.user.id}`
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.httpBlockerService.enableHttpBlocker(false);
          const UnitRes = res.body;
          if (UnitRes && UnitRes.length > 0 && UnitRes[0].organization) {
            this.selectedUnit = UnitRes[0].organization;
            this.loadUserOrgRoleMapping();
          }
        },
        () => {
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
  }
  private loadUnits() {
    this.unit$ = concat(
      of([]),
      this.unitInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.unitLoading = true)),
        switchMap((text: string) =>
          this.departmentService
            .userOrganizationDepartmentMappingSearch({
              query: `userMaster.id:${this.user.id} AND ` + (text ? this.searchTermModify.modify(text) : '*'),
              size: 100
            })
            .pipe(
              map((res: HttpResponse<any>) => {
                const units: any = [];
                if (res.body && res.body.length > 0) {
                  res.body.forEach(element => {
                    if (element && element.organization) {
                      if (units.length > 0) {
                        const unitAlreadyExist = units.find(unit => unit.id === element.organization.id);
                        if (!unitAlreadyExist) {
                          units.push(element.organization);
                        }
                      } else {
                        units.push(element.organization);
                      }
                    }
                  });
                }
                // const userMaster: any = res.body && res.body.length > 0 && res.body[0].userMaster ? res.body[0].userMaster : [];
                return units;
              }),
              catchError(() => of([])),
              tap(() => (this.unitLoading = false))
            )
        )
      )
    );
  }

  onUnitChange(ev) {
    if (!ev) {
      return;
    }
    this.userRoleMappings = this.filterRoleByUnit(this.masterUserRoleMappings);
  }

  onRoleSelect(ev) {
    if (!ev) {
      return;
    }
    if (this.userRoleMappings.find(ele => ele.role.id === ev.id)) {
      this.jhiAlertService.error('gatewayApp.user.messages.error.duplicateRule', { rule: ev.name });
      return;
    }
    const has = this.userRoleMappings.some(({ role }) => role.id === this.selectedRole.id);
    if (has) {
      return;
    }
    this.userRoleMappings = [...this.userRoleMappings, this.getRoleByUser()];
    this.roleSelected.emit(this.userRoleMappings);
    this.selectedRole = {} as IUser;
  }

  removeRole(index, role) {
    this.userRoleMappings = this.userRoleMappings.filter((data, idx) => idx !== index);
    this.deletedRoles = [...this.deletedRoles, role];
    this.roleSelected.emit(this.userRoleMappings);
    this.roleDeleted.emit(this.deletedRoles);
  }

  private loadRoles() {
    this.roles$ = concat(
      of([]),
      this.roleInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.roleLoading = true)),
        switchMap((text: string) =>
          this.roleService.searchRoles({ query: `active:true name: ${text ? this.searchTermModify.modify(text) : '*'}` }).pipe(
            catchError(() => of([])),
            tap(() => (this.roleLoading = false))
          )
        )
      )
    );
  }

  private getRoleByUser(): UserOrganizationRoleMapping {
    const objdata = new UserOrganizationRoleMapping();
    objdata.organization = this.selectedUnit;
    objdata.role = this.selectedRole;
    objdata.user = this.user;
    objdata.createdBy = this.preferences.user;
    objdata.createdDatetime = this.dateConverter.toDateTimeString(new Date());
    return objdata;
  }

  private loadUserOrgRoleMapping() {
    if (this.selectedUnit && this.selectedUnit.id) {
      this.userOrgRoleMapping$ = this.userOrganizationRoleMappingService
        .search({
          query: `user.id: ${this.user.id}`
        })
        .pipe(map(data => data.body))
        .subscribe(role => {
          this.masterUserRoleMappings = role;
          this.userRoleMappings = this.filterRoleByUnit(role);
        });
    }
  }

  filterRoleByUnit(userOgrRole: UserOrganizationRoleMapping[]): UserOrganizationRoleMapping[] {
    return userOgrRole.filter(({ organization }) => organization.id === this.selectedUnit.id);
  }
}
