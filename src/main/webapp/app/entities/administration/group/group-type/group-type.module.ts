import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GroupTypeService } from './';
import { ArthaSharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [ArthaSharedModule],
  declarations: [],
  entryComponents: [],
  providers: [GroupTypeService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GroupTypeModule {}
