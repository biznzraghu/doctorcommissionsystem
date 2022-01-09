import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { Widget } from './dashboard.model';
import { DashboardService } from './dashboard.service';
import { JhiAlertService } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Preferences } from '../artha-models/preferences.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
@Component({
  selector: 'jhi-dashboard-setting-component',
  templateUrl: 'dashboard-setting.component.html'
})
export class DashboardSettingComponent implements OnInit {
  @Input() pageName;
  @Input() tabs;
  @Input() authorities;
  @Input() preferences: Preferences;
  dashboardList: any;
  newItem: object;
  addNew: boolean;
  activeTab: number;
  dashboardTab: any;
  dashboardTabs: any;
  selectedTabValue: string;
  allDashboardList: any;
  tabModel: string;
  authFilter: string;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50,
    suppressScrollX: true
  };
  public successMsg: string;
  public errorMsg: string;
  @ViewChild('widgetTopSection') widgetTopSection: ElementRef;
  @ViewChild('widgetInnerSection') widgetInnerSection: ElementRef;
  @ViewChild('removeWidgetPop') removeWidgetPop: any;
  @ViewChild('removeTabPop') removeTabPop: any;
  imageList = ['../../../content/images/list.png', '../../../content/images/graph.png'];
  constructor(
    public activeModal: NgbActiveModal,
    private activatedRoute: ActivatedRoute,
    private dashboardService: DashboardService,
    private alertService: JhiAlertService,
    private httpBlockerService: HttpBlockerService,
    private elementRef: ElementRef,
    private errorParser: ErrorParser
  ) {}
  ngOnInit() {
    this.dashboardTabs = JSON.parse(JSON.stringify(this.tabs));
    if (this.dashboardTabs.length > 0) {
      this.viewDetail(this.dashboardTabs[0]);
    }
    this.addNew = false;
    this.newItem = { dashboardName: '', edit: true };
    this.activeTab = 1;
    this.selectedTabValue = 'Selected';
    const authLength = this.authorities.length;
    for (let i = 0; i < authLength; i++) {
      if (this.authorities[i] !== '000000000') {
        if (!this.authFilter) {
          this.authFilter = this.authorities[i];
        } else {
          this.authFilter = this.authFilter + ' OR ' + this.authorities[i];
        }
      } else {
        this.authFilter = '*';
        break;
      }
    }
  }

  selectedTab(tab) {
    this.clear();
    this.previewScrollToTop();
    this.selectedTabValue = tab;
    if (tab === 'Selected' && this.dashboardTab) {
      this.viewDetail(this.dashboardTab);
    } else {
      this.dashboardService.getAllWidgets({ query: 'code:(' + this.authFilter + ')' }).subscribe(
        (res: HttpResponse<any>) => {
          this.allDashboardList = res.body;
        },
        (res: HttpErrorResponse) => {
          this.onError(res.error);
        }
      );
    }
  }

  previewScrollToTop() {
    if (this.widgetTopSection && this.widgetInnerSection) {
      const info = this.widgetTopSection.nativeElement;
      info.scrollTop =
        this.elementRef.nativeElement.querySelector('div.widgetTopSectionDiv').offsetHeight -
        this.widgetInnerSection.nativeElement.offsetHeight;
    }
  }
  selectTab(item) {
    item.edit = true;
    this.activeTab = item.dashboardOrder;
    this.tabModel = item.dashboardName;
  }

  deSelectTab(item) {
    event.stopPropagation();
    item.edit = false;
    this.tabModel = '';
  }

  close() {
    this.activeModal.close('cancel');
  }

  saveSettings() {
    const tabLength = this.dashboardTabs.length;
    for (let i = 0; i < tabLength; ++i) {
      this.dashboardTabs[i].dashboardOrder = i + 1;
    }
    this.dashboardService.saveAllDashboards(this.dashboardTabs).subscribe(
      (res: HttpResponse<any>) => {
        this.dashboardTabs = res;
        this.viewDetail(this.dashboardTabs[0]);
      },
      (res: HttpErrorResponse) => {
        this.onError(res.error);
      }
    );
  }

  deleteTab(event, item, index) {
    event.stopPropagation();
    this.clear();
    this.dashboardService.deleteDashboard(item).subscribe(
      (res: HttpResponse<any>) => {
        this.dashboardTabs.splice(index, 1);
        if (this.dashboardTabs.length > 0) {
          this.saveSettings();
        } else {
          this.selectedTabValue = 'All';
          this.dashboardTab = undefined;
        }
        this.successMsg = 'Tab deleted successfully!';
        this.httpBlockerService.enableHttpBlocker(false);
        this.tabModel = '';
      },
      (err: HttpErrorResponse) => {
        this.errorMsg = 'Something went wrong while deleting tab!';
        this.httpBlockerService.enableHttpBlocker(false);
      },
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        if (this.removeTabPop.isOpen()) {
          this.removeTabPop.close();
        }
      }
    );
  }

  saveList(item, index?: any) {
    item.edit = false;
    item.dashboardName = this.tabModel ? this.tabModel : item.dashboardName;
    item.userName = this.preferences.user.id;
    item.dashboardOrder = index !== undefined ? index + 1 : this.dashboardTabs.length + 1;
    item.updatedOn = null;
    item.createdOn = item.createdOn ? item.createdOn : null;
    this.clear();
    if (this.addNew) {
      if (item.dashboardName === '' || item.dashboardName === undefined || item.dashboardName.trim() === '') {
        this.alertService.error('global.messages.response-msg', { msg: 'Enter valid Dashboard name..' });
        return;
      }
      const enteredName = item.dashboardName.trim();
      if (this.dashboardTabs && this.dashboardTabs.length > 0) {
        const isTabExist = this.dashboardTabs.find(tab => tab.dashboardName.toLowerCase() === enteredName.toLowerCase());
        if (isTabExist) {
          this.alertService.error('global.messages.response-msg', { msg: 'Tab name is already exist.' });
          return;
        }
      }
      this.dashboardService.createNewDashboard(item).subscribe(
        (res: HttpResponse<any>) => {
          this.dashboardTabs.push(res);
          this.addNew = false;
          this.viewDetail(res);
          this.newItem = { dashboardName: '', edit: true };
          this.tabModel = '';
          this.httpBlockerService.enableHttpBlocker(false);
          this.successMsg = 'Tab added successfully!';
        },
        (err: HttpErrorResponse) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.errorMsg = 'Something went wrong while adding tab!';
        },
        () => {
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    } else {
      this.dashboardService.editDashboard(item).subscribe(
        (res: HttpResponse<any>) => {
          this.dashboardTabs.splice(index, 1, res);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (err: HttpErrorResponse) => {
          this.httpBlockerService.enableHttpBlocker(false);
        },
        () => {
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    }
  }

  showDeleteBtn(item) {
    item.showDelete = true;
  }
  clearItem() {
    this.addNew = false;
  }

  viewDetail(tab) {
    if (this.removeTabPop && this.removeTabPop.isOpen()) {
      this.removeTabPop.close();
      this.dashboardTabs.forEach((data: any) => (data.showDelete = false));
    }
    this.clear();
    this.previewScrollToTop();
    this.dashboardTab = tab;
    this.selectedTabValue = 'Selected';
    this.activeTab = tab.dashboardOrder;
    this.dashboardList = [];
    this.dashboardService.getWidgets({ query: 'userDashboard.id:' + tab.id }).subscribe(
      (res: HttpResponse<any>) => {
        this.dashboardList = res.body;
      },
      (res: HttpErrorResponse) => {
        this.onError(res.error);
      }
    );
  }

  addTab() {
    if (!this.addNew) {
      this.addNew = true;
    }
  }

  addWidget(widget) {
    this.clear();
    if (this.dashboardTabs.length === 0) {
      this.alertService.success('global.messages.response-msg', { msg: 'Please Select/Add Dashboard' });
    } else {
      const newWidget = new Widget();
      newWidget.customProperties = widget.properties;
      newWidget.customSettings = widget.settings;
      newWidget.userDashboard.id = this.dashboardTab.id;
      newWidget.widgetMaster.id = widget.id;
      this.dashboardService.createWidgets(newWidget).subscribe(
        (res: HttpResponse<any>) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.successMsg = 'Widget added successfully!';
        },
        (err: HttpErrorResponse) => {
          this.errorMsg = 'Something went wrong while adding widget!';
          this.httpBlockerService.enableHttpBlocker(false);
        },
        () => {
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    }
  }

  removeWidget(widget) {
    this.clear();
    this.dashboardService.deleteWidgets(widget.id).subscribe(
      (res: HttpResponse<any>) => {
        this.viewDetail(this.dashboardTab);
        this.successMsg = 'Widget deleted successfully!';
      },
      (err: HttpErrorResponse) => {
        this.errorMsg = 'Something went wrong while deleting widget!';
      },
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        if (this.removeWidgetPop.isOpen()) {
          this.removeWidgetPop.close();
        }
      }
    );
  }
  openDeleteTabPop(item) {
    event.stopPropagation();
    if (this.removeTabPop && this.removeTabPop.isOpen()) {
      this.removeTabPop.close();
      this.dashboardTabs.forEach((data: any) => (data.showDelete = false));
    }
    item.showDelete = true;
    this.removeTabPop.open();
  }
  closeDeleteTabPop() {
    this.removeTabPop.close();
    this.dashboardTabs.forEach((data: any) => (data.showDelete = false));
  }
  hideTabDeleteIcon(item) {
    // if ((this.removeTabPop) && (this.removeTabPop.isOpen())) {
    // } else {
    // 	item.showDelete = false;
    // }
    if (!(this.removeTabPop && this.removeTabPop.isOpen())) {
      item.showDelete = false;
    }
  }
  showTabDeleteIcon(item) {
    // if ((this.removeTabPop) && (this.removeTabPop.isOpen())) {
    // } else {
    // 	item.showDelete = true;
    // }
    if (!(this.removeTabPop && this.removeTabPop.isOpen())) {
      item.showDelete = true;
    }
  }
  private clear() {
    this.successMsg = null;
    this.errorMsg = null;
  }
  onDropSuccess(item) {
    this.saveSettings();
  }

  private onError(errorObj): any {
    this.errorParser.parse(errorObj);
  }
}
