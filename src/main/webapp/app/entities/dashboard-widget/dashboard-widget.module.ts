import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbModule, NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { GridsterModule } from 'angular-gridster2';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { ClickOutsideModule } from 'ng-click-outside';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { DashboardWidgetService } from './';
import { DynamicComponentService } from './dynamic-component.service';

const ENTITY_STATES = [
  // ...dashboardWidgetRoute,
];

@NgModule({
  imports: [
    NgbModule,
    NgbPopoverModule,
    ArthaSharedModule,
    GridsterModule,
    FormsModule,
    ClickOutsideModule,
    NgxMyDatePickerModule.forRoot(),
    RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
    PerfectScrollbarModule,
    NgSelectModule
  ],
  declarations: [],

  entryComponents: [],
  providers: [DashboardWidgetService, DynamicComponentService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardWidgetModule {}
