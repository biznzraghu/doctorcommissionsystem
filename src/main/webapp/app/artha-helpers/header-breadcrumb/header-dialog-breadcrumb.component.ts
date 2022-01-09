import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SessionStorageService } from 'ngx-webstorage';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';

@Component({
  selector: 'jhi-header-dialog-breadcrumb',
  templateUrl: './header-dialog-breadcrumb.component.html'
})
export class HeaderDialogBreadcrumbComponent implements OnInit, OnDestroy {
  @Input() routeUrl: string;
  @Input() params = {};

  @ViewChild('headerDialogView', { static: true }) headerView: TemplateRef<any>;

  @Output()
  public onBack = new EventEmitter<any>();

  constructor(
    private mainCommunicationService: MainCommunicationService,
    private router: Router,
    private sessionStorage: SessionStorageService
  ) {}

  ngOnInit() {
    this.mainCommunicationService.updateHeaderView(this.headerView);
  }
  ngOnDestroy() {}

  back() {
    this.onBack.emit(true);
    const sessionValue = this.sessionStorage.retrieve('ledgerDocsLink');
    if (sessionValue != null) {
      const pushingUrl = this.router.url.includes('?rd=y');
      if (pushingUrl) {
        this.router.navigate([sessionValue], { queryParams: this.params, replaceUrl: true });
      } else {
        this.router.navigate([this.routeUrl], { queryParams: this.params, replaceUrl: true });
      }
    } else {
      this.router.navigate([this.routeUrl], { queryParams: this.params, replaceUrl: true });
    }
  }

  isBackEvent(e) {
    const k = e.which || e.keyCode;
    if (k === 32 || k === 13) {
      this.back();
    }
  }
}
