import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs/Rx';
import { HttpResponse } from '@angular/common/http';
import { DepartmentService } from './../../administration/administration-services/department.service';
import { JhiAlertService } from 'ng-jhipster';
import { INgxMyDpOptions, IMyDateModel, IMyOptions } from 'ngx-mydatepicker';
import { ServiceMasterService } from './../../administration/administration-services/service-master.service';
import { DepartmentRevenueBenefit, VariablePayout, LengthOfStayBenefit } from './../../artha-models/variable-payout.model';
import { NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ApplicableServicesComponent } from './applicable-services-modal/applicable-services.component';
import * as moment from 'moment';
import { AthmaDateConverter, SearchTermModify } from 'app/artha-helpers';
import { of, concat } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, switchMap, map, catchError } from 'rxjs/operators';
@Component({
  selector: 'jhi-variable-payout-basic-details',
  templateUrl: './variable-payout-basic-details.component.html'
})
export class VariablePayoutBasicDetailsComponent implements OnInit {
  isDepartmentRevenue: boolean;
  isStayBenefit: boolean;
  departmentSearching: boolean;
  departmentStaySearching: boolean;
  @ViewChild('commencementDateDp', { static: false }) commencementDateDp: any;
  @ViewChild('contractEndDateDp', { static: false }) contractEndDateDp: any;
  @ViewChild('minimumAssuredDateDp', { static: false }) minimumAssuredDateDp: any;
  ngxDateOptions: INgxMyDpOptions = {
    dateFormat: 'dd/mm/yyyy'
  };
  minimumAssuredDateOptions: INgxMyDpOptions;
  commencementDate: any;
  contractEndDate: any;
  minimumAssuredDate: any;
  serviceList: any;
  serviceSearching: boolean;
  departmentRevenueBenefit: DepartmentRevenueBenefit;
  departmentRevenueBenefitUpdate: DepartmentRevenueBenefit;
  variablePayout: VariablePayout;
  lengthOfStayBenefit: LengthOfStayBenefit;
  lengthOfStayBenefitUpdate: LengthOfStayBenefit;
  uploadFiles = [];
  editRevenueBenefitIndex: number;
  editStayBenefitIndex: number;
  modalRef: NgbModalRef;
  isDuplicateRevenueDepartment: boolean;

  public stayBenefitServicesList$: Observable<any[]>;
  public serviceInput$ = new Subject<string>();
  public servicesLoading = false;
  @ViewChild('basicDetailsFrom', { static: false }) form: any;
  @Input() variablePayoutObj;
  @Input() organizationCode;

  constructor(
    private router: Router,
    private departmentService: DepartmentService,
    private jhiAlertService: JhiAlertService,
    private serviceMasterService: ServiceMasterService,
    private modalService: NgbModal,
    private dateConverter: AthmaDateConverter,
    private searchTermModify: SearchTermModify
  ) {
    this.isDepartmentRevenue = false;
    this.isStayBenefit = false;
    this.departmentSearching = false;
    this.departmentStaySearching = false;
    this.serviceSearching = false;
    this.departmentRevenueBenefit = new DepartmentRevenueBenefit();
    this.departmentRevenueBenefitUpdate = new DepartmentRevenueBenefit();
    this.variablePayout = new VariablePayout();
    this.lengthOfStayBenefit = new LengthOfStayBenefit();
    this.lengthOfStayBenefitUpdate = new LengthOfStayBenefit();
    this.isDuplicateRevenueDepartment = false;
    this.loadStayBenefitServicesList();

    this.minimumAssuredDateOptions = {
      dateFormat: 'dd/mm/yyyy',
      alignSelectorRight: true,
      disableUntil: { year: moment().year(), month: moment().month() + 1, day: moment().date() - 1 }
    };
  }

