import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { NgbModalOptions, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AddExceptionsDialogComponent } from './variable-payout-rules-add-exceptions-modal/variable-payout-rules-add-exceptions.component';
import { ServiceItemBenefit, ExceptionSponsor } from 'app/entities/artha-models/variable-payout.model';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import {
  VisitType,
  DiscountType,
  BeneficiaryType,
  PatientCategory,
  MaterialAmount,
  Type
} from 'app/entities/artha-models/variable-payout.enum';
import { JhiAlertService } from 'ng-jhipster';
import { AddRuleControlComponent } from './add-rule-control.component';
import { AddRuleHelperService } from './add-rule-control-helper.service';
import { VariablePayoutService } from '../variable-payout.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { TemplateValidationService } from '../variable-payout-template/template-validation.service';

@Component({
  selector: 'jhi-variable-payout-rules',
  templateUrl: './variable-payout-rules.component.html',
  providers: [AddRuleHelperService]
})
export class VariablePayoutRulesComponent implements OnInit {
  isRules: boolean;
  noRecordFound: boolean;
  @ViewChild(AddRuleControlComponent) public addRuleComponent: AddRuleControlComponent;
  // rulesDataInstance hole the data from variable-payout-template-dialog.component
  @Input() rulesDataInstance: Array<ServiceItemBenefit>;
  @Input() viewMode: boolean;
  @Input() selectedUserId: any;
  @Input() organizationCode: any;
  @Input() parentData: any;
  public newRuleData: ServiceItemBenefit;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  public VisitTypeEnum = VisitType;
  public TypeEnum = Type;
  public DiscountTypeEnum = DiscountType;
  public BeneficiaryTypeEnum = BeneficiaryType;
  public PatientCategoryEnum = PatientCategory;
  public MaterialAmountEnum = MaterialAmount;
  modalRef: NgbModalRef;
  public editRowIndex: number;
  public editRuleData: ServiceItemBenefit;
  public deepShadoweditRuleData: ServiceItemBenefit;
  public activeDepartments: any[] = [];

  constructor(
    private jhiAlertService: JhiAlertService,
    private addRuleHelperService: AddRuleHelperService,
    private variablePayoutService: VariablePayoutService,
    private modalService: NgbModal,
    private httpBlockerService: HttpBlockerService,
    private templateValidationService: TemplateValidationService
  ) {
    this.newRuleData = new ServiceItemBenefit();
    this.isRules = false;
    this.noRecordFound = true;
  }

  ngOnInit() {
    this.getDepartments();
  }
  public setInfiniteScrollHeight() {
    if (this.selectedUserId) {
      const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
      const patientDetailHeader = this.getElementByClassName('patient-header-container')
        ? this.getElementByClassName('patient-header-container')
        : 56;
      const tabHeaderHeight = this.getElementByClassName('variable-payout-tabs') ? this.getElementByClassName('variable-payout-tabs') : 40;
      const fixedTabelHeaderHeight = this.getElementByClassName('fixed-tbl-head-1') ? this.getElementByClassName('fixed-tbl-head-1') : 46;
      const addRuleControls = this.getElementByClassName('add-rule-controls-1') ? this.getElementByClassName('add-rule-controls-1') : 0;
      const templateSearchtContainer = this.getElementByClassName('template-search-container')
        ? this.getElementByClassName('template-search-container')
        : 62;
      const fixedFooter = this.getElementByClassName('button-container') ? this.getElementByClassName('button-container') : 29;
      return (
        window.innerHeight -
        headerElementHeight -
        patientDetailHeader -
        tabHeaderHeight -
        fixedTabelHeaderHeight -
        templateSearchtContainer -
        fixedFooter -
        addRuleControls -
        13
      );
    } else {
      const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;

      const codeNameDivHeight = this.getElementByClassName('template-header-container')
        ? this.getElementByClassName('template-header-container')
        : 48;
      const tabHeaderHeight = this.getElementByClassName('variable-payout-tabs') ? this.getElementByClassName('variable-payout-tabs') : 40;
      const fixedTabelHeaderHeight = this.getElementByClassName('fixed-tbl-head-1') ? this.getElementByClassName('fixed-tbl-head-1') : 46;
      const addRuleControls = this.getElementByClassName('add-rule-controls-1') ? this.getElementByClassName('add-rule-controls-1') : 0;
      const fixedFooter = this.getElementByClassName('button-container') ? this.getElementByClassName('button-container') : 29;
      return (
        window.innerHeight -
        headerElementHeight -
        codeNameDivHeight -
        tabHeaderHeight -
        fixedTabelHeaderHeight -
        fixedFooter -
        addRuleControls -
        13
      );
    }
  }
  private getElementByClassName(elementName: string): number {
    const element: any = document.getElementsByClassName(elementName)[0];
    return element && element.offsetHeight ? element.offsetHeight : 0;
  }

