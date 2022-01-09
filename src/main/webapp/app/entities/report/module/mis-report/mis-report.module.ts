import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MisReportService, MisReportComponent, misReportRoute } from './';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { ArthaSharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [
    PerfectScrollbarModule, 
    InfiniteScrollModule, 
    ArthaHelperModule,
    ArthaSharedModule,
    RouterModule.forChild(misReportRoute)
  ],
  declarations: [MisReportComponent],
  entryComponents: [MisReportComponent],
  exports: [MisReportComponent],
  providers: [MisReportService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MisReportModule {}
