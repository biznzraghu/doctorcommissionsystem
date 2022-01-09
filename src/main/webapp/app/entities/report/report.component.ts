import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
// import { ArthaDetailHeaderOptions } from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { ReportService } from './report.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { JhiAlertService } from 'ng-jhipster';
import { ExportHelper } from 'app/shared/util/export.helper';
import { Subject } from 'rxjs/Rx';
import { UtilsHelperService } from 'app/artha-helpers';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.component.html',
  styleUrls: []
})
export class ReportComponent implements OnInit {
  public title;
  public reportName;
  unitListData;
  userIdentity;
  preferences: any;
  selectedUnitCode: any = '';
  selectedUnit: any = '';
  selectedTime: any = '';
  selectedEmployee: any = '';
  filterList = '';
  selectedDept: any = '';
  empSelected: boolean;
  deptSelected: boolean;
  dateSelected: boolean;
  unit: any;
  year: any;
  month: any;
  departmentId: any;

  unitFilterList = [
    'variable-payout-breakup-summary',
    'variable-payout-breakup',
    'department-wise-revenue',
    'department-revenue-break-up',
    'item-service-wise-payout-breakup'
  ];
  yearMonth = [
    'variable-payout-breakup-summary',
    'variable-payout-breakup',
    'department-wise-revenue',
    'department-revenue-break-up',
    'item-service-wise-payout-breakup'
  ];
  deptFilterList = ['department-wise-revenue', 'department-revenue-break-up'];
  empFilterList = ['variable-payout-breakup-summary', 'variable-payout-breakup'];

  userList = [
    { user: 'Gaurav Bhagat', date: '2020-07-12', year: '2020', status: 'scheduled' },
    { user: 'Sajith Chandran', date: '2020-07-01', year: '2020', status: 'Done' },
    { user: 'Pramod CK', date: '2020-08-28', year: '2020', status: 'scheduled' },
    { user: 'Anurag Mishra', date: '2020-08-18', year: '2020', status: 'Done' }
  ];

  /** VARIABLE DECRATION FOR MIS REPORTS */
  isMisReport: boolean;
  expandReport = true;
  expandBtnName = 'hideFilter'; // showFilter
  misReportScheduleSubject: Subject<any>;
  misReportRefreshReportsSubject: Subject<any>;
  featureCode: string;
  tableContentHeight = 400;
  formFields: any[] = [];
  constructor(
    private mainCommunicationService: MainCommunicationService,
    private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private errorParser: ErrorParser,
    private httpBlockerService: HttpBlockerService,
    private jhiAlertService: JhiAlertService,
    private exportHelper: ExportHelper,
    private utils: UtilsHelperService
  ) {
    // this.isMisReport = true;
    this.misReportScheduleSubject = new Subject<any>();
    this.misReportRefreshReportsSubject = new Subject<any>();
    this.activatedRoute.data.subscribe(data => {
      if (data) {
        this.preferences = data['preference'];
        this.userIdentity = this.preferences.user;
        this.isMisReport = data['misReport'];
      }
      this.reportName = this.activatedRoute.snapshot.routeConfig.path;
      this.title = this.activatedRoute.snapshot.routeConfig.path.replace(/-/gim, ' ');
    });

    this.empSelected = false;
    this.deptSelected = false;
    this.dateSelected = false;
  }

  ngOnInit() {}

  // unit filter selection
  passUnit(unit) {
    if (unit) {
      this.selectedUnit = unit.code;
      this.selectedUnitCode = ' unitCode:' + unit.code;
    } else {
      this.selectedUnit = '';
    }
  }

  // year & month selection
  // getDates(event) {
  //   this.selectedTime = ' AND year:' + event.year + ' AND month:' + event.month;
  //   if(this.selectedTime) {
  //     this.dateSelected = true;
  //   } else {
  //     this.dateSelected = false;
  //   }
  // }

  getDates(event) {
    if (this.utils.hasPropertyAndValue(event, 'year') && this.utils.hasPropertyAndValue(event, 'month')) {
      this.selectedTime = ' AND year:' + event.year + ' AND month:' + event.month;
      this.year = event.year;
      this.month = event.month;
      if (this.selectedTime) {
        this.dateSelected = true;
      } else {
        this.dateSelected = false;
      }
    } else {
      this.dateSelected = false;
      this.selectedTime = null;
    }
  }

  // employee seletion
  getEmployee(event) {
    if (event.user) {
      this.selectedEmployee = ' AND employeeId:' + event.user;
      if (this.selectedEmployee) {
        this.empSelected = true;
      } else {
        this.empSelected = false;
      }
    } else {
      this.selectedEmployee = '';
      this.empSelected = false;
    }
  }

  // department selection
  getDepartment(event) {
    // eslint-disable-next-line no-console
    // console.log('GETDEPARTMENT::', event);
    this.departmentId = event.user;
    if (event.user) {
      this.selectedDept = ' AND departmentId:' + event.user;
      if (this.selectedDept) {
        this.deptSelected = true;
      } else {
        this.deptSelected = false;
      }
    } else {
      this.selectedDept = '';
      this.deptSelected = false;
    }
  }

