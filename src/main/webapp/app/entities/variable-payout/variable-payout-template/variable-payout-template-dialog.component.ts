import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import * as moment from 'moment';
// import { Observable, of } from 'rxjs';
// import { debounceTime, distinctUntilChanged, switchMap, map, tap, finalize } from 'rxjs/operators';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CopyComponent } from './../add-tabs/copy-modal/copy-popup.component';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { VariablePayoutService } from 'app/entities/variable-payout/variable-payout.service';
import { VariablePayoutTemplate } from 'app/entities/artha-models/variable-payout-template.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { TemplateValidationService } from './template-validation.service';
import { Observable } from 'rxjs';
import { VariablePayoutRulesComponent } from '../add-tabs/variable-payout-rules.component';
import { VariablePayoutExceptionsComponent } from '../add-tabs/variable-payout-exceptions.component';
import { ServiceItemBenefit } from 'app/entities/artha-models/variable-payout.model';
import { ExportHelper } from 'app/shared/util/export.helper';
import { HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
@Component({
  selector: 'jhi-variable-payout-template-dialog',
  templateUrl: './variable-payout-template-dialog.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class VariablePayoutTemplateDialogComponent implements OnInit {
  activeTab: number;
  selectedValue: any;
  templateCode: any;
  templateName: any;
  modalRef: NgbModalRef;
  private dataSubscription: any;
  preferences: Preferences;
  showEditButton: boolean;
  isViewMode: boolean;
  isEditMode: boolean;
  unitCode: any;
  public templateId;
  // not in use
  // isTemplateSearching: boolean;
  serviceItemBenefits: Array<ServiceItemBenefit>;
  page: any;
  itemsPerPage: any;
  Unitpredicate: any;
  reverse: any;
  /**
   * variablePayoutTemplate is a master model to hold all the data related to Template
   */
  public variablePayoutTemplate: VariablePayoutTemplate;
  private initVariablePayoutTemplate: VariablePayoutTemplate;

  // child forms dirty check
  @ViewChild(VariablePayoutRulesComponent) private variablPayoutRulesComponent: VariablePayoutRulesComponent;
  @ViewChild(VariablePayoutExceptionsComponent) public addExceptionsComponent: VariablePayoutExceptionsComponent;
  // search rule
  searchRuleText: string;
  modifyPrivileges: Array<string>;

  constructor(
    private router: Router,
    private httpBlockerService: HttpBlockerService,
    private userService: UserService,
    private modalService: NgbModal,
    private activatedRoute: ActivatedRoute,
    private variablePayoutService: VariablePayoutService,
    private stateStorage: StateStorageService,
    private templateValidationService: TemplateValidationService,
    private exportHelper: ExportHelper,
    private jhiAlertService: JhiAlertService
  ) {
    this.variablePayoutTemplate = new VariablePayoutTemplate();
    this.serviceItemBenefits = [];
    this.variablePayoutTemplate.serviceItemExceptions = [];
    this.variablePayoutTemplate.organization = [];
    this.activeTab = 0;
    this.isViewMode = false;
    this.modifyPrivileges = ['103100', '103102'];
    this.page = 0;
    this.itemsPerPage = 20;
    this.Unitpredicate = 'lastModifiedBy.lastModifiedDate';
    this.reverse = false;
    // not in use
    // this.isTemplateSearching = false;
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.preferences = data['preferences'];
      this.showEditButton = data['pageType'] && data['pageType'] === 'Detail' ? true : false;
      this.isViewMode = data['pageType'] && data['pageType'] === 'Detail' ? true : false;
      this.isEditMode = data['pageType'] && data['pageType'] === 'Edit' ? true : false;
      if (this.preferences && this.preferences.organization && this.preferences.organization.code) {
        this.unitCode = this.preferences.organization.code;
      }
      if (data['PayoutTemplateData'] && data['PayoutTemplateData'].body) {
        this.variablePayoutTemplate = data['PayoutTemplateData'].body;
         // eslint-disable-next-line no-console
         console.log("VARIABLE__PAYOUT__TEMPLATE::",this.variablePayoutTemplate);
        if (!this.variablePayoutTemplate.organization) {
          this.variablePayoutTemplate.organization = [];
        }
        if (!this.variablePayoutTemplate.serviceItemExceptions) {
          this.variablePayoutTemplate.serviceItemExceptions = [];
        }
        this.templateId = this.variablePayoutTemplate.id;
        // eslint-disable-next-line no-console
        console.log('PayoutTemplateData === ', data['PayoutTemplateData'].body);
        if (this.templateId) {
          this.getRulesDetails(this.templateId);
        }
      }
    });

    const shadowObj = JSON.stringify(this.variablePayoutTemplate);
    this.initVariablePayoutTemplate = JSON.parse(shadowObj);
  }

  ngOnInit() {}
  // not in use
  // templateSearchTypeahead = (text$: Observable<string>) =>
  //   text$.pipe(
  //     debounceTime(500),
  //     distinctUntilChanged(),
  //     tap(term => {
  //       this.isTemplateSearching = term.length >= 1;
  //     }),
  //     switchMap((term: string) =>
  //       term.length < 1
  //         ? of([])
  //         : this.variablePayoutService
  //             .getResourceServiceItemBenefitTemplates({
  //               query: term.trim() ? term : '*',
  //               size: 9999
  //             })
  //             .pipe(
  //               map((res: any) => {
  //                 return res.body;
  //               })
  //             )
  //     ),
  //     tap(() => (this.isTemplateSearching = false)),
  //     finalize(() => (this.isTemplateSearching = false))
  //   );

  // templateInputFormatter = x => x.templateName;
  // templateResultFormatter = x => x.templateName;

  // onSelectTemplate(event) {
  //   if (event && event.item) {
  //     // this.selectedValue.data = event.item;
  //     this.selectedValue = event.item;
  //     this.templateCode = this.selectedValue.code;
  //   }
  // }
  getRulesDetails(payoutID) {
    const reqObj = {
      query: `planTemplate.id:${payoutID}`
    };
    this.variablePayoutService.getServiceItemBenefitsByPayoutId(reqObj).subscribe((res: any) => {
      // // eslint-disable-next-line no-console
      // console.log('getRulesDetails ', res);
      this.serviceItemBenefits = res && res.body ? res.body : [];
    });
  }
  getTabInfo(tabIndex) {
    this.activeTab = tabIndex;
  }

  cancel() {
    if (this.isEditMode) {
      this.router.navigate([`artha/variable-payouts/variable-payouts-template/${this.templateId}/detail`]);
    } else {
      this.router.navigate(['artha/variable-payouts'], { queryParams: { currentTabIndex: 2 }, replaceUrl: true });
    }
  }

  save() {
    this.httpBlockerService.enableHttpBlocker(true);
    const date = new Date();
    this.variablePayoutTemplate.createdDate = moment(date).format('YYYY-MM-DDTHH:mm:ss');
    const createdBy = this.stateStorage.getValue('preferences');
    if (createdBy && createdBy.user) {
      this.variablePayoutTemplate.createdBy = createdBy.user;
      this.variablePayoutTemplate.status = true;
      this.variablePayoutTemplate.unitCode =
        createdBy && createdBy.organization && createdBy.organization.code ? createdBy.organization.code : '';
    }
    // // eslint-disable-next-line no-console
    // console.log('Save variablePayoutTemplate ', this.variablePayoutTemplate);
    this.templateValidationService
      .saveTemplateValidation(this.variablePayoutTemplate)
      .then((validateTemplate: VariablePayoutTemplate) => {
        if (validateTemplate) {
          // eslint-disable-next-line no-console
          // console.log('Save variablePayoutTemplate ', this.variablePayoutTemplate);
          // this.httpBlockerService.enableHttpBlocker(false);
          this.saveTemplate();
        }
      })
      .catch(() => {
        this.httpBlockerService.enableHttpBlocker(false);
      });
  }
  update() {
    this.httpBlockerService.enableHttpBlocker(true);
    const createdBy = this.stateStorage.getValue('preferences');
    if (createdBy && createdBy.user) {
      this.variablePayoutTemplate.lastModifiedBy = createdBy.user;
      this.variablePayoutTemplate.lastModifiedBy.lastModifiedDate = new Date();
    }
    this.templateValidationService
      .saveTemplateValidation(this.variablePayoutTemplate)
      .then((validateTemplate: VariablePayoutTemplate) => {
        if (validateTemplate) {
          this.updateTemplate();
        }
      })
      .catch(() => {
        this.httpBlockerService.enableHttpBlocker(false);
      });
  }
  private updateTemplate() {
    this.variablePayoutService.updateVariablePayoutTemplate(this.variablePayoutTemplate).subscribe(
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        this.resetTemplateForms();
        // this.variablePayoutTemplate = new VariablePayoutTemplate();
        // this.initVariablePayoutTemplate = new VariablePayoutTemplate();
        // this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form.markAsPristine();
        // this.addExceptionsComponent.addExceptionForm.form.markAsPristine();
        this.router.navigate([`artha/variable-payouts/variable-payouts-template/${this.templateId}/detail`]);
      },
      (error: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        // eslint-disable-next-line no-console
        console.log('createVariablePayoutTemplate', error);
      }
    );
  }
  saveTemplate() {
    this.variablePayoutService.createVariablePayoutTemplate(this.variablePayoutTemplate).subscribe(
      () => {
        this.httpBlockerService.enableHttpBlocker(false);
        // this.variablePayoutTemplate = new VariablePayoutTemplate();
        // this.initVariablePayoutTemplate = new VariablePayoutTemplate();
        this.resetTemplateForms();
        this.router.navigate(['artha/variable-payouts']);
      },
      (error: any) => {
        this.httpBlockerService.enableHttpBlocker(false);
        // eslint-disable-next-line no-console
        console.log('createVariablePayoutTemplate', error);
      }
    );
  }
  private resetTemplateForms() {
    this.variablePayoutTemplate = new VariablePayoutTemplate();
    this.initVariablePayoutTemplate = new VariablePayoutTemplate();
    this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form.markAsPristine();
    this.addExceptionsComponent.addExceptionForm.form.markAsPristine();
  }

  // copy() {
  //   const ngbModalOptions: NgbModalOptions = {
  //     backdrop: 'static',
  //     keyboard: true,
  //     windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
  //   };
  //   this.modalRef = this.modalService.open(CopyComponent, ngbModalOptions);
  //   this.modalRef.componentInstance.isVariablePayout = false;
  //   this.modalRef.componentInstance.templateInfo = this.variablePayoutTemplate;
  //   this.modalRef.result.then(
  //     // result => {
  //     //     if (result) {

  //     //     }
  //     // },
  //     () => {}
  //   );
  // }

  copy() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CopyComponent, ngbModalOptions);
    this.modalRef.componentInstance.isVariablePayout = false;
    this.modalRef.componentInstance.templateInfo = this.variablePayoutTemplate;
    this.modalRef.componentInstance.templateId = this.variablePayoutTemplate.id
    this.modalRef.result.then(
      result => {
          if (result) {
            this.router.navigate([`artha/variable-payouts/variable-payouts-template/${this.variablePayoutTemplate.id}/detail`], { replaceUrl: true });
          }
      },
      () => {}
    );
  }


  editDetail(id) {
    this.router.navigate([`artha/variable-payouts/variable-payouts-template/${id}/edit`], { replaceUrl: true });
  }
  /** Search Rule */
  search(searchTerm) {
    if (searchTerm !== undefined) {
      this.searchRuleText = searchTerm;
      if (this.searchRuleText === undefined) {
        this.searchRuleItem(this.searchRuleText);
      } else if (this.searchRuleText.trim().length === 0) {
        // this.searchRuleItem('*');
        this.clearRuleSearch();
      } else if (this.searchRuleText.trim().length >= 3) {
        this.searchRuleItem(this.convertToSpecialString(this.searchRuleText));
      }
    }
  }

  searchRuleItem(term) {
    // eslint-disable-next-line no-console
    // console.log('searchRuleItem ', term);
    const reqObj: any = {
      query: term + 'AND ' + `planTemplate.id: ${this.templateId}` //  AND version: ${this.variablePayoutObj.version}
    };
    // if (this.variablePayoutObj.changeRequestStatus === 'APPROVED') {
    //   reqObj.isApproved = true;
    // }
    this.variablePayoutService.searchServiceItemBenifits(reqObj).subscribe((res: any) => {
      this.serviceItemBenefits = res && res.body ? res.body : [];
    });
  }
  clearRuleSearch() {
    this.searchRuleText = '';
    if (this.templateId) {
      this.getRulesDetails(this.templateId);
    }
    // eslint-disable-next-line no-console
    // console.log('searchRuleText ', this.searchRuleText);
  }
  convertToSpecialString(strData) {
    let cleanStr = strData.trim();
    cleanStr = cleanStr.replace(/\+/g, ' ');
    cleanStr = cleanStr.replace(/  +/g, ' ');
    cleanStr = cleanStr.replace(/"/g, '');
    // cleanStr = cleanStr.replace(/\:/g, '\\:');
    cleanStr = cleanStr.replace(/\//g, '\\/');
    cleanStr = cleanStr.replace(/\[/g, '\\[');
    cleanStr = cleanStr.replace(/\]/g, '\\]');
    cleanStr = cleanStr.replace(/\(/g, '\\(');
    cleanStr = cleanStr.replace(/\)/g, '\\)');
    const splitStr = cleanStr.split(' ');
    let convertStr = '';
    splitStr.map(function(obj) {
      // function(obj, index?)
      convertStr += '*' + obj + '*' + ' ';
    });
    return convertStr;
  }
  checkUpdates() {
    const mainObj = JSON.stringify(this.variablePayoutTemplate);
    const shadowObj = JSON.stringify(this.initVariablePayoutTemplate);
    // eslint-disable-next-line no-console
    // console.log('this.checkUpdates ', this.variablPayoutRulesComponent);
    // // eslint-disable-next-line no-console
    // console.log('this.addRuleComponent ', this.variablPayoutRulesComponent.addRuleComponent);
    // // eslint-disable-next-line no-console
    // console.log('this.addRuleForm ', this.addExceptionsComponent.addExceptionForm.form.dirty);
    const isAddRuleComponent = this.variablPayoutRulesComponent.addRuleComponent;
    if (isAddRuleComponent) {
      if (
        mainObj === shadowObj &&
        !this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form.dirty &&
        !this.addExceptionsComponent.addExceptionForm.form.dirty
      ) {
        return true;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  exportUnitMapping() {
    this.page = 0;
    this.variablePayoutService
      .exportUnit({
        query: 'id:' + this.variablePayoutTemplate.id,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.Unitsort() 
      })
      .subscribe(
        (res: any) => this.exportHelper.openFile(res.body),
        (res: HttpErrorResponse) => this.onError(res.error)
    );
  }

  Unitsort() {
    const result = [this.Unitpredicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }


  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }


  canDeactivate() {
    return Observable.of(this.checkUpdates());
  }
}
