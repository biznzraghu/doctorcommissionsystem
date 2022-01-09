import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface, PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';

import { ArthaSharedModule } from 'app/shared/shared.module';
import { ArthaCoreModule } from 'app/core/core.module';
import { ArthaAppRoutingModule } from './app-routing.module';
import { ArthaHomeModule } from './home/home.module';
import { ArthaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { ArthaHelperModule } from './artha-helpers/artha-helper.module';
import { MainCommunicationService } from './layouts/main/main-communication.service';
import { AnotherActiveUserErrorPopupComponent } from './layouts/main/other-user-active-error.modal';
import { NgSelectModule } from '@ng-select/ng-select';
import { NavbarService } from './layouts/navbar/navbar.service';
import { SidebarService } from './layouts/sidebar/sidebar.service';
// Entry Dialog Components
import { AboutDialogComponent } from './layouts/navbar/about-dialog/about-dialog.component';
import { UnitChangeComponent } from './layouts/navbar/dialogs/unit-change-dialog/unit-change-dialog.component';
import { CommentDialogComponent } from './layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { HttpBlockerSpinnerComponent } from './layouts/http-blocker/http-blocker-spinner.component';
import { HttpBlockerService } from './layouts/http-blocker/http-blocker-spinner.service';
import { DynamicComponentService } from './entities/dashboard-widget/dynamic-component.service';
import { DashboardWidgetService } from './entities/dashboard-widget/dashboard-widget.service';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  wheelPropagation: false
};
@NgModule({
  imports: [
    BrowserModule,
    PerfectScrollbarModule,
    ArthaSharedModule,
    ArthaCoreModule,
    ArthaHomeModule,
    ArthaHelperModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ArthaEntityModule,
    ArthaAppRoutingModule,
    NgSelectModule
  ],
  declarations: [
    JhiMainComponent,
    NavbarComponent,
    SidebarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    AboutDialogComponent,
    UnitChangeComponent,
    CommentDialogComponent,
    AnotherActiveUserErrorPopupComponent,
    HttpBlockerSpinnerComponent
  ],
  bootstrap: [JhiMainComponent],
  providers: [
    MainCommunicationService,
    NavbarService,
    SidebarService,
    HttpBlockerService,
    DynamicComponentService,
    DashboardWidgetService,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    }
  ],
  entryComponents: [AboutDialogComponent, UnitChangeComponent, CommentDialogComponent, AnotherActiveUserErrorPopupComponent]
})
export class ArthaAppModule {}