  schedule() {
    if (this.reportName === 'item-service-wise-payout-breakup') {
      this.generateItemServicePayoutBreakup();
    }

    if (this.utils.isEmpty(this.selectedUnit)) {
      this.jhiAlertService.error('arthaApp.report.validation.selectUnit');
      return;
    }

    if (this.utils.isEmpty(this.selectedTime)) {
      this.jhiAlertService.error('arthaApp.report.validation.selectYearMonth');
      return;
    }

    if (this.reportName === 'variable-payout-breakup' || this.reportName === 'variable-payout-breakup-summary') {
      if (this.utils.isEmpty(this.selectedEmployee)) {
        this.jhiAlertService.error('arthaApp.report.validation.selectEmployee');
        return;
      }
      this.httpBlockerService.enableHttpBlocker(true);
      this.filterList = this.selectedUnitCode + this.selectedTime + this.selectedEmployee;
      this.generateReport(this.filterList);
    }

    if (this.reportName === 'department-wise-revenue') {
      if (this.utils.isEmpty(this.selectedDept)) {
        this.jhiAlertService.error('arthaApp.report.validation.selectDepartment');
        return;
      }
      this.httpBlockerService.enableHttpBlocker(true);
      this.filterList = this.selectedUnitCode + this.selectedTime + this.selectedDept;
      this.generateDepartmentWiseRevenue();
    }

    if (this.reportName === 'department-revenue-break-up') {
      if (this.utils.isEmpty(this.selectedDept)) {
        this.jhiAlertService.error('arthaApp.report.validation.selectDepartment');
        return;
      } else {
        this.httpBlockerService.enableHttpBlocker(true);
        this.generateDepartmentRevenueBreakUp();
      }
    }
  }

  generateReport(filterList) {
    this.reportService
      .generateReport(
        {
          query: filterList
        },
        this.reportName
      )
      .subscribe(
        (result: HttpResponse<any>) => {
          this.downloadXls(result);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (result: HttpErrorResponse) => {
          this.httpBlockerService.enableHttpBlocker(false);
          this.onError(result);
        }
      );
  }

  generateItemServicePayoutBreakup() {
    const params = {
      unit: this.selectedUnit,
      year: this.year,
      month: this.month
    };
    this.reportService.getItemServiceWisePayoutBreakup(params).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        // this.downloadXls(res);
        this.exportHelper.openFile(res);
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onErrorHandler(res.error);
      }
    );
  }

  generateDepartmentRevenueBreakUp() {
    const params = {
      departmentId: this.departmentId,
      year: this.year,
      month: this.month,
      unitCode: this.selectedUnit
    };
    this.reportService.getDepartment(params).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        // this.downloadXls(res);
        this.exportHelper.openFile(res);
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onErrorHandler(res.error);
      }
    );
  }

  generateDepartmentWiseRevenue() {
    const params = {
      departmentId: this.departmentId,
      year: this.year,
      month: this.month,
      unitCode: this.selectedUnit
    };
    this.reportService.getDepartmentWiseRevenue(params).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        // this.downloadXls(res);
        this.exportHelper.openFile(res);
      },
      (res: HttpErrorResponse) => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.onErrorHandler(res.error);
      }
    );
  }

  reset() {
    this.reportService.refreshReport(true);
  }
  private onErrorHandler(error) {
    this.errorParser.parse(error);
  }

  private onError(error) {
    // this.jhiAlertService.error(
    //   error && error.message ? error.message : 'global.messages.response-msg',
    //   error && error.description ? { msg: error.description } : null,
    //   null
    // );
    if (error.status === 400) {
      this.jhiAlertService.error('global.messages.response-msg', {
        msg: 'Doctor Payout Breakup Is Not Available For The Selected Employee'
      });
    }
    // this.errorParser.parse(error);
  }

  downloadXls(data) {
    const fileUrl = URL.createObjectURL(new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;' }));
    window.open(fileUrl);
  }
  /** METHOD DECRATION RELATED TO MIS REPORTS */
  // scheduleMisReport() {
  //   const params = {
  //     departmentId: this.departmentId,
  //     year: this.year,
  //     month: this.month,
  //     unitCode: this.selectedUnit
  //   };
  //   // this.misReportScheduleSubject.next(this.formFields);
  //   this.misReportScheduleSubject.next(params);
  // }
  scheduleMisReport() {
    if (this.utils.isEmpty(this.selectedUnit)) {
      this.jhiAlertService.error('arthaApp.report.validation.selectUnit');
      return;
    } else if (this.utils.isEmpty(this.selectedTime)) {
      this.jhiAlertService.error('arthaApp.report.validation.selectYearMonth');
      return;
    } else {
      const params = {
        departmentId: this.departmentId,
        year: this.year,
        month: this.month,
        unitCode: this.selectedUnit
      };
      // this.misReportScheduleSubject.next(this.formFields);
      this.misReportScheduleSubject.next(params);
    }
  }

  refreshReports() {
    this.misReportRefreshReportsSubject.next();
    this.reportService.refreshReport(true);
  }

  public expandPeriodReport() {
    this.expandReport = !this.expandReport;
    if (!this.expandReport) {
      this.expandBtnName = 'showFilter';
    } else {
      this.expandBtnName = 'hideFilter';
    }
  }
}
