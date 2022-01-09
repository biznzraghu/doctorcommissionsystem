import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { ErrorParser } from 'app/shared/util/error-parser.service';
// import { ConfigurationHelperService, Preferences } from 'athma-commons';
import * as c3 from 'c3';
import { Preferences } from '../artha-models/preferences.model';
import { DashboardWidgetService } from './dashboard-widget.service';
@Component({
  selector: 'jhi-department-trend-widget-graph',
  templateUrl: 'department-trend-widget.component.html'
})
export class DepartmentTrendWidgetComponent implements OnInit, AfterViewInit {
  @Input() data: any;
  widgetData: any;
  prefSubscription: any;
  preferences: Preferences;
  discountDetail: any;
  fromDate: any;
  toDate: any;
  decimalPipeEx = '1.2-2';
  loading: boolean;
  currency: any = 'INR';
  duration: string;
  productivityChartData: any;
  selectedDateFilter = 'Last 7 Days';

  dateRanges: Array<string>;
  showCustomDate: boolean;

  @ViewChild('dateFilterPop') dateFilterPop: any;
  constructor(
    private elementRef: ElementRef,
    private dashboardWidgetService: DashboardWidgetService,
    // private configHelperService: ConfigurationHelperService,
    private errorParser: ErrorParser
  ) {
    this.dashboardWidgetService.getPreferencesData().then((res: any) => {
      this.preferences = res;
      if (this.preferences) {
        this.loadConfiguration();
        this.getGraphData(this.selectedDateFilter);
      }
    });
  }

  ngOnInit() {
    this.widgetData = JSON.parse(this.data);
    this.duration = 'day';
    this.loading = false;
    this.dateRanges = ['Last 7 Days', 'Last 7 weeks', 'Last 12 weeks', 'Last 6 months', 'Last 12 months'];
  }

  loadConfiguration() {
    const hscId = this.preferences.healthcareServiceCenter ? this.preferences.healthcareServiceCenter.id : null;
    const global = this.preferences.organization.partOf ? this.preferences.organization.partOf : null;

    // this.configHelperService.getConfiguration(['athma_currency'], this.preferences.hospital.id, global, hscId)
    // 	.then((configMap: any) => {
    // 		if (configMap) {
    // 			if (configMap['athma_currency'] && configMap['athma_currency'].length > 0) {
    // 				this.currency = configMap['athma_currency'];
    // 			}
    // 		}
    // 	});
  }

  durationChange(duration) {
    this.selectedDateFilter = duration;
    this.getGraphData(duration);
  }

  getGraphData(selectedDateFilter) {
    // this.loading = true;
    this.dashboardWidgetService.setDateRangeTrend(selectedDateFilter).then(response => {
      this.duration = response.duration;
      this.fromDate = response.fromDate;
      this.toDate = response.toDate;
      this.dashboardWidgetService
        .getDepartmentWiseTrend({
          unitCode: this.preferences.organization.code,
          fromDate: this.fromDate,
          toDate: this.toDate,
          format: this.duration
        })
        .subscribe(
          (res: HttpResponse<any>) => {
            this.productivityChartData = res.body;
            // this.loading = false;
            this.generateGraph();
            if (this.dateFilterPop && this.dateFilterPop.isOpen()) {
              this.dateFilterPop.close();
            }
          },
          (err: HttpErrorResponse) => {
            this.onError(err.error);
          }
        );
    });
  }

  ngAfterViewInit() {
    // this.generateGraph();
  }

  generateGraph() {
    const parent = this;

    const columnData = [];
    const valueData = [0, 0, 0, 0, 0, 0, 0];

    const depRevenue = this.productivityChartData.departmentRevenue;

    const keys = this.productivityChartData.key;
    const depName = this.productivityChartData.departmentName;
    for (let index = 0; index < keys.length; index++) {
      let element: any;
      let depRev: any;

      if (depName[index].indexOf(',') > 0) {
        element = depName[index].split(',');
        depRev = depRevenue[index].split(',');
      } else {
        element = [depName[index]];
        depRev = [depRevenue[index]];
      }
      if (element[0] !== ' ' && parseInt(depRev[0], 10) >= 1) {
        element.forEach(function(obj, ind) {
          const key = obj;
          const value = depRev[ind];
          valueData[index] = parseInt(value, 10);
          if (key !== '' && value !== '') {
            columnData.push([key, ...valueData]);
          }
        });
      }
    }

    columnData.unshift(['keys', ...keys]);

    const chartConfig = {
      bindto: this.elementRef.nativeElement.querySelector('#departmentTrendWidget'),
      size: {
        height: 340,
        width: 580
      },
      data: {
        x: 'keys',
        columns: columnData
      },
      axis: {
        x: {
          type: 'category',
          height: 70,
          tick: {
            culling: {
              max: 15
            }
          }
        },
        y: {
          width: 70,
          padding: { bottom: 0 },
          min: 0,
          label: {
            text: 'Amount(' + parent.currency + ')',
            position: 'outer-middle'
          },
          tick: {
            format: (d: any) => '' + d
          }
        }
      }
    };
    if (this.productivityChartData) {
      const chart = c3.generate(chartConfig);
    }
  }

  refreshData() {
    this.durationChange(this.selectedDateFilter);
  }
  closeDateFilterPop() {
    if (this.dateFilterPop && this.dateFilterPop.isOpen() && !this.showCustomDate) {
      this.dateFilterPop.close();
    }
  }
  private onError(errorObj): any {
    this.errorParser.parse(errorObj);
  }
}
