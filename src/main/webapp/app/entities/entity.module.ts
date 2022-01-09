import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EntityRoutingModule } from './entity.route';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
import { SortablejsModule } from 'ngx-sortablejs';
// import { PreferencesDataResolver } from '../layouts/navbar/navbar.route';

@NgModule({
  imports: [
    EntityRoutingModule,
    ArthaHelperModule,
    SortablejsModule.forRoot({ animation: 150 }),
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  providers: [
    // PreferencesDataResolver
  ]
})
export class ArthaEntityModule {}
