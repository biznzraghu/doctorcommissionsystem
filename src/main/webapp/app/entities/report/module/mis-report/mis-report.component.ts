/* eslint-disable*/
import { Component, OnInit, OnDestroy, Input, ViewChild, ElementRef } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { MisReport, MisConfiguration } from './mis-report.model';
import { MisReportService } from './mis-report.service';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { saveAs } from 'file-saver';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { ExportHelper } from 'app/shared/util/export.helper';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';

@Component({
  selector: 'jhi-mis-report',
  templateUrl: './mis-report.component.html'
})
export class MisReportComponent implements OnInit, OnDestroy {
  private scheduleEventSubscriber: Subscription;
  private refreshEventSubscriber: Subscription;
  misReport: MisReport;
  misReports: MisReport[];
  misConfig: MisConfiguration;
  preferences: Preferences;
  filterCriteria: string;
  isScheduling: boolean;
  scrollBarConfig: PerfectScrollbarConfigInterface;
  queryParams: any;
  reportName: string;
  predicate: any;
  reverse: any;
  unit: any;
  @Input() scheduleEvent: Observable<void>;
  @Input() refreshEvent: Observable<void>;
  @Input() featureCode: string;
  @Input() contentHeight: number;
  @ViewChild(PerfectScrollbarDirective) directiveScroll: PerfectScrollbarDirective;

  constructor(
    private misReportService: MisReportService,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private elementRef: ElementRef,
    private exportHelper: ExportHelper,
    private errorParser: ErrorParser,
    private httpBlockerService: HttpBlockerService
  ) {
    this.misReport = new MisReport();
    this.misReports = [];
    this.misConfig = new MisConfiguration();
    this.isScheduling = false;
    this.scrollBarConfig = {
      minScrollbarLength: 50
    };
    this.reverse = false;
    this.predicate = 'createdDate';
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(data => {
      // tslint:disable-next-line:no-string-literal
      this.preferences = data['preference'];
      this.reportName = data['reportName'];
      if (this.preferences && this.preferences.user) {
        this.misReport.createdBy = this.preferences.user;
        this.unit = this.preferences.organization;
      }
    });
    if (this.reportName) {
      this.loadAll();
    }
    this.scheduleEventSubscriber = this.scheduleEvent.subscribe(data => this.schedule(data));
    this.refreshEventSubscriber = this.refreshEvent.subscribe(() => this.loadAll());
  }

  generateFilterCriteria(formFields) {
    this.filterCriteria = '';
    if (formFields && formFields.unitCode) {
      this.filterCriteria += `unit=${formFields.unitCode}&&`;
    }
    if (formFields && formFields.year) {
      this.filterCriteria += `year=${formFields.year}&&`;
    }
    if (formFields && formFields.month) {
      this.filterCriteria += `month=${formFields.month}&&`;
    }
    this.filterCriteria += 'limitSize=10&&startIndex=0';
  }

  loadAll() {
    const queryObj = {
      query: `reportName:${this.reportName}`+ ' AND ' +`unitCode:${this.unit.code}`,
      sort: this.sort()
    };
    this.misReportService.fetchMisReportByName(queryObj).subscribe(
      (res: HttpResponse<any>) => {
        this.misReports = res.body;
        this.setQueryParams();
      },
      error => this.onError(error.error)
    );
  }

  setQueryParams() {
    this.queryParams = [];
    this.misReports.forEach(report => {
      const params = [];
      // tslint:disable-next-line:forin
      for (const queryParam in report.queryParams) {
        params.push({ key: queryParam, value: report.queryParams[queryParam] });
      }
      this.queryParams.push(params);
    });
  }

  unCamelCase(str) {
    return str
      .replace(/([a-z])([A-Z])/g, '$1 $2')
      .replace(/\b([A-Z]+)([A-Z])([a-z])/, '$1 $2 $3')
      .replace(/^./, (stri: any) => stri.toUpperCase());
  }

  schedule(formFields) {
    this.httpBlockerService.enableHttpBlocker(true);
    const params = {
      unit: formFields.unitCode,
      year: formFields.year,
      month: formFields.month
    };
    if (!this.isScheduling) {
      this.isScheduling = true;
      this.misReport.createdDate = moment();
      this.misReport.scheduleDate = moment().add(10, 'second');
      this.misReport.reportName = this.reportName;
      this.misReportService.create(this.misReport, params, this.reportName).subscribe(
        (result: HttpResponse<any>) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.isScheduling = false;
          this.alertService.success('global.messages.response-msg', { msg: 'Scheduled Sucessfully!' });
          this.loadAll();
        },
        (result: HttpErrorResponse) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.isScheduling = false;
          this.onError(result.error);
        }
      );
    } else {
      this.alertService.error('global.messages.response-msg', { msg: 'Please fill mandatory fields!' });
    }
  }

  downloadReport(reportId) {
    this.httpBlockerService.enableHttpBlocker(true);
    this.misReportService.downloadReport(reportId).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.exportHelper.openFile(res);
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onError(res.error)
      }
    );
  }

  setInfiniteScrollHeight() {
    const tableHeaderHeight = this.elementRef.nativeElement.querySelector('table.fixed-header-table-head').offsetHeight;
    return this.contentHeight - tableHeaderHeight;
  }

  private onError(error) {
    this.errorParser.parse(error);
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset() {
    this.loadAll();
  }

  ngOnDestroy() {
    this.scheduleEventSubscriber.unsubscribe();
    this.refreshEventSubscriber.unsubscribe();
  }
}
