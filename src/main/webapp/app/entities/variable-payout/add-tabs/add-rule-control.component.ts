import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core'; // ViewChild,
import { NgbModalRef, NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { VariablePayoutService } from '../variable-payout.service';
import { JhiAlertService } from 'ng-jhipster';
import { SearchTermModify } from 'app/artha-helpers';
import { AddExceptionsDialogComponent } from './variable-payout-rules-add-exceptions-modal/variable-payout-rules-add-exceptions.component';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ServiceItemBenefit, ExceptionSponsor } from 'app/entities/artha-models/variable-payout.model';
import { ValueSetCode } from 'app/entities/artha-models/value-set-code.model';
import { TemplateValidationService } from '../variable-payout-template/template-validation.service';
import { ArthaEnumHelperService } from 'app/entities/artha-common-services/artha-enum-helper.service';
import { NgForm } from '@angular/forms';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { AddRuleHelperService } from './add-rule-control-helper.service';

@Component({
  selector: 'jhi-add-rule-control',
  templateUrl: './add-rule-control.component.html',
  providers: [AddRuleHelperService]
})
export class AddRuleControlComponent implements OnInit {
  @ViewChild('addRuleForm', { static: false }) addRuleForm: NgForm;
  isRules: boolean;
  noRecordFound: boolean;
  multiSelectDropdownSettings: any;

  visitTypeList = [];
  typeBeneficiary: any = [];
  netGrossList: any = [];
  tariffClassList: any = [];
  patientCategory: any = [];
  materialAmount: any = [];
  serviceList: any;
  modalRef: NgbModalRef;
  selectedVisit: any;
  typeList = [];
  selectedVisitList: any = [];

  valueSearching: Boolean;
  departmentSearching: Boolean;

  selectedType: any;
  selectedValue: any;
  public quantityFromTo: string;
  @Input() rule: ServiceItemBenefit;
  @Output() onAddRecord = new EventEmitter();
  // Variables for edit rule or change rule;
  @Input() isEditControl: boolean;

  @Input() departmentList: any[] = [];

  @Output() onUpdateRecord = new EventEmitter();
  @Output() onCancelUpdateRecord = new EventEmitter();

  selectedDepartment: any[] = [];
  selectedTarrifClass: ValueSetCode[] = [];
  beneficiary: any;
  disableValueField: boolean;
  disableUntilTypeSelect: boolean;
  public isFormDirty: boolean;
  departmentDropdownSettings: IDropdownSettings = {};

  disableQuentityField: boolean;
  disableMatAmtField: boolean;
  disableServiceItemExcetion: boolean;
  disableTariffClass: boolean;

  showValueMultiSelect = false;
  valueMultiSelectDropdownSettings: any;
  valueMultiSelectList: any[] = [];
  selectedValueList: any[] = [];

  constructor(
    private modalService: NgbModal,
    private variablePayoutService: VariablePayoutService,
    private enumHelperService: ArthaEnumHelperService,
    private jhiAlertService: JhiAlertService,
    private searchTermModify: SearchTermModify,
    private templateValidationService: TemplateValidationService,
    private addRuleHelperService: AddRuleHelperService
  ) {
    this.isRules = false;
    this.noRecordFound = true;
    this.valueSearching = false;
    this.disableValueField = true;
    this.disableUntilTypeSelect = true;
  }
  ngOnInit() {
    setTimeout(() => {
      this.addRuleForm.form.markAsPristine();
      this.isFormDirty = this.addRuleForm.form.dirty;
    }, 500);
    this.getVisitList();
    this.getType();
    this.getDiscountType();
    this.getTypeBeneficiary();
    this.getTariffclass();
    this.getPatientCategoryList();
    this.getMaterialAmountList();
    this.getValueListForServiceGroup();
    this.valueMultiSelectDropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: true
    };

