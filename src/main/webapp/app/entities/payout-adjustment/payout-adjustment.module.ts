import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgSelectModule } from '@ng-select/ng-select';
import { PerfectScrollbarConfigInterface, PerfectScrollbarModule, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { PayoutAdjustmentListComponent } from './payout-adjustment-list/payout-adjustment-list.component';
import { payoutAdjustmentRoute, PayoutAdjustmentDataResolver } from './payout-adjustment.route';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { InsertPayoutDialogComponent } from './payout-adjustment-list/insert-payout-entry-dialog/insert-payout-entry-dialog.component';
import { NewPayoutAdjustmentComponent } from './new-payout-adjustment/new-payout-adjustment.component';
import { AddUpdateEntityControlComponent } from './new-payout-adjustment/add-update-entity-control/add-update-entity-control.component';
import { PayoutAdjustmentService } from './payout-adjustment.service';
import { PayoutHelperService } from './payout-helper.service';
import { DetailPayoutAdjustmentComponent } from './detail-payout-adjustment/detail-payout-adjustment.component';
import { AdvanceSearchComponent } from 'app/artha-helpers';
import { Daterangepicker } from 'ng2-daterangepicker';
import { ArthaSharedModule } from 'app/shared/shared.module';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  swipeEasing: false,
  wheelPropagation: true
};

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      payoutAdjustmentRoute
    ),
    InfiniteScrollModule,
    NgSelectModule,
    PerfectScrollbarModule,
    ArthaHelperModule,
    ArthaSharedModule,
    Daterangepicker
  ],
  declarations: [
    PayoutAdjustmentListComponent,
    InsertPayoutDialogComponent,
    NewPayoutAdjustmentComponent,
    AddUpdateEntityControlComponent,
    DetailPayoutAdjustmentComponent
  ],
  entryComponents: [InsertPayoutDialogComponent, AdvanceSearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    PayoutAdjustmentService,
    PayoutAdjustmentDataResolver,
    PayoutHelperService
  ]
})
export class PayoutAdjustmentModule {}
