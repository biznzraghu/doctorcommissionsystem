import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { VariablePayoutService } from '../../variable-payout.service';
import { JhiAlertService } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { SearchTermModify } from 'app/artha-helpers';
import { ServiceItemException } from './../../../artha-models/service-item-exception.model';
import { ValueSetCodeService } from './../../../administration/administration-services/value-set-codes.service';
import { ArthaEnumHelperService } from 'app/entities/artha-common-services/artha-enum-helper.service';
import { VariableHelperService } from '../../variable-payout-helper.service';

@Component({
  selector: 'jhi-variable-payout-rules-add-exceptions-dialog',
  templateUrl: './variable-payout-rules-add-exceptions.component.html'
})
export class AddExceptionsDialogComponent implements OnInit {
  activeTab: number;
  SponsorType: any;
  sponsorTypeSearching: Boolean;
  planTypeSearching: Boolean;
  sponsorTypeTags: Array<any>;
  planLists: any = [];
  servicesList: Array<any>;
  selectedType: any;
  selectedServiceItemValue: any;
  typeList: any;
  ServiceItemValueSearching: Boolean;
  serviceItemException: ServiceItemException;
  planSearching: boolean;
  planSearch: any;
  sponsorTypeLists: any;
  sponsorTypeSearch: any;
  onDeathIncentive = false;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  @Input() isEdit = true;
  disableServiceItemExcetion: boolean;
  selectedTypeDetails: any;
  rule: any;
  @Input() patientCatg: any;


  constructor(
    public activeModal: NgbActiveModal,
    private variablePayoutService: VariablePayoutService,
    private jhiAlertService: JhiAlertService,
    private searchTermModify: SearchTermModify,
    private enumHelperService: ArthaEnumHelperService,
    private valueSetCodeService: ValueSetCodeService,
    private variableHelperService: VariableHelperService
  ) {
    this.activeTab = 0;
    // this.SponsorType = 'applicableSponsor';
    this.sponsorTypeSearching = false;
    this.planTypeSearching = false;
    this.planSearching = false;
    this.sponsorTypeTags = [];
    this.servicesList = [];
    this.typeList = [];
    this.getServiceItemExceptions();
    this.ServiceItemValueSearching = false;
    this.serviceItemException = new ServiceItemException();
  }

  ngOnInit() {
    // For Initialization
    if(this.patientCatg === 'CASH') {
      this.SponsorType = null;
    } else {
      this.SponsorType = 'applicableSponsor';
    }
  }

  getTabInfo(tabIndex) {
    this.activeTab = tabIndex;
  }

  no() {
    this.activeModal.close(false);
  }

  yes() {
    this.activeModal.close(true);
  }

  save() {
    if (this.isEdit) {
      this.activeModal.close({
        sponser: {
          type: this.SponsorType === 'applicableSponsor' ? true : false,
          sponsorType: this.sponsorTypeTags,
          plan: this.planLists
        },
        onDeathIncentive: this.onDeathIncentive,
        serviceItemException: {
          servicesList: this.servicesList
        }
      });
    } else {
      const errorMessage = '<strong>This is view mode, you can not modify data.</strong>';
      this.customError(errorMessage);
    }
  }

  searchSponsorTypeTypehead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.sponsorTypeSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.valueSetCodeService.getSponsorType().map((res: HttpResponse<any>) => {
              return res.body;
            })
      )
      .do(() => (this.sponsorTypeSearching = false));

  inputFormatSponsorTypeheadData = (x: any) => {
    const valueX = x.display !== undefined ? x.display : '';
    return valueX;
  };
  formatSponsorTypeheadData = (x: any) => {
    const valueX = x.display !== undefined ? x.display : '';
    return valueX;
  };

  selectedSponsorType(e) {
    if (e && e.item) {
      const selectedSponsor = e.item;
      if (this.sponsorTypeTags.find(ele => ele.id === selectedSponsor.id)) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.sponsorTypeExist');
        return;
      }
      this.sponsorTypeTags.push(selectedSponsor);
      this.sponsorTypeSearch = { item: { name: '' } };
    }
  }

  planSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.planSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.variablePayoutService.getPlan({ 
            query: this.searchTermModify.modify(term),
            size: 9999
          })
              .map((res: HttpResponse<any>) => {
              return res.body;
            })
      )
      .do(() => (this.planSearching = false));

  inputFormatPlanTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatPlanTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  selectedPlan(e) {
    if (e && e.item) {
      const selectedPlan = e.item;
      if (this.planLists.find(ele => ele.id === selectedPlan.id)) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.planExist');
        return;
      }
      this.planLists.push(selectedPlan);
      this.planSearch = { item: { name: '' } };
    }
  }

  removeSponsorType(i) {
    this.sponsorTypeTags.splice(i, 1);
  }

  removePlan(i) {
    this.planLists.splice(i, 1);
  }

  removeService(i) {
    this.servicesList.splice(i, 1);
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

  getServiceItemValueApiBasedOnType() {
    let api: string;
    if (this.selectedType === 'Service') {
      api = 'api/service-masters';
    }
    return api;
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
              .getValue(this.selectedTypeDetails.api, {
                query: `${this.selectedTypeDetails.query} (${this.searchTermModify.modify(term)}) OR code: (${this.searchTermModify.modify(
                  term
                )}))`
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

  addServices() {
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
      const name = this.selectedServiceItemValue.display ? this.selectedServiceItemValue.display : this.selectedServiceItemValue.name;
      const code = this.selectedServiceItemValue.code ? this.selectedServiceItemValue.code : '';
      this.serviceItemException.value.name = name;
      this.serviceItemException.value.code = code;
      const newException = { ...this.serviceItemException };
      if (this.servicesList.find((ele: any) => ele.exceptionType === this.selectedType && ele.value.code && ele.value.code === code)) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.exceptionExist');
        return;
      } else {
        this.servicesList.unshift(newException);
        this.selectedType = null;
        this.selectedServiceItemValue = null;
        this.serviceItemException = new ServiceItemException();
      }
    }
  }
  customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }
}
