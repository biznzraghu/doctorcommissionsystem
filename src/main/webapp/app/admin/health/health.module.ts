import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArthaSharedModule } from 'app/shared/shared.module';

import { JhiHealthCheckComponent } from './health.component';
import { JhiHealthModalComponent } from './health-modal.component';

import { healthRoute } from './health.route';

@NgModule({
  imports: [ArthaSharedModule, RouterModule.forChild([healthRoute])],
  declarations: [JhiHealthCheckComponent, JhiHealthModalComponent],
  entryComponents: [JhiHealthModalComponent]
})
export class HealthModule {}