  ngOnInit() {
    if (this.variablePayoutObj.commencementDate) {
      this.commencementDate = {
        date: this.dateConverter.toBootstrapDate(new Date(this.variablePayoutObj.commencementDate))
      };
    }
    if (this.variablePayoutObj.contractEndDate) {
      this.contractEndDate = {
        date: this.dateConverter.toBootstrapDate(new Date(this.variablePayoutObj.contractEndDate))
      };
    }
    if (this.variablePayoutObj.minAssuredValidityDate) {
      this.minimumAssuredDate = {
        date: this.dateConverter.toBootstrapDate(new Date(this.variablePayoutObj.minAssuredValidityDate))
      };
    }
  }

  uploadContractInfo($event: any) {
    const file: File = $event.target.files[0];

    if (file && file.size > 0) {
      if (file.size > 10 * 1024 * 1024) {
        this.jhiAlertService.error('global.messages.response-msg', { msg: 'Maximum file size: 10 Mb.' });
        return;
      }

      const ind = this.uploadFiles.findIndex(function(f) {
        return f.name === file.name;
      });
      if (ind === -1) {
        this.uploadFiles.push(file);
      } else {
        this.jhiAlertService.error('global.messages.response-msg', {
          msg: 'This Document is already exist, Please enter different document.'
        });
      }
    }
  }

  removeUploadContract(index: number) {
    this.uploadFiles.splice(index, 1);
  }

  revenueBenefitTypeChange(flow) {
    if (flow === 'create') {
      if (this.departmentRevenueBenefit && this.departmentRevenueBenefit.revenueBenefitType !== 'FIXED') {
        this.departmentRevenueBenefit.payoutPercentage = null;
      }
    } else if (flow === 'update') {
      if (this.departmentRevenueBenefitUpdate && this.departmentRevenueBenefitUpdate.revenueBenefitType !== 'FIXED') {
        this.departmentRevenueBenefitUpdate.payoutPercentage = null;
      }
    }
  }