    this.multiSelectDropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'display',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: false
    };
    this.departmentDropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'codeName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: true
    };

    if (this.isEditControl) {
      this.disableUntilTypeSelect = false;
      if (this.rule.minQuantity && this.rule.maxQuantity) {
        this.quantityFromTo = this.joinMinMaxQuentity(this.rule.minQuantity, this.rule.maxQuantity);
      }
      if (this.rule.department && this.rule.department.length > 0) {
        this.selectedDepartment = this.rule.department.filter((dept: any) => (dept.codeName = dept.code + '|' + dept.name));
      }
      if (this.rule.tariffClass && this.rule.tariffClass.length > 0) {
        this.selectedTarrifClass = this.rule.tariffClass;
      }
      if (this.rule.visitType) {
        this.selectedVisitList = this.rule.visitType.map(data => ({ id: data, display: data }));
      }

      if (this.rule.type === 'SERVICE_GROUP') {
        this.selectedValue = this.rule.serviceGroup;
      }

      if (this.rule.type) {
        this.mapTypeValue();
      }
    }
  }
  // Supporting or transforming functiona
  private joinMinMaxQuentity(minQuantity: number, maxQuantity: number): string {
    return minQuantity + '-' + maxQuantity;
  }
  private mapTypeValue() {
    this.typeChange();
    this.addRuleHelperService
      .typeValueMapping(this.rule)
      .then((mappedValue: any) => {
        if (mappedValue && mappedValue.selectedValue) {
          this.selectedValue = mappedValue.selectedValue;
          this.disableValueField = false;
        }
      })
      .catch(err => {
        if (err && err.error) {
          this.customError(err.error);
        } else {
          this.disableValueField = true;
        }
      });
  }
  addRules() {
    this.templateValidationService
      .addNewRuleValidation(this.rule, this.selectedValue, this.selectedVisitList)
      .then((validatedRule: any) => {
        this.rule = { ...validatedRule };
        if (this.selectedVisitList.length > 0) {
          this.rule.visitType = [];
          this.selectedVisitList.forEach((data: any) => this.rule.visitType.push(data.id));
        }

        if (this.rule.type === 'SERVICE_GROUP') {
          this.rule.serviceGroup = [];
          const filterServiceGroup: any = [];
          this.selectedValue.forEach((element: any) => {
            const serviceGroup = this.valueMultiSelectList.find((group: any) => group.id === element.id);
            if (serviceGroup) {
              filterServiceGroup.push(serviceGroup);
            }
            this.rule.serviceGroup = filterServiceGroup;
          });
        }

        if (this.selectedDepartment) {
          this.rule.department = [];
          const filterDepartment: any = [];
          this.selectedDepartment.forEach((element: any) => {
            const department = this.departmentList.find((dept: any) => element.id === dept.id);
            if (department) {
              filterDepartment.push(department);
            }
          });
          this.rule.department = filterDepartment;
        }
        if (this.selectedTarrifClass) {
          this.rule.tariffClass = [];
          const filterTariffClass: any = [];
          this.selectedTarrifClass.forEach((element: any) => {
            const tariffClass = this.tariffClassList.find((dept: any) => element.id === dept.id);
            if (tariffClass) {
              filterTariffClass.push(tariffClass);
            }
          });
          this.rule.tariffClass = filterTariffClass;
        }
        this.resetVariables();
        this.onAddRecord.emit({ rule: this.rule });
      })
      .catch(error => {
        if (error && error.message) {
          this.customError(error.message);
          return;
        }
      });
  }
  onClickUpdateRecord() {
    this.templateValidationService
      .addNewRuleValidation(this.rule, this.selectedValue, this.selectedVisitList)
      .then((validatedRule: any) => {
        this.rule = { ...validatedRule };
        if (this.selectedVisitList.length > 0) {
          this.rule.visitType = [];
          this.selectedVisitList.forEach((data: any) => this.rule.visitType.push(data.id));
        }

        if (this.rule.type === 'SERVICE_GROUP') {
          this.rule.serviceGroup = [];
          const filterServiceGroup: any = [];
          this.selectedValue.forEach((element: any) => {
            const serviceGroup = this.valueMultiSelectList.find((group: any) => group.id === element.id);
            if (serviceGroup) {
              filterServiceGroup.push(serviceGroup);
            }
            this.rule.serviceGroup = filterServiceGroup;
          });
        }

        if (this.selectedDepartment) {
          this.rule.department = [];
          const filterDepartment: any = [];
          this.selectedDepartment.forEach((element: any) => {
            const department = this.departmentList.find((dept: any) => element.id === dept.id);
            if (department) {
              filterDepartment.push(department);
            }
          });
          this.rule.department = filterDepartment;
        }
        if (this.selectedTarrifClass) {
          this.rule.tariffClass = [];
          const filterTariffClass: any = [];
          this.selectedTarrifClass.forEach((element: any) => {
            const tariffClass = this.tariffClassList.find((dept: any) => element.id === dept.id);
            if (tariffClass) {
              filterTariffClass.push(tariffClass);
            }
          });
          this.rule.tariffClass = filterTariffClass;
        }
        this.resetVariables();
        this.onUpdateRecord.emit({ rule: this.rule });
      })
      .catch(error => {
        if (error && error.message) {
          this.customError(error.message);
          return;
        }
      });
  }
  resetVariables() {
    this.quantityFromTo = undefined;
    this.selectedDepartment = undefined;
    this.selectedTarrifClass = undefined;
    this.selectedValue = undefined;
    this.selectedVisitList = [];
    // this.rule.type = undefined;
    this.disableValueField = true;
    this.disableMatAmtField = true;
    this.disableQuentityField = true;
    this.disableServiceItemExcetion = true;
    this.disableUntilTypeSelect = true;
    this.disableTariffClass = true;
  }
  onClickCancelUpdate() {
    this.onCancelUpdateRecord.emit();
  }
  getDiscountType() {
    this.enumHelperService.getEnum('api/discount-type', { query: '*' }).then(data => {
      this.netGrossList = data;
    });
  }

  getVisitList() {
    this.enumHelperService.getEnum('api/visit-type', { query: '*' }).then(data => {
      // for multiselect need bindLabel as id and bindValue as display
      this.visitTypeList = this.enumHelperService.getFormatedObject(data, 'id', 'display');
    });
  }

  getPatientCategoryList() {
    this.enumHelperService.getEnum('api/patient-category', { query: '*' }).then(data => {
      // for ng-select need bindLabel as code and bindValue as name
      this.patientCategory = this.enumHelperService.getFormatedObject(data, 'code', 'name');
    });
  }

  getMaterialAmountList() {
    this.enumHelperService.getEnum('api/material-amount', { query: '*' }).then(data => {
      this.materialAmount = this.enumHelperService.getFormatedObject(data, 'code', 'name');
    });
  }

  getType() {
    this.enumHelperService.getEnum('api/type', { query: '*' }).then(data => {
      this.typeList = this.enumHelperService.getFormatedObject(data, 'code', 'name');
    });
  }

  getTypeBeneficiary() {
    this.enumHelperService.getEnum('api/beneficiary-type', { query: '*' }).then(data => {
      this.typeBeneficiary = this.enumHelperService.getFormatedObject(data, 'code', 'name');
    });
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  onAddExceptionsModal() {
    if (this.disableUntilTypeSelect) {
      return;
    }
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary about-product-popup'
    };
    this.modalRef = this.modalService.open(AddExceptionsDialogComponent, ngbModalOptions);
    if (this.rule && this.rule.exceptionSponsor && this.rule.exceptionSponsor.applicable) {
      this.modalRef.componentInstance.SponsorType = this.rule.exceptionSponsor.applicable ? 'applicableSponsor' : 'exemptedSponsor';
    }

    if (this.rule && this.rule.exceptionSponsor && this.rule.exceptionSponsor.sponsorType) {
      this.modalRef.componentInstance.sponsorTypeTags = this.rule.exceptionSponsor.sponsorType;
    }

    if (this.rule && this.rule.exceptionSponsor && this.rule.exceptionSponsor.plans) {
      this.modalRef.componentInstance.planLists = this.rule.exceptionSponsor.plans;
    }

    if (this.rule && this.rule.onDeathIncentive !== undefined) {
      this.modalRef.componentInstance.onDeathIncentive = this.rule.onDeathIncentive;
    }

    if (this.rule && this.rule.serviceItemExceptions) {
      this.modalRef.componentInstance.servicesList = this.rule.serviceItemExceptions;
    }

    if (this.rule && this.rule.patientCategory) {
      this.modalRef.componentInstance.patientCatg = this.rule.patientCategory;
    }

    this.modalRef.componentInstance.disableServiceItemExcetion = this.disableServiceItemExcetion;
    this.modalRef.result.then(
      result => {
        if (result) {
          this.rule.onDeathIncentive = result.onDeathIncentive;
          this.rule.exceptionSponsor = new ExceptionSponsor();
          this.rule.exceptionSponsor.applicable = result.sponser.type;
          if (result.sponser && result.sponser.plan && result.sponser.plan.length > 0) {
            this.rule.exceptionSponsor.plans = result.sponser.plan;
          }
          if (result.sponser && result.sponser.sponsorType && result.sponser.sponsorType.length > 0) {
            this.rule.exceptionSponsor.sponsorType = result.sponser.sponsorType;
          }
          if (result.serviceItemException && result.serviceItemException.servicesList) {
            this.rule.serviceItemExceptions = result.serviceItemException.servicesList;
          }
        }
      },
      () => {}
    );
  }

  getValueListForServiceGroup() {
    this.variablePayoutService
      .getValue(this.addRuleHelperService.getValyeApiBasedOnType('SERVICE_GROUP'), {
        query: 'context:Service_Group',
        size: 9999
      })
      .subscribe((res: any) => {
        this.valueMultiSelectList = res.body;
      });
  }

  valueSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.valueSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.variablePayoutService
              .getValue(this.addRuleHelperService.getValyeApiBasedOnType(this.rule.type), {
                query:
                  this.rule.type === 'SERVICE_GROUP'
                    ? 'context:Service_Group' + ' AND ' + this.searchTermModify.modify(term)
                    : this.searchTermModify.modify(term),
                size: 9999
              })
              .map((res: HttpResponse<any>) => {
                if (res.body.length) {
                  res.body.forEach(element => {
                    if (element && element.display) {
                      element.name = element.display;
                    } else {
                      if (element && element.name) {
                        // nothing to do
                        element && element.name ? '' : '';
                      } else {
                        if (element && element.code) {
                          element.name = element.code;
                        }
                      }
                    }
                  });
                  return res.body;
                } else {
                  const data = this.enumHelperService.getFormatedObject(res.body, 'code', 'name');
                  return data;
                }
              })
      )
      .do(() => (this.valueSearching = false));

  inputFormatValueTypeheadData = (x: any) => {
    const valueX = x.name || x.display || '';
    return valueX;
  };
  formatValueTypeheadData = (x: any) => {
    const valueX = x.name || x.display || '';
    return valueX;
  };
  typeChange() {
    this.disableUntilTypeSelect = false;
    this.selectedValue = null;
    if (this.rule.type === 'SERVICE_GROUP') {
      this.showValueMultiSelect = true;
      this.selectedValueList = [];
    } else {
      this.showValueMultiSelect = false;
      this.selectedValueList = [];
    }
    switch (this.rule.type) {
      case 'ALL_SERVICES': {
        this.disableValueField = true;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;

        break;
      }
      case 'SERVICE_TYPE': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;

        break;
      }
      case 'SERVICE_GROUP': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;

        break;
      }
      case 'SERVICE_NAME': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = false;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.rule.serviceItemExceptions = [];

        break;
      }

      case 'ALL_PACKAGES': {
        this.disableValueField = true;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        break;
      }
      case 'PACKAGE_CATEGORY': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        break;
      }
      case 'PACKAGE_GROUP': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;

        break;
      }
      case 'PACKAGE_MINUS_MATERIAL_COST': {
        this.disableValueField = false;
        this.disableMatAmtField = false;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;

        break;
      }
      case 'PACKAGE_NAME': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = false;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;

        break;
      }
      case 'SERVICE_INSIDE_PACKAGE': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = false;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.materialAmount = undefined;
        this.rule.serviceItemExceptions = [];

        break;
      }

      case 'ALL_ITEMS': {
        this.disableValueField = true;
        this.disableMatAmtField = false;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = true;

        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        this.selectedTarrifClass = [];

        break;
      }
      case 'ITEM_CATEGORY': {
        this.disableValueField = false;
        this.disableMatAmtField = false;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = true;

        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        this.selectedTarrifClass = [];

        break;
      }
      case 'ITEM_GROUP': {
        this.disableValueField = false;
        this.disableMatAmtField = false;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = false;
        this.disableTariffClass = true;

        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        this.selectedTarrifClass = [];

        break;
      }
      case 'ITEM_NAME': {
        this.disableValueField = false;
        this.disableMatAmtField = false;
        this.disableQuentityField = false;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = true;

        this.selectedTarrifClass = [];
        this.rule.serviceItemExceptions = [];

        break;
      }
      case 'INVOICE': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.serviceItemExceptions = [];
        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        break;
      }
      case 'Invoice_With_Anaesthesia': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.serviceItemExceptions = [];
        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        break;
      }
      case 'Invoice_With_Surgery': {
        this.disableValueField = false;
        this.disableMatAmtField = true;
        this.disableQuentityField = true;
        this.disableServiceItemExcetion = true;
        this.disableTariffClass = false;

        this.rule.serviceItemExceptions = [];
        this.rule.materialAmount = undefined;
        this.quantityFromTo = undefined;
        this.rule.maxQuantity = undefined;
        this.rule.minQuantity = undefined;
        break;
      }
    }
  }

  private getTariffclass() {
    this.variablePayoutService.getTariffClassList({ query: 'active:true' }).subscribe(
      (res: any) => {
        const data = res.body;
        if (data && data.length) {
          this.tariffClassList = data;
        }
      },
      (error: any) => {
        this.onError(error.json);
      }
    );
  }
  public checkIsAmountPercentage() {
    if (this.rule.amount) {
      const enteredAmount: any = this.rule.amount;
      const isPercent = enteredAmount.indexOf('%');
      if (isPercent > -1) {
        if (isPercent === 0) {
          this.customError('Please enter valid number with % sign, without any space!');
          return;
        }
        const arr: any[] = enteredAmount.split('');
        arr.pop();
        if (isNaN(+arr.join(''))) {
          this.customError('Please enter valid number with % sign, without any space!');
          return;
        }
        if (+arr.join('') < 0) {
          this.customError('Percentage should not less than 0');
          return;
        }
        if (+arr.join('') > 100) {
          this.customError('Percentage should not greater than 100');
          this.rule.amount = null;
          this.rule.amount = undefined;
          return;
        }
      } else {
        if (isNaN(+this.rule.amount)) {
          this.rule.amount = undefined;
          this.customError('Pleae enter valid number!');
          return;
        }
      }
    }
  }

  public fixedAmountChanged() {
    if(this.rule.amount) {
      const fixedRuleAmount = this.rule.amount.split('.');
      if(fixedRuleAmount[0].length > 8) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.fixedAmountValidity'); 
        this.rule.amount = null;
        this.rule.amount = undefined;
        return;
      }
    }
  }

  public quantityFromAndToChanged() {
    if(this.quantityFromTo) {
      const quantityFromToAmount = this.quantityFromTo.split('-');
      if(quantityFromToAmount[0].length > 8) {
        this.jhiAlertService.error('arthaApp.variable-payout.validation.fixedAmountValidity'); 
        return;
      }
     
    }
  }

  public validateQuantityFromTo() {
    this.jhiAlertService.clear();
    this.templateValidationService
      .validateQuantityFromTo(this.quantityFromTo)
      .then((data: any) => {
        const { minQuantity, maxQuantity } = data;
        if (minQuantity && maxQuantity) {
          this.rule.minQuantity = minQuantity;
          // eslint-disable-next-line no-console
          console.log("TOQUANTITY::", this.rule.minQuantity);
          this.rule.maxQuantity = maxQuantity;
          // eslint-disable-next-line no-console
          console.log("FROMQUANTITY::",  this.rule.maxQuantity);
        }
      })
      .catch(err => {
        if (err && err.error) {
          this.customError(err.error);
        }
      });
  }
  private customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }
}
