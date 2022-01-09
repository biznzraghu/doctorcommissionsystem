import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
import { PerfectScrollbarConfigInterface, PerfectScrollbarModule, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { IndexUtilDialogComponent, ResetPasswordDialogComponent, UserDetailComponent, UserRoleMappingComponent } from '.';
import { administrationRoute } from './administration.route';
import { DepartmentDialogComponent } from './departments/department-dialog/department-dialog.component';
import { DepartmentsComponent } from './departments/departments.component';
import * as RoleConstant from './role/role.constant';
import { UnitDialogComponent } from './units/unit-dialog/unit-dialog.component';
import { UnitsComponent } from './units/units.component';
import { UsersComponent } from './users/users.component';
import { ValueSetCreateComponent } from './value-set/value-set-create/value-set-create.component';
import { ValueSetDetailComponent } from './value-set/value-set-detail/value-set-detail.component';
import { ValueSetComponent } from './value-set/value-set.component';
import { GroupListComponent, GroupDialogComponent, GroupDetailComponent } from './group/group.index';
import { GroupTypeModule } from './group/group-type/group-type.module';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { UnitDetailComponent } from './units/unit-detail.component';
import { DepartmentPopupComponent } from './units/department-dialog/department-popup.component';
const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  swipeEasing: false,
  wheelPropagation: true
};
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ArthaSharedModule,
    RouterModule.forChild(
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      administrationRoute
    ),
    InfiniteScrollModule,
    PerfectScrollbarModule,
    ArthaHelperModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgxMyDatePickerModule.forRoot(),
    GroupTypeModule
  ],
  declarations: [
    ValueSetComponent,
    ValueSetCreateComponent,
    ValueSetDetailComponent,
    UnitsComponent,
    UsersComponent,
    UnitDialogComponent,
    UnitDetailComponent,
    DepartmentsComponent,
    DepartmentDialogComponent,
    ResetPasswordDialogComponent,
    UserDetailComponent,
    IndexUtilDialogComponent,
    ...RoleConstant.ROLES_COMPONENTS,
    GroupListComponent,
    GroupDialogComponent,
    GroupDetailComponent,
    UserRoleMappingComponent,
    DepartmentPopupComponent
  ],
  providers: [
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ],
  entryComponents: [
    UnitDialogComponent,
    DepartmentDialogComponent,
    ResetPasswordDialogComponent,
    DepartmentPopupComponent,
    ...RoleConstant.ROLES_ENTRY_COMPONENTS
  ]
})
export class AdministrationModule {}
