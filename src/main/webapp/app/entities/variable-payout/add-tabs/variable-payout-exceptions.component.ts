
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { VariablePayoutService } from '../variable-payout.service';
import { SearchTermModify } from 'app/artha-helpers';
import { JhiAlertService } from 'ng-jhipster';
import { ServiceItemException } from '../../artha-models/service-item-exception.model';
import { ServiceItemExceptions } from 'app/entities/artha-models/variable-payout.model';
import { NgForm } from '@angular/forms';
import { ArthaEnumHelperService } from 'app/entities/artha-common-services/artha-enum-helper.service';
import { ValueSetCodeService } from 'app/entities/administration/administration-services/value-set-codes.service';
import { VariableHelperService } from '../variable-payout-helper.service';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { PreferenceService } from './../../../artha-helpers/services/preference.service';
@Component({
  selector: 'jhi-variable-payout-exceptions',
  templateUrl: './variable-payout-exceptions.component.html'
})
export class VariablePayoutExceptionsComponent implements OnInit {
  @ViewChild('addExceptionForm', { static: false }) addExceptionForm: NgForm;
  isExceptions: boolean;
  noRecordFound: boolean;
  searchText: string;
  valueSearching: boolean;

  selectedType: any;
  selectedServiceItemValue: any;
  typeList: any;
  ServiceItemValueSearching: Boolean;
  serviceItemException: ServiceItemException;
  selectedTypeDetails: any;
  @Input() viewMode: boolean;
  @Input() templateExceptions: Array<ServiceItemExceptions>;

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  constructor(
    private variablePayoutService: VariablePayoutService,
    private jhiAlertService: JhiAlertService,
    private searchTermModify: SearchTermModify,
    private enumHelperService: ArthaEnumHelperService,
    private valueSetCodeService: ValueSetCodeService,
    private variableHelperService: VariableHelperService,
    private preferenceService: PreferenceService,
  ) {
    this.isExceptions = false;
    this.noRecordFound = true;
    this.valueSearching = false;

    this.typeList = [];
    this.getServiceItemExceptions();
    this.ServiceItemValueSearching = false;
    this.serviceItemException = new ServiceItemException();
  }

  ngOnInit() {
    if (this.templateExceptions && this.templateExceptions.length === 0) {
      this.noRecordFound = true;
    } else {
      this.noRecordFound = false;
    }
  }

  getServiceItemExceptions() {
    this.enumHelperService.getEnum('api/exception-type', { query: '*' }).then(
      data => {
        this.typeList = this.enumHelperService.getFormatedObject(data, 'code', 'name');
      },
      (error: any) => {
        this.onError(error.json);
      }
    );
  }

  ServiceItemValueSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.ServiceItemValueSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.selectedType !== 'SponsorType'
          ? this.variablePayoutService
              // .getValue(this.selectedTypeDetails.api, { query: `${this.selectedTypeDetails.query} ${this.searchTermModify.modify(term)})` })
              .getValue(this.selectedTypeDetails.api, {
                // query: `${this.selectedTypeDetails.query} ${this.searchTermModify.modify(term)} OR code:${this.searchTermModify.modify(
                //   term
                query: `${this.selectedTypeDetails.query} ${this.preferenceService.modify(term)} OR code:${this.preferenceService.modify(
                  term
                )})`,
                size: 9999
              })
              .map((res: HttpResponse<any>) => {
                return res.body;
              })
          : this.valueSetCodeService
              .search({ query: 'active:true AND valueSet.code:1.25.2.1.228' + ' AND (display:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
      )
      .do(() => (this.ServiceItemValueSearching = false));

  inputFormatServiceItemValueTypeheadData = (x: any) => {
    return x.display ? x.display : x.name;
  };
  formatServiceItemValueTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  selectedTypeChange() {
    this.selectedServiceItemValue = null;
    this.selectedTypeDetails = this.variableHelperService.getServiceItemExceptionValueApiBasedOnType(this.selectedType);
  }
  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  addExceptions() {
    if (this.selectedType === undefined || this.selectedType === null || this.selectedType === '') {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectType');
    } else if (
      this.selectedServiceItemValue === undefined ||
      this.selectedServiceItemValue === null ||
      this.selectedServiceItemValue === ''
    ) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectValue');
    } else {
      this.serviceItemException.exceptionType = this.selectedType;
      this.serviceItemException.value.code =
        this.selectedServiceItemValue && this.selectedServiceItemValue.code ? this.selectedServiceItemValue.code : '-';
      this.serviceItemException.value.name = this.selectedServiceItemValue
        ? this.selectedServiceItemValue.name || this.selectedServiceItemValue.display
        : '-';
      const serviceItem = { ...this.serviceItemException };
      if (
        this.templateExceptions.find(
          (ele: any) =>
            ele.exceptionType === this.selectedType &&
            ele.value.name &&
            ele.value.name === (this.selectedServiceItemValue.name || this.selectedServiceItemValue.display)
        )
      ) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.exceptionExist');
        return;
      }
      this.templateExceptions.unshift(serviceItem);
      this.selectedType = null;
      this.selectedServiceItemValue = null;
      this.serviceItemException = new ServiceItemException();
      if (this.templateExceptions && this.templateExceptions.length > 0) {
        this.noRecordFound = false;
      }
    }
  }

  search(event) {
    this.searchText = event;
  }

  clearSearch() {
    this.searchText = null;
  }

  removeException(index: number) {
    this.templateExceptions.splice(index, 1);
  }
}
