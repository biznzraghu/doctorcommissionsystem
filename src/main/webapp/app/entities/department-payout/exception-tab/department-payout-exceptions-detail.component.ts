import { Component, Input, OnInit } from '@angular/core';
import { DepartmentPayout, ServiceItemException } from 'app/entities/department-payout/department-payout.model';

@Component({
  selector: 'jhi-department-payout-exceptions-detail',
  templateUrl: './department-payout-exceptions-detail.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class DepartmentPayoutExceptionsDetailComponent implements OnInit {
  isExceptions: boolean;
  noRecordFound: boolean;
  selectedType: any;
  serviceItemException: ServiceItemException;
  serviceItemExceptionList: any = [];

  @Input() departmentPayout: DepartmentPayout;

  typeList = [
    { code: 'Service', name: 'Service' },
    { code: 'ServiceGroup', name: 'Service Group' },
    { code: 'ItemWithBrand', name: 'Item with Brand' },
    { code: 'ItemWithGeneric', name: 'Item with Generic' },
    { code: 'SponsorType', name: 'Sponsor Type' },
    { code: 'Sponsor', name: 'Sponsor' },
    { code: 'Plan', name: 'Plan' }
  ];

  constructor() {
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
      }
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
