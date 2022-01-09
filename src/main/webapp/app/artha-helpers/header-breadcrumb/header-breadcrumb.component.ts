import { ActivatedRoute } from '@angular/router';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';

@Component({
  selector: 'jhi-header-breadcrumb',
  templateUrl: './header-breadcrumb.component.html'
})
export class HeaderBreadcrumbComponent implements OnInit, OnDestroy {
  private subscription: any;
  module: string;
  menu: string;

  @ViewChild('headerView', { static: true }) headerView: TemplateRef<any>;

  constructor(private mainCommunicationService: MainCommunicationService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.subscription = this.route.data.subscribe(data => {
      this.module = data['module'];
      this.menu = data['menu'];
      this.mainCommunicationService.updateHeaderView(this.headerView);
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
