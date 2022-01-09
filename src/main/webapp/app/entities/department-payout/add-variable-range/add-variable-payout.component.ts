import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PayoutRange } from '../department-payout.model';
import { JhiAlertService } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
@Component({
  selector: 'jhi-add-variable-payout',
  templateUrl: './add-variable-payout.component.html'
})
export class AddVariablePayoutComponent implements OnInit {
  @Input() isRangeAvailable;
  @Input() payoutRangeList: any;
  @Input() isEditMode;

  payoutRange: PayoutRange;
  isEdit = false;
  bFormModificationCheck = false;
  isEnableAdd = false;
  editStayBenefitIndex: number;

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService) {
    this.payoutRange = new PayoutRange();
    if (!this.isRangeAvailable) {
      this.payoutRangeList = [];
    }
  }
  ngOnInit() {
    // this.isEdit = this.isEditMode;
  }

  onClickAddRecord() {
    if (this.payoutRangeList === null || this.payoutRangeList === undefined) {
      this.payoutRangeList = [];
    }
    if (this.payoutRangeList.length === 0) {
      if (Number(this.payoutRange.fromAmount) === 0) {
        this.payoutRangeList.push(this.payoutRange);
      } else {
        this.jhiAlertService.error('arthaApp.department-payout.validation.variableRange');
        return;
      }
    } else {
      const data = this.payoutRangeList.length - 1;
      if (
        Number(this.payoutRange.fromAmount) - this.payoutRangeList[data].toAmount === 1 &&
        Number(this.payoutRange.toAmount) > Number(this.payoutRange.fromAmount)
      ) {
        this.payoutRangeList.push(this.payoutRange);
      } else {
        this.jhiAlertService.error('arthaApp.department-payout.validation.variableValidation');
        return;
      }
    }

    this.payoutRange = new PayoutRange();
    this.isEnableAdd = false;
  }

  editPayoutRange(index) {
    this.editStayBenefitIndex = index;
    // this.isEdit = !this.isEdit;
  }
  addPayoutRang() {
    this.editStayBenefitIndex = undefined;
  }

  process() {
    this.activeModal.close(this.payoutRangeList);
  }

  close() {
    this.activeModal.dismiss();
  }

  deleteRange(pRange) {
    if (pRange === undefined) {
      return;
    }
    const index: number = this.payoutRangeList.indexOf(pRange, 0);
    if (index > -1) {
      this.payoutRangeList.splice(index, 1);
    }
  }

  formModified() {
    this.bFormModificationCheck = true;
    this.checkEnabled();
  }

  checkEnabled() {
    if (
      this.payoutRange.fromAmount !== null &&
      this.payoutRange.fromAmount >= 0 &&
      this.payoutRange.toAmount !== null &&
      this.payoutRange.toAmount > 0 &&
      this.payoutRange.percentage > 0 &&
      this.payoutRange.percentage <= 100
    ) {
      this.isEnableAdd = true;
    } else {
      this.isEnableAdd = false;
    }
  }

  addPercentage($event) {
    if (this.payoutRange.percentage <= 0 || this.payoutRange.percentage > 100) {
      this.jhiAlertService.error('arthaApp.department-payout.validation.percentageRange');
      this.payoutRange.percentage = null;
      return;
    }
  }

  limitToTwoDecimals(event) {
    this.isNumeric(event);
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
}