  addDepartmentRevenue(obj, flow, index) {
    if (obj.department === undefined || obj.department === null || obj.department === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectDepartment');
    } else if (obj.revenueBenefitType === undefined || obj.revenueBenefitType === null || obj.revenueBenefitType === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectRevenueBenefitType');
    } else if (obj && obj.revenueBenefitType === 'FIXED' && !obj.payoutPercentage) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.enterPercentage');
    } else {
      if (
        !(
          this.variablePayoutObj &&
          this.variablePayoutObj.departmentRevenueBenefits &&
          this.variablePayoutObj.departmentRevenueBenefits.length
        )
      ) {
        this.variablePayoutObj.departmentRevenueBenefits = [];
      }
      if (flow === 'create') {
        this.variablePayoutObj.departmentRevenueBenefits.push(obj);
        this.departmentRevenueBenefit = new DepartmentRevenueBenefit();
      } else if (flow === 'update') {
        this.variablePayoutObj.departmentRevenueBenefits[index] = obj;
        this.departmentRevenueBenefitUpdate = new DepartmentRevenueBenefit();
        this.editRevenueBenefitIndex = undefined;
      }
    }
  }

  editDepartmentRevenue(ind, editObj) {
    this.editRevenueBenefitIndex = ind;
    this.departmentRevenueBenefitUpdate = JSON.parse(JSON.stringify(editObj));
  }

  cancelDepartmentUpdate() {
    this.editRevenueBenefitIndex = undefined;
  }

  deleteDepartmentRevenue(index: number) {
    this.form.control.markAsDirty();
    if (this.editRevenueBenefitIndex === undefined) {
      this.variablePayoutObj.departmentRevenueBenefits.splice(index, 1);
    }
  }

  addStayBenefit(updateObj, flow, index) {
    if (updateObj.department === undefined || updateObj.department === null || updateObj.department === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectDepartment');
    } else if (updateObj.days === undefined || updateObj.days === null || updateObj.days === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.enterDays');
    } else if (updateObj.perDayPayoutAmount === undefined || updateObj.perDayPayoutAmount === null || updateObj.perDayPayoutAmount === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.enterPerDayPayoutAmount');
    } else if (updateObj.stayBenefitServices.length === 0) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectServices');
    } else {
      if (!(this.variablePayoutObj && this.variablePayoutObj.lengthOfStayBenefits && this.variablePayoutObj.lengthOfStayBenefits.length)) {
        this.variablePayoutObj.lengthOfStayBenefits = [];
      }
      const copyLengthOfStayBenefit = JSON.parse(JSON.stringify(updateObj));
      if (flow === 'create' && copyLengthOfStayBenefit && copyLengthOfStayBenefit.stayBenefitServices) {
        copyLengthOfStayBenefit.stayBenefitServices.forEach((item, i) => {
          if (item) {
            const obj = {
              serviceCode: item.code,
              serviceId: item.id,
              serviceName: item.name
            };
            copyLengthOfStayBenefit.stayBenefitServices[i] = obj;
          }
        });
      }

      if (flow === 'create') {
        this.variablePayoutObj.lengthOfStayBenefits.push(copyLengthOfStayBenefit);
        this.lengthOfStayBenefit = new LengthOfStayBenefit();
      } else if (flow === 'update') {
        this.variablePayoutObj.lengthOfStayBenefits[index] = updateObj;
        this.lengthOfStayBenefitUpdate = new LengthOfStayBenefit();
        this.editStayBenefitIndex = undefined;
      }
    }
  }

  editStateBenefit(ind, editObj) {
    this.editStayBenefitIndex = ind;
    this.lengthOfStayBenefitUpdate = JSON.parse(JSON.stringify(editObj));
  }

  cancelStateBenefitUpdate() {
    this.editStayBenefitIndex = undefined;
  }

  deleteStayBenefit(index: number) {
    this.form.control.markAsDirty();
    if (this.editStayBenefitIndex === undefined) {
      this.variablePayoutObj.lengthOfStayBenefits.splice(index, 1);
    }
  }

  departmentSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.departmentSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.departmentService
              .userOrganizationDepartmentMappingSearch({
                query:
                  `organization.code:${this.organizationCode} AND userMaster.id:${this.variablePayoutObj.employeeId} AND department.name:` +
                  this.searchTermModify.modify(term)
              })
              .map((res: HttpResponse<any>) => {
                const departments: any = res.body && res.body.length > 0 && res.body[0].department ? res.body[0].department : [];
                return departments;
              })
      )
      .do(() => (this.departmentSearching = false));

  inputFormatDepartmentTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatDepartmentTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  departmentStaySearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.departmentStaySearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.departmentService
              .userOrganizationDepartmentMappingSearch({
                query:
                  `organization.code:${this.organizationCode} AND userMaster.id:${this.variablePayoutObj.employeeId} AND department.name:` +
                  this.searchTermModify.modify(term)
              })
              .map((res: HttpResponse<any>) => {
                const departments: any = res.body && res.body.length > 0 && res.body[0].department ? res.body[0].department : [];
                return departments;
              })
      )
      .do(() => (this.departmentStaySearching = false));

  inputFormatDepartmentStayTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatDepartmentStayTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  departmentRevenueBenefitChange() {
    if (this.departmentRevenueBenefit.department && this.departmentRevenueBenefit.department.id === undefined) {
      this.departmentRevenueBenefit.department = undefined;
    }
  }
  departmentStayBenefitChange() {
    if (this.lengthOfStayBenefit.department && this.lengthOfStayBenefit.department.id === undefined) {
      this.lengthOfStayBenefit.department = undefined;
    }
  }
  onSelectDepartment(e) {
    if (
      this.variablePayoutObj &&
      this.variablePayoutObj.departmentRevenueBenefits &&
      this.variablePayoutObj.departmentRevenueBenefits.length
    ) {
      if (this.variablePayoutObj.departmentRevenueBenefits.find(ele => ele.department.id === e.item.id)) {
        this.isDuplicateRevenueDepartment = true;
        this.jhiAlertService.error('arthaApp.variable-payout.validation.departmentExist');
        return;
      } else {
        this.isDuplicateRevenueDepartment = false;
      }
    }
  }

  onSelectStayDepartment(e) {
    if (this.variablePayoutObj && this.variablePayoutObj.lengthOfStayBenefits && this.variablePayoutObj.lengthOfStayBenefits.length) {
      if (this.variablePayoutObj.lengthOfStayBenefits.find(ele => ele.department.id === e.item.id)) {
        this.isDuplicateRevenueDepartment = true;
        this.jhiAlertService.error('arthaApp.variable-payout.validation.departmentExist');
        return;
      } else {
        this.isDuplicateRevenueDepartment = false;
      }
    }
  }

  serviceSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.serviceSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.serviceMasterService.search({ query: `*` }).map((res: HttpResponse<any>) => {
              return res.body;
            })
      )
      .do(() => (this.serviceSearching = false));

  inputFormatServiceTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatServiceTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  private loadStayBenefitServicesList() {
    this.stayBenefitServicesList$ = concat(
      of([]),
      this.serviceInput$.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => (this.servicesLoading = true)),
        switchMap((text: string) =>
          this.serviceMasterService
            .search({
              query: text ? this.searchTermModify.modify(text) : '*',
              page: 0,
              size: 10
            })
            .pipe(
              map(res => res.body),
              catchError(() => of([])),
              tap(() => (this.servicesLoading = false))
            )
        )
      )
    );
  }

  daysValidation(data) {
    if (data < 1 || data > 100) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.daysValidation');
    }
  }

  onChangeCommencementDate(event: IMyDateModel) {
    this.commencementDate = { date: this.dateConverter.toBootstrapDate(new Date(event.jsdate)) };
    this.commencementDateDp.toggleCalendar();
    if (this.commencementDate && this.commencementDate.date) {
      this.variablePayoutObj.commencementDate = moment(this.dateConverter.toDateString(this.commencementDate.date)).format(
        'YYYY-MM-DDTHH:mm:ss'
      );
    }
  }

  clearCommencementdate() {
    this.commencementDate = null;
    this.variablePayoutObj.commencementDate = null;
  }

  onChangeContractEndDate(event: IMyDateModel) {
    this.contractEndDate = { date: this.dateConverter.toBootstrapDate(new Date(event.jsdate)) };
    this.contractEndDateDp.toggleCalendar();
    if (this.contractEndDate && this.contractEndDate.date) {
      this.variablePayoutObj.contractEndDate = moment(this.dateConverter.toDateString(this.contractEndDate.date)).format(
        'YYYY-MM-DDTHH:mm:ss'
      );
    }
  }

  clearContractEnddate() {
    this.contractEndDate = null;
    this.variablePayoutObj.contractEndDate = null;
  }

  onChangeMinimumAssuredDate(event: IMyDateModel) {
    this.minimumAssuredDate = { date: this.dateConverter.toBootstrapDate(new Date(event.jsdate)) };
    this.minimumAssuredDateDp.toggleCalendar();
    if (this.minimumAssuredDate && this.minimumAssuredDate.date) {
      this.variablePayoutObj.minAssuredValidityDate = moment(this.dateConverter.toDateString(this.minimumAssuredDate.date)).format(
        'YYYY-MM-DDTHH:mm:ss'
      );
    }
  }

  clearMinimumAssuredDate() {
    this.minimumAssuredDate = null;
    this.variablePayoutObj.minAssuredValidityDate = null;
    this.variablePayoutObj.minAssuredAmount = 0;
  }

  minAssuredAmountChanged() {
    if (this.variablePayoutObj.minAssuredAmount) {
      const minAssuredAmount = this.variablePayoutObj.minAssuredAmount.split('.');
      if (minAssuredAmount[0].length > 8) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.minAssuredAmountValidity');
        this.variablePayoutObj.minAssuredAmount = null;
        this.minimumAssuredDate = null;
        this.variablePayoutObj.minAssuredValidityDate = null;
        return;
      }
    }

    if (!this.variablePayoutObj.minAssuredAmount) {
      this.minimumAssuredDate = null;
      this.variablePayoutObj.minAssuredValidityDate = null;
    } else if (parseInt(this.variablePayoutObj.minAssuredAmount, 10) === 0) {
      this.minimumAssuredDate = null;
      this.variablePayoutObj.minAssuredValidityDate = null;
    }
  }

  maxPayoutAmoutChange() {
    if (this.variablePayoutObj.maxPayoutAmount) {
      const maxPayoutAmount = this.variablePayoutObj.maxPayoutAmount.split('.');
      if (maxPayoutAmount[0].length > 8) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.maxPayoutAmountValidity');
        this.variablePayoutObj.maxPayoutAmount = null;
        return;
      }
    }
  }

  isNumeric(event) {
    if (event.which === 13) {
      return;
    }
    const value = event.target.value;
    if (event.which !== 46 && (event.which < 48 || event.which > 57)) {
      event.preventDefault();
    }
    if (value.length > 1 && event.which === 46) {
      const findsDot = new RegExp(/\./g);
      const containsDot = value.match(findsDot);
      if (containsDot) {
        event.preventDefault();
      }
    }
    if (isNaN(value)) {
      event.preventDefault();
    }
  }

  applicableServicesModal(i) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary about-product-popup'
    };
    this.modalRef = this.modalService.open(ApplicableServicesComponent, ngbModalOptions);
    this.modalRef.componentInstance.stayBenefitServicesListFromParent =
      this.variablePayoutObj &&
      this.variablePayoutObj.lengthOfStayBenefits &&
      this.variablePayoutObj.lengthOfStayBenefits[i] &&
      this.variablePayoutObj.lengthOfStayBenefits[i].stayBenefitServices;
    this.modalRef.result.then(
      res => {
        this.variablePayoutObj.lengthOfStayBenefits[i].stayBenefitServices = res;
        this.form.control.markAsDirty();
      },
      () => {}
    );
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  limitToTwoDecimals(event) {
    this.isNumeric(event);
    if (event && event.target && event.target.value) {
      const val = '' + event.target.value;
      const checkDecimal = val.split('.');
      if (checkDecimal && checkDecimal.length > 1) {
        if (checkDecimal[1].length > 1) {
          event.preventDefault();
        }
      }
    }
  }
  public checkIsValidPercentage(event, fieldName) {
    // eslint-disable-next-line no-console
    if (event && event.target && event.target.value && event.target.value > 100) {
      this.jhiAlertService.clear();
      this.onError({ description: 'Percentage should not more than 100.' });
      if (fieldName === 'departmentRevenueBenefit.payoutPercentage') {
        this.departmentRevenueBenefit.payoutPercentage = undefined;
      } else {
        if (fieldName === 'departmentRevenueBenefitUpdate.payoutPercentage') {
          this.departmentRevenueBenefitUpdate.payoutPercentage = undefined;
        }
      }
    }
  }

  contractEndDateOptions() {
    if (this.commencementDate && this.commencementDate.date) {
      const d: Date = new Date(this.dateConverter.toDateString(this.commencementDate.date));
      d.setDate(d.getDate());
      const copy = this.getCopyOfOptions(this.ngxDateOptions);
      copy.disableUntil = {
        year: d.getFullYear(),
        month: d.getMonth() + 1,
        day: d.getDate()
      };
      this.ngxDateOptions = copy;
    }
  }

  getCopyOfOptions(options): IMyOptions {
    return JSON.parse(JSON.stringify(options));
  }

  upperLimitChanged() {
    if(this.departmentRevenueBenefit.upperLimit) {
      const upperLimitAmount = this.departmentRevenueBenefit.upperLimit.split('.');
      if(upperLimitAmount[0].length > 8) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.upperLimitValidity');
        this.departmentRevenueBenefit.upperLimit = null;
        this.departmentRevenueBenefit.upperLimit = undefined;
        return;
      }
    }
  }
}
