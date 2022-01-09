import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { DepartmentPayoutRoute, DepartmentPayoutDataResolver } from './department-payout.route'; // , PreferencesDataResolver
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { DepartmentPayoutComponent } from './department-payout.component';
import { DepartmentPayoutDialogComponent } from './department-payout-dialog.component';
import { DepartmentPayoutMainComponent } from './department-payout-main.component';
import { DepartmentPayoutTabComponent } from './department-payout-tab-content.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { AddVariablePayoutComponent } from './add-variable-range/add-variable-payout.component';
import { AddConsultantPopupComponent } from './add-consultant/add-consultant-popup.component';
import { DepartmentPayoutExceptionsComponent } from './exception-tab/department-payout-exceptions.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { DepartmentPayoutService } from './department-payout.service';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { AddCustomDepartmentComponent } from 'app/entities/department-payout/add-custome-department/add-custome-department.component';
import { DepartmentPayoutDetailComponent } from './view-details/department-payout-detail.component';
import { DepartmentPayoutDetailMainComponent } from './view-details/department-payout-detail-main.component';
import { DepartmentPayoutExceptionsDetailComponent } from './exception-tab/department-payout-exceptions-detail.component';
import { DepartmentHelperService } from './department-helper.service';
import { AdvanceSearchComponent } from 'app/artha-helpers';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { DepartmentPayoutDeleteComponent } from './department-payout-delete/department-payout-delete.component';
import { TwoDigitDecimaNumberDirective } from './add-variable-range/two-digit-decima-number-directive';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  swipeEasing: false,
  wheelPropagation: true
};
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgSelectModule,
    NgMultiSelectDropDownModule.forRoot(),
    RouterModule.forChild(
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      DepartmentPayoutRoute
    ),
    PerfectScrollbarModule,
    ArthaHelperModule,
    InfiniteScrollModule,
    ArthaSharedModule
  ],
  declarations: [
    DepartmentPayoutComponent,
    DepartmentPayoutDialogComponent,
    DepartmentPayoutMainComponent,
    DepartmentPayoutTabComponent,
    AddVariablePayoutComponent,
    AddConsultantPopupComponent,
    DepartmentPayoutExceptionsComponent,
    AddCustomDepartmentComponent,
    DepartmentPayoutDetailComponent,
    DepartmentPayoutDetailMainComponent,
    DepartmentPayoutExceptionsDetailComponent,
    DepartmentPayoutDeleteComponent,
    TwoDigitDecimaNumberDirective
  ],
  providers: [
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    DepartmentPayoutService,
    DepartmentPayoutDataResolver,
    // PreferencesDataResolver,
    DepartmentHelperService
  ],
  entryComponents: [
    AddVariablePayoutComponent,
    AddConsultantPopupComponent,
    AddCustomDepartmentComponent,
    AdvanceSearchComponent,
    DepartmentPayoutDeleteComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DepartmentPayoutModule {}
