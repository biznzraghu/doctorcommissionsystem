import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { SearchTermModify } from 'app/artha-helpers';
import { ValueSetCodeService } from 'app/entities/administration/administration-services/value-set-codes.service';
import { DepartmentPayout, ServiceItemException } from 'app/entities/department-payout/department-payout.model';
import { DepartmentPayoutService } from 'app/entities/department-payout/department-payout.service';
import { Observable } from 'rxjs/Rx';

@Component({
  selector: 'jhi-department-payout-exceptions',
  templateUrl: './department-payout-exceptions.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class DepartmentPayoutExceptionsComponent implements OnInit {
  isExceptions: boolean;
  noRecordFound: boolean;
  searchText: string;
  selectedType: any;
  serviceItemException: ServiceItemException;
  serviceItemExceptionList: any = [];

  serviceSearching = false;
  serviceSelected: any;
  @Input() departmentPayout: DepartmentPayout;
  @Input() formDisabled;
  @Output() exceptionList = new EventEmitter();

  // typeList = ['Select', 'Service', 'Service Group', 'Item with Brand', 'Item with Generic', 'SponsorType', 'Sponsor', 'Plan'];
  typeList = [
    { code: 'Service', name: 'Service' },
    { code: 'ServiceGroup', name: 'Service Group' },
    { code: 'ItemWithBrand', name: 'Item with Brand' },
    { code: 'ItemWithGeneric', name: 'Item with Generic' },
    { code: 'SponsorType', name: 'Sponsor Type' },
    { code: 'Sponsor', name: 'Sponsor' },
    { code: 'Plan', name: 'Plan' }
  ];
  constructor(
    private router: Router,
    private departmentPayoutService: DepartmentPayoutService,
    private searchTermModify: SearchTermModify,
    private valueSetCodeService: ValueSetCodeService
  ) {
    this.isExceptions = false;
    this.noRecordFound = true;
    this.serviceItemException = new ServiceItemException();
  }

  ngOnInit() {
    if (!this.departmentPayout.id || this.departmentPayout.serviceItemExceptions.length > 0) {
      this.selectedType = this.typeList[0];
      this.serviceItemExceptionList = [];
      this.serviceItemExceptionList = this.departmentPayout.serviceItemExceptions;
      this.noRecordFound = false;
    } else {
      this.serviceItemExceptionList = this.departmentPayout.serviceItemExceptions;
      if (this.serviceItemExceptionList.length > 0) {
        this.isExceptions = true;
        this.noRecordFound = false;
      } else {
        this.isExceptions = false;
        this.noRecordFound = true;
        this.selectedType = this.typeList[0];
      }
    }
  }

  addExceptions() {
    if (this.serviceItemException && this.serviceItemException.exceptionType !== null && this.serviceItemException.value !== null) {
      this.serviceItemException.level = 'DocumentLevel';
      this.serviceItemExceptionList.push(this.serviceItemException);
      this.serviceItemException = new ServiceItemException();
      this.serviceSelected = null;
      this.noRecordFound = false;
      this.selectedType = null;

      this.exceptionList.emit(this.serviceItemExceptionList);
    }
  }

  clearSearch() {
    this.serviceSelected = null;
  }

  onChangeType(event) {
    const data = event;
    if (data !== 'Select' && data !== null && data !== undefined) {
      this.serviceItemException.exceptionType = this.selectedType;
    } else {
      this.serviceItemException.exceptionType = null;
    }
  }

  searchUserTypehead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => (this.serviceSearching = term.length >= 1))
      .switchMap(term =>
        term.length >= 1 && this.selectedType === 'Service'
          ? this.departmentPayoutService
              .searchService({ query: 'active:true ' + ' AND (name:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
          : term.length >= 1 && this.selectedType === 'ServiceGroup'
          ? this.departmentPayoutService
              .searchServiceGroup({
                query: 'active:true AND context: Service_Group' + ' AND ' + this.searchTermModify.modify(term)
              })
              .map((res: any) => {
                return res.body;
              })
          : term.length >= 1 && this.selectedType === 'ItemWithBrand'
          ? this.departmentPayoutService
              .searchItemBrand({ query: 'active:true ' + ' AND (name:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
          : // : term.length >= 1 && this.selectedType === 'ItemWithGeneric'
          // ? this.departmentPayoutService
          //     .searchService({ query: 'active:true ' + ' AND (name:' + this.searchTermModify.modify(term) + ')' })
          //     .map((res: any) => {
          //       return res.body;
          //     })
          term.length >= 1 && this.selectedType === 'SponsorType'
          ? this.valueSetCodeService
              .search({ query: 'active:true AND valueSet.code:1.25.2.1.228' + ' AND (display:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
          : term.length >= 1 && this.selectedType === 'Sponsor'
          ? this.departmentPayoutService
              .searchSponsore({ query: 'active:true' + ' AND (name:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
          : term.length >= 1 && this.selectedType === 'Plan'
          ? this.departmentPayoutService
              .searchPlan({ query: 'active:true ' + ' AND (name:' + this.searchTermModify.modify(term) + ')' })
              .map((res: any) => {
                return res.body;
              })
          : Observable.of([])
      )
      .do(() => (this.serviceSearching = false));

  displayFormatUserTypeheadData = (x: any) => {
    if (x.display !== null && x.display !== undefined) {
      return x.code + '|' + x.display;
    } else {
      return x.code + '|' + x.name;
    }
  };

  inputFormatUserTypeheadData = (x: any) => {
    return x.display ? x.display : x.name;
  };

  onSelectUserfromTyeahead(event) {
    const data = event;
    if (data !== 'Select' && data !== null && data !== undefined) {
      this.serviceItemException.value.name = data.item.display ? data.item.display : data.item.name;
      this.serviceItemException.value.code = data.item.code;
    } else {
      this.serviceItemException.value = null;
    }
  }

  deleteRange(pRange) {
    if (pRange === undefined) {
      return;
    }
    const index: number = this.serviceItemExceptionList.indexOf(pRange, 0);
    if (index > -1) {
      this.serviceItemExceptionList.splice(index, 1);
    }
  }

  displayExcptParams(serviceExp) {
    const excpt = serviceExp.exceptionType;
    for (let i = 0; i < this.typeList.length; i++) {
      if (this.typeList[i].code === excpt) {
        return this.typeList[i].name;
      }
    }
  }
}
