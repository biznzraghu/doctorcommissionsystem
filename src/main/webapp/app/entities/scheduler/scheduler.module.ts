import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArthaSharedModule } from 'app/shared/shared.module';
import { SchedulerComponent, SchedulerFilterPipe, SchedulerRoute, SchedulerService } from './';

@NgModule({
  imports: [ArthaSharedModule, CommonModule, RouterModule.forChild(SchedulerRoute)],
  declarations: [SchedulerComponent, SchedulerFilterPipe],
  entryComponents: [SchedulerComponent],
  providers: [SchedulerService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SchedulerModule {}
