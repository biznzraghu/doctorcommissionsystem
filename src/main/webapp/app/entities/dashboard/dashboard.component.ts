import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { AuthenticationResolver, DashboardService, UpdateWidget } from './dashboard.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { GridsterConfigService } from './dashboard-config.service';
import { Preferences } from '../artha-models/preferences.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  options: GridsterConfig;
  // dashboards: Array<GridsterItem>;
  selectedTab: string;
  allTabs: any;
  prefSubscription: any;
  preferences: Preferences;
  subscription: any;
  changedWidget: any;
  predicate: string;
  userAuthorities: string[];
  reverse: boolean;
  config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50,
    suppressScrollX: true
  };
  userId: number;
  dashboards: Array<any>;
  @ViewChild(PerfectScrollbarDirective) directiveScroll: PerfectScrollbarDirective;

  constructor(
    private elementRef: ElementRef,
    private dashboardService: DashboardService,
    private httpBlockerService: HttpBlockerService,
    private updateWidget: UpdateWidget,
    private errorParser: ErrorParser,
    private authenticationResolver: AuthenticationResolver,
    private activatedRoute: ActivatedRoute
  ) {
    this.allTabs = [];
    this.predicate = 'customProperties.x';
    this.reverse = true;

    this.authenticationResolver.resolve().then((data: any) => {
      if (data.auth.userIdentity) {
        const res = data.auth.userIdentity;
        if (res && res.authorities && res.authorities.length > 0) {
          this.userAuthorities = res.authorities.filter(authi => !(authi === 'ROLE_USER' || authi === 'ROLE_ADMIN'));
        }
      }
    });

    this.dashboardService.getPreferencesData().then((res: any) => {
      if (res) {
        this.preferences = res;
        if (this.preferences) {
          this.reloadDashboard();
          const parent = this;
          this.options = GridsterConfigService;
          this.options.itemChangeCallback = function(item) {
            for (let i = 0; i < parent.dashboards.length; ++i) {
              if (parent.dashboards[i].id === item.id) {
                parent.dashboards[i].customProperties = item;
              }
            }
            parent.dashboardService.saveAllWidgets(parent.dashboards).subscribe(
              () => {},
              (err: HttpErrorResponse) => {
                this.onError(err.error);
              }
            );
          };
          this.subscription = this.updateWidget.getChengedWidget().subscribe(widget => {
            const dashboardLength = this.dashboards.length;
            for (let i = 0; i < dashboardLength; ++i) {
              if (this.dashboards[i].id === widget.id) {
                this.dashboards.splice(i, 1, widget);
                break;
              }
            }
          });
        }
      }
    });
  }
  ngOnInit() {}

  tabChanged(data) {
    this.getWidgets(data);
    if (this.directiveScroll) {
      this.directiveScroll.scrollToTop();
    }
  }

  getWidgets(tab) {
    this.dashboards = [];
    this.httpBlockerService.enableHttpBlocker(true);
    this.dashboardService
      .getWidgets({
        sort: ['customProperties.x,asc', 'customProperties.y,asc'],
        query: 'userDashboard.id:' + tab.id
      })
      .subscribe((res: HttpResponse<any>) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.dashboards = res.body.map(
          item => {
            item.customProperties.id = item.id;
            return item;
          },
          (err: HttpErrorResponse) => {
            this.httpBlockerService.enableHttpBlocker(false);
            this.onError(err.error);
          }
        );
      });
  }

  sort() {
    const result = [this.predicate + ',asc'];
    if (this.predicate !== 'id') {
      result.push('customProperties.y,asc');
    }
    if (this.directiveScroll) {
      this.directiveScroll.scrollToTop();
    }
    return result;
  }

  setDashboardPageHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.dashboard-header').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight;
  }

  reloadDashboard() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.dashboardService.getAllTabs({ query: 'userName:' + this.preferences.user.id, sort: ['dashboardOrder'] }).subscribe(
      (res: HttpResponse<any>) => {
        this.allTabs = res.body;
        if (this.allTabs.length > 0) {
          this.getWidgets(this.allTabs[0]);
        } else {
          this.dashboards = [];
        }
        this.httpBlockerService.enableHttpBlocker(false);
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onError(res.error);
      }
    );
  }

  changedOptions() {
    this.options.api.optionsChanged();
  }

  removeItem(item) {
    this.dashboards.splice(this.dashboards.indexOf(item), 1);
  }

  // No add item calling
  addItem() {
    // this.dashboards.push({});
  }

  private onError(errorObj): any {
    this.errorParser.parse(errorObj);
  }
}
