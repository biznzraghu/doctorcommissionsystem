import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {
  DashboardComponent,
  dashboardRoute,
  DashboardService,
  UpdateWidget,
  DashboardHeaderComponent,
  DashboardSettingComponent
} from './';
import { GridsterModule } from 'angular-gridster2';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { DynamicModule } from 'ng-dynamic-component';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
import { NgbModule, NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';

import { NgSelectModule } from '@ng-select/ng-select';
import { ClickOutsideModule } from 'ng-click-outside';
import {
  DashboardWidgetComponent,
  DepartmentTrendWidgetComponent,
  DoctorsDepartmentWidgetComponent,
  WidgetHeaderComponent
} from '../dashboard-widget';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';

const ENTITY_STATES = [...dashboardRoute];

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  wheelPropagation: false
};

@NgModule({
  imports: [
    NgbModule,
    NgbPopoverModule,
    GridsterModule,
    ArthaSharedModule,
    PerfectScrollbarModule,
    ClickOutsideModule,
    // RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
    DynamicModule.forRoot(),
    NgxMyDatePickerModule.forRoot(),
    NgSelectModule,
    ArthaHelperModule,
    RouterModule.forChild(
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      dashboardRoute
    )
  ],
  declarations: [
    DashboardComponent,
    DashboardHeaderComponent,
    WidgetHeaderComponent,
    DashboardWidgetComponent,
    DashboardSettingComponent,
    DoctorsDepartmentWidgetComponent,
    DepartmentTrendWidgetComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  entryComponents: [
    DashboardComponent,
    WidgetHeaderComponent,
    DashboardHeaderComponent,
    DashboardSettingComponent,
    DoctorsDepartmentWidgetComponent,
    DepartmentTrendWidgetComponent
  ],
  providers: [
    DashboardService,
    UpdateWidget,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ]
})
export class DashboardModule {}