  getDepartments() {
    this.addRuleHelperService
      .getDepartmentList(this.organizationCode, this.selectedUserId)
      .then((departments: any) => {
        if (departments) {
          this.activeDepartments = departments;
        }
      })
      .catch(err => {
        this.customError(err.error);
      });
  }
  onAddNewRule(data?: any) {
    if (this.editRowIndex) {
      this.customError('Either save or cancel <b> Updating record </b>');
    } else {
      if (data && data.rule && this.parentData) {
        this.selectedUserId ? (data.rule.variablePayout = this.parentData) : (data.rule.planTemplate = this.parentData);
        data.rule.variablePayoutBaseId = this.parentData && this.parentData.variablePayoutId ? this.parentData.variablePayoutId : undefined;
        data.rule.validTo = this.parentData && this.parentData.version;
        this.httpBlockerService.enableHttpBlocker(true);
        const deepArray: any[] = this.rulesDataInstance ? JSON.parse(JSON.stringify(this.rulesDataInstance)) : [];
        this.templateValidationService
          .isSameRuleExist(data.rule, deepArray)
          .then(ruleData => {
            if (ruleData) {
              // eslint-disable-next-line no-console
              // console.log(' after resolve ruleData ', ruleData);
              // this.httpBlockerService.enableHttpBlocker(false);
              this.variablePayoutService.saveServiceItemBenefits(data.rule).subscribe(
                (res: any) => {
                  if (res) {
                    this.httpBlockerService.enableHttpBlocker(false);
                    if (res && res.body) {
                      this.rulesDataInstance.unshift(res.body);
                    } else {
                      this.rulesDataInstance.unshift(this.newRuleData);
                    }
                  }
                  this.newRuleData = new ServiceItemBenefit();
                },
                (error: any) => {
                  this.httpBlockerService.enableHttpBlocker(false);
                  this.jhiAlertService.error(error);
                }
              );
            }
          })
          .catch(err => {
            // eslint-disable-next-line no-console
            // console.log(' after err ', err);
            this.newRuleData = new ServiceItemBenefit();
            this.httpBlockerService.enableHttpBlocker(false);
            if (err) {
              this.customError(`<b>${err}</b>!`);
            } else {
              this.customError('<b>Rule alredy exist</b> with same type and value!');
            }
          });
      }
    }
  }

