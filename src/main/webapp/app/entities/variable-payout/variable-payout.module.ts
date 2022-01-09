import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { variablePayoutRoute, VariablePayoutDataResolver, VariablePayoutTemplateDataResolver } from './variable-payout.route';
import { VariablePayoutService } from './variable-payout.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgSelectModule } from '@ng-select/ng-select';
import { VariableHelperService } from './variable-payout-helper.service';

import {
  VARIABLE_PAYOUT_COMPONENTS,
  VARIABLE_PAYOUT_TAB_COMPONENTS,
  VARIABLE_PAYOUT_RULES_COMPONENTS,
  OTHER_COMPONENTS,
  VARIABLE_PAYOUT_ENTRY_COMPONENTS
} from './index';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { SortablejsModule } from 'ngx-sortablejs';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  swipeEasing: false,
  wheelPropagation: true
};
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgSelectModule,
    NgxMyDatePickerModule.forRoot(),
    NgMultiSelectDropDownModule.forRoot(),
    RouterModule.forChild(variablePayoutRoute),
    SortablejsModule,
    PerfectScrollbarModule,
    InfiniteScrollModule,
    ArthaHelperModule,
    ArthaSharedModule
  ],
  declarations: [
    ...VARIABLE_PAYOUT_COMPONENTS,
    ...VARIABLE_PAYOUT_TAB_COMPONENTS,
    ...VARIABLE_PAYOUT_RULES_COMPONENTS,
    ...OTHER_COMPONENTS
  ],
  entryComponents: [...VARIABLE_PAYOUT_ENTRY_COMPONENTS],
  providers: [
    VariablePayoutService,
    VariablePayoutDataResolver,
    // PreferencesDataResolver,
    VariablePayoutTemplateDataResolver,
    VariableHelperService,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VariablePayoutModule {}
