import { AdvanceSearchComponent } from 'app/artha-helpers';
import { AddRuleControlComponent } from './add-tabs/add-rule-control.component';
import { ApplicableServicesComponent } from './add-tabs/applicable-services-modal/applicable-services.component';
import { CopyComponent } from './add-tabs/copy-modal/copy-popup.component';
import { TemplateComponent } from './add-tabs/template-modal/template-popup.component';
import { VariablePayoutBasicDetailsViewComponent } from './add-tabs/variable-payout-basic-details-view/variable-payout-basic-details-view.component';
import { VariablePayoutBasicDetailsComponent } from './add-tabs/variable-payout-basic-details.component';
import { VariablePayoutExceptionsComponent } from './add-tabs/variable-payout-exceptions.component';
import { AddExceptionsDialogComponent } from './add-tabs/variable-payout-rules-add-exceptions-modal/variable-payout-rules-add-exceptions.component';
import { VariablePayoutRulesComponent } from './add-tabs/variable-payout-rules.component';
import { VariablePayoutUnitMappingComponent } from './add-tabs/variable-payout-unit-mapping.component';
import { VariablePayoutDeleteComponent } from './variable-payout-delete/variable-payout-delete.component';
import { VariablePayoutDialogComponent } from './variable-payout-dialog.component';
import { ChangeRequestsTabListComponent } from './variable-payout-list/variable-payout-list-tabs/change-requests/change-requests-tab.component';
import { PayoutTabListComponent } from './variable-payout-list/variable-payout-list-tabs/payouts/payouts-tab.component';
import { TemplatesTabListComponent } from './variable-payout-list/variable-payout-list-tabs/templates/templates-tab.component';
import { VariablePayoutListComponent } from './variable-payout-list/variable-payout-list.component';
import { VariablePayoutTemplateNameComponent } from './variable-payout-template/template-name-entry-dialog/variable-payout-template-name.component';
import { VariablePayoutTemplatePopupComponent } from './variable-payout-template/template-name-entry-dialog/variable-payout-template.popup.component';
import { VariablePayoutTemplateDialogComponent } from './variable-payout-template/variable-payout-template-dialog.component';

export const VARIABLE_PAYOUT_COMPONENTS = [
  VariablePayoutListComponent,
  VariablePayoutDialogComponent,
  VariablePayoutBasicDetailsComponent,
  VariablePayoutBasicDetailsViewComponent,
  VariablePayoutExceptionsComponent,
  VariablePayoutTemplateDialogComponent,
  VariablePayoutUnitMappingComponent,
  VariablePayoutDeleteComponent
];
export const VARIABLE_PAYOUT_TAB_COMPONENTS = [PayoutTabListComponent, ChangeRequestsTabListComponent, TemplatesTabListComponent];
export const VARIABLE_PAYOUT_RULES_COMPONENTS = [
  AddExceptionsDialogComponent,
  ApplicableServicesComponent,
  AddRuleControlComponent,
  VariablePayoutTemplatePopupComponent,
  VariablePayoutTemplateNameComponent,
  VariablePayoutRulesComponent
];
export const OTHER_COMPONENTS = [CopyComponent, TemplateComponent];
export const VARIABLE_PAYOUT_ENTRY_COMPONENTS = [
  VariablePayoutListComponent,
  VariablePayoutDialogComponent,
  VariablePayoutBasicDetailsComponent,
  VariablePayoutRulesComponent,
  VariablePayoutExceptionsComponent,
  VariablePayoutTemplateDialogComponent,
  VariablePayoutUnitMappingComponent,
  AddExceptionsDialogComponent,
  ApplicableServicesComponent,
  CopyComponent,
  TemplateComponent,
  AdvanceSearchComponent,
  VariablePayoutDeleteComponent,
  VariablePayoutTemplatePopupComponent,
  VariablePayoutTemplateNameComponent
];