  trackId(item: any) {
    return item.id;
  }
  deleteRuleRow(index) {
    const serviceItemId = this.rulesDataInstance[index].id;
    this.httpBlockerService.enableHttpBlocker(true);
    this.variablePayoutService.deleteServiceItemBenefit(serviceItemId).subscribe(
      (res: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        if (res) {
          this.rulesDataInstance.splice(index, 1);
        }
      },
      error => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.jhiAlertService.error(error);
      }
    );
  }

  editRuleRow(index) {
    this.editRowIndex = index;
    // const ruleData = { ...this.rulesDataInstance[index] };
    const ruleData = JSON.parse(JSON.stringify(this.rulesDataInstance[index]));
    this.deepShadoweditRuleData = JSON.parse(JSON.stringify(this.rulesDataInstance[index]));
    this.editRuleData = ruleData;
  }
  public saveUpdates(data?) {
    if (data && data.rule && this.parentData) {
      this.selectedUserId ? (data.rule.variablePayout = this.parentData) : (data.rule.planTemplate = this.parentData);
      this.httpBlockerService.enableHttpBlocker(true);
      data.rule.validTo = this.parentData && this.parentData.version;
      // this.rulesDataInstance.splice(index, 1);
      const deepArray: any[] = this.rulesDataInstance ? JSON.parse(JSON.stringify(this.rulesDataInstance)) : [];
      deepArray.splice(this.editRowIndex, 1);
      // eslint-disable-next-line no-console
      // console.log(' after resolve rule ', data.rule);
      // eslint-disable-next-line no-console
      // console.log(' after resolve deepArray ', deepArray);
      // eslint-disable-next-line no-console
      // console.log(' after resolve rulesDataInstance ', this.rulesDataInstance);
      this.templateValidationService
        .isSameRuleExist(data.rule, deepArray)
        .then(ruleData => {
          if (ruleData) {
            // eslint-disable-next-line no-console
            // console.log(' after resolve isSameRuleExist ', ruleData);
            // this.httpBlockerService.enableHttpBlocker(false);
            this.variablePayoutService.updateServiceItemBenefits(data.rule).subscribe(
              (res: any) => {
                this.httpBlockerService.enableHttpBlocker(false);
                if (res) {
                  if (data && data.rule) {
                    this.rulesDataInstance[this.editRowIndex] = data.rule;
                  } else {
                    this.rulesDataInstance[this.editRowIndex] = this.editRuleData;
                  }
                }
                this.resetEditVariables();
              },
              (error: any) => {
                this.httpBlockerService.enableHttpBlocker(false);
                this.jhiAlertService.error(error);
                this.rulesDataInstance[this.editRowIndex] = this.deepShadoweditRuleData;
                this.resetEditVariables();
              }
            );
          }
        })
        .catch(err => {
          this.httpBlockerService.enableHttpBlocker(false);
          // this.rulesDataInstance[this.editRowIndex] = this.editRuleData;
          this.rulesDataInstance[this.editRowIndex] = this.deepShadoweditRuleData;
          this.resetEditVariables();
          if (err) {
            this.customError(`<b>${err}</b>!`);
          } else {
            this.customError('<b>Rule alredy exist</b> with same type and value!');
          }
        });
    }
  }
  public cancelUpdateRecord() {
    this.rulesDataInstance[this.editRowIndex] = this.deepShadoweditRuleData;
    this.resetEditVariables();
  }
  public resetEditVariables() {
    this.editRuleData = new ServiceItemBenefit();
    this.deepShadoweditRuleData = new ServiceItemBenefit();
    this.editRowIndex = undefined;
  }
  onAddExceptionsModal(rule) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary about-product-popup'
    };
    this.modalRef = this.modalService.open(AddExceptionsDialogComponent, ngbModalOptions);
    /** Properties in Add Exceptions Dialog Component Dialog
     * sponser : {
          type: this.SponsorType,
          sponsorType: this.sponsorTypeTags,
          plan: this.planLists
        },
        onDeathIncentive: this.onDeathIncentive,
        serviceItemException: {
          servicesList: this.servicesList
        }
     */
    this.modalRef.componentInstance.isEdit = false;
    if (rule && rule.type) {
      this.modalRef.componentInstance.rule = rule;
    }
    // this.modalRef.componentInstance.isEdit = (!this.viewMode) ? true : false;
    if (rule && rule.exceptionSponsor && rule.exceptionSponsor.applicable) {
      this.modalRef.componentInstance.SponsorType = rule.exceptionSponsor.applicable ? 'applicableSponsor' : 'exemptedSponsor';
    }

    if (rule && rule.exceptionSponsor && rule.exceptionSponsor.sponsorType) {
      this.modalRef.componentInstance.sponsorTypeTags = rule.exceptionSponsor.sponsorType;
    }

    if (rule && rule.exceptionSponsor && rule.exceptionSponsor.plans) {
      this.modalRef.componentInstance.planLists = rule.exceptionSponsor.plans;
    }

    if (rule && rule.onDeathIncentive !== undefined) {
      this.modalRef.componentInstance.onDeathIncentive = rule.onDeathIncentive;
    }

    if (rule && rule.serviceItemExceptions) {
      this.modalRef.componentInstance.servicesList = rule.serviceItemExceptions;
    }

    this.modalRef.result.then(
      result => {
        if (result) {
          // eslint-disable-next-line no-console
          // console.log('onAddExceptionsModal result  ', result);
          rule.onDeathIncentive = result.onDeathIncentive;
          rule.exceptionSponsor = new ExceptionSponsor();
          rule.exceptionSponsor.applicable = result.sponser.type;
          if (result.sponser && result.sponser.plan && result.sponser.plan.length > 0) {
            rule.exceptionSponsor.plans = result.sponser.plan;
          }
          if (result.sponser && result.sponser.sponsorType && result.sponser.sponsorType.length > 0) {
            rule.exceptionSponsor.sponsorType = result.sponser.sponsorType;
          }
          if (result.serviceItemException && result.serviceItemException.servicesList) {
            rule.serviceItemExceptions = result.serviceItemException.servicesList;
          }
        }
      },
      () => {}
    );
  }

  customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }
}
