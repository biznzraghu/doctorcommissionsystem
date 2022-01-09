import { Component, OnInit, ViewChild, TemplateRef, ElementRef, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ArthaDetailHeaderOptions } from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { JhiAlertService } from 'ng-jhipster';
import { NgbModalOptions, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CopyComponent } from './add-tabs/copy-modal/copy-popup.component';
import { TemplateComponent } from './add-tabs/template-modal/template-popup.component';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { VariablePayout, UserDTO, ServiceItemBenefit, ServiceItemExceptions } from '../artha-models/variable-payout.model';
import { VariablePayoutService } from './variable-payout.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, map, tap, finalize } from 'rxjs/operators';
import { CommentDialogComponent } from 'app/layouts/navbar/dialogs/comments-dialog/comments-dialog.component';
import { VariablePayoutBasicDetailsComponent } from './add-tabs/variable-payout-basic-details.component';
import { VariablePayoutDeleteComponent } from './variable-payout-delete/variable-payout-delete.component';
import { VariablePayoutRulesComponent } from './add-tabs/variable-payout-rules.component';
import { VariablePayoutExceptionsComponent } from './add-tabs/variable-payout-exceptions.component';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { DialogService } from 'app/artha-helpers';
import { fromPromise } from 'rxjs/internal-compatibility';
import { AthmaDateConverter, SearchTermModify } from 'app/artha-helpers';
import { AccountService } from 'app/core/auth/account.service';
import { DepartmentService } from '../administration/administration-services/department.service';
@Component({
  selector: 'jhi-variable-payout-dialog',
  templateUrl: './variable-payout-dialog.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class VariablePayoutDialogComponent implements OnInit, AfterViewInit {
  activeTab: number;
  empList: any;
  selectedEmp: any = [];
  isEmployeeSelected: boolean;
  modalRef: NgbModalRef;
  preferences: Preferences;
  private dataSubscription: any;
  isTemplateSearching: boolean;
  selectedTemplate: any;
  templateLists: any = [];
  templateSearch: any;
  allowToEditDocument: boolean;
  modifyPrivileges: Array<string>;

  @ViewChild('headerView', { static: false }) headerView: TemplateRef<any>;
  public options: ArthaDetailHeaderOptions;
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  unitCode: string;
  getversionListQry = '*';
  allVersionList: Array<VariablePayout>;
  isEditMode: Boolean;
  showAllBtns: Boolean;

  variablePayoutObj: VariablePayout;
  initVariablePayout: VariablePayout;

  public serviceItemBenefits?: Array<ServiceItemBenefit>;
  public serviceItemExceptions?: Array<ServiceItemExceptions>;

  buttonHeading: any;
  currentStatus: any;
  variablePayoutSavedInfo: any;
  latestVersionInfo: any;
  previouseTerm: string;

  // child forms dirty check
  @ViewChild(VariablePayoutBasicDetailsComponent, { static: false }) private basicDeatailsComponent: VariablePayoutBasicDetailsComponent;
  @ViewChild(VariablePayoutRulesComponent) private variablPayoutRulesComponent: VariablePayoutRulesComponent;
  @ViewChild(VariablePayoutExceptionsComponent) public addExceptionsComponent: VariablePayoutExceptionsComponent;

  // rules search box
  searchRuleText: string;
  sortjsOptions: any;

  showMoreTemplateBtn: boolean;
  showMoreTemplate: boolean;
  constructor(
    private router: Router,
    private mainCommunicationService: MainCommunicationService,
    private userService: UserService,
    private jhiAlertService: JhiAlertService,
    private modalService: NgbModal,
    private activatedRoute: ActivatedRoute,
    private variablePayoutService: VariablePayoutService,
    public departmentService: DepartmentService,
    private httpBlockerService: HttpBlockerService,
    private dialogService: DialogService,
    private searchTermModify: SearchTermModify,
    private accountService: AccountService,
    private elementRef: ElementRef,
    private dateConverter: AthmaDateConverter
  ) {
    this.sortjsOptions = {
      onUpdate: (event: any) => {
        this.postChangesToServer(event);
      }
    };
    this.variablePayoutObj = new VariablePayout();
    this.modifyPrivileges = ['103100', '103102'];
    // new fields for tab separation
    this.serviceItemBenefits = [];
    this.serviceItemExceptions = [];
    // new fields for tab separation
    this.activeTab = 0;
    this.isTemplateSearching = false;
    this.isEditMode = true;
    this.showAllBtns = true;
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.preferences = data['preferences'];
      if (this.preferences && this.preferences.organization && this.preferences.organization.code) {
        this.unitCode = this.preferences.organization.code;
      }
      if (data['VariablePayoutData'] && data['VariablePayoutData'].body) {
        this.variablePayoutObj = data['VariablePayoutData'].body;
        /* header data binding starts */
        this.isEmployeeSelected = true;
        this.options = new ArthaDetailHeaderOptions();
        this.options.isHeaderForEmployee = true;
        this.options.showSearchIcon = false;
        this.options.remarksListCount = 0;
        this.setHeaderDetails();
        this.getEmployeeDetails(this.variablePayoutObj.employeeId);
        /* header data binding ends */

        /* version list starts */
        this.loadVersionList();
        /* version list ends */

        // reset all data
        this.resetFormData();
      }
    });
    if (this.variablePayoutObj.id === undefined || this.variablePayoutObj.id === null) {
      this.buttonHeading = 'Save Draft';
    } else {
      this.buttonHeading = 'Update Draft';
    }
    if (this.variablePayoutObj.serviceItemBenefitTemplates && this.variablePayoutObj.serviceItemBenefitTemplates.length > 0) {
      this.variablePayoutObj.serviceItemBenefitTemplates.map(ele => {
        this.templateLists.push(ele);
      });
    }

    // take copy
    const shadowObj = JSON.stringify(this.variablePayoutObj);
    this.initVariablePayout = JSON.parse(shadowObj);
  }
  postChangesToServer(event) {
    // eslint-disable-next-line no-console
    // console.log('postChangesToServer', event)
  }
  ngOnInit() {
    this.mainCommunicationService.updateHeaderView(this.headerView);
    // Reset form dirty flags
    // this.resetFormData();
  }
  ngAfterViewInit() {}
  setHeaderDetails() {
    this.options.status = this.variablePayoutObj.changeRequestStatus || 'NEW';
    this.currentStatus = this.options.status;
    this.options.createdBy = this.variablePayoutObj.createdBy;
    this.options.approvedBy = [];
    if (this.variablePayoutObj.transactionApprovalDetails && this.variablePayoutObj.transactionApprovalDetails.length > 0) {
      for (let i = 0; i < this.variablePayoutObj.transactionApprovalDetails.length; i++) {
        this.options.approvedBy.push(this.variablePayoutObj.transactionApprovalDetails[i].approvedBy);
        this.options.approvedBy[i].onDate = this.variablePayoutObj.transactionApprovalDetails[i].approvedDateTime;
        this.options.approvedBy[i].status = this.variablePayoutObj.transactionApprovalDetails[i].type;
      }
      this.options.remarksListCount =
        this.variablePayoutObj.transactionApprovalDetails && this.variablePayoutObj.transactionApprovalDetails.length > 0
          ? this.variablePayoutObj.transactionApprovalDetails.length
          : 0;
    }
    this.options.createdDate = this.variablePayoutObj.createdDate;
    this.options.versionNo = this.variablePayoutObj.version;
    this.options.showVersionNo = true;
    this.options.departmentList = [];
    this.variablePayoutObj.departmentRevenueBenefits.forEach(dept => {
      this.options.departmentList.push(dept.department);
    });
  }

  loadVersionList() {
    if (this.preferences && this.preferences.organization && this.preferences.organization.code) {
      this.unitCode = this.preferences.organization.code;
      this.getversionListQry = `unitCode: ${this.unitCode}`;
      if (this.variablePayoutObj && this.variablePayoutObj.variablePayoutId) {
        this.getversionListQry = this.getversionListQry + ` AND variablePayoutId: ${this.variablePayoutObj.variablePayoutId}`;
        this.variablePayoutService
          .search({
            query: this.getversionListQry,
            size: 200
          })
          .subscribe(
            (res: HttpResponse<any>) => {
              this.allVersionList = res.body || [];
              const getVersionList = [];
              res.body.forEach(item => {
                getVersionList.push(item.version);
              });
              if (getVersionList) {
                getVersionList.sort(function(a, b) {
                  return a - b;
                });
              }
              this.options.versionList = getVersionList || [];
              this.checkViewMode();
            },
            (res: HttpErrorResponse) => {
              this.onError(res.error);
            }
          );
      }
    }
  }
  onVersionChange(data) {
    this.latestVersionInfo = data;
    if (JSON.stringify(this.initVariablePayout) !== JSON.stringify(this.variablePayoutObj)) {
      const modalPromise = this.dialogService.confirm();
      const newObservable = fromPromise(modalPromise);
      newObservable.subscribe(respose => {
        if (respose === true) {
          this.switchVersion(data);
        }
      });
    } else {
      this.switchVersion(data);
    }
  }

  switchVersion(data) {
    const dataObj = this.allVersionList.find(item => {
      return item.version === data;
    });
    const shadowDataObj = JSON.parse(JSON.stringify(dataObj));
    this.variablePayoutObj = shadowDataObj; // dataObj;
    this.templateLists = [];
    if (this.variablePayoutObj.serviceItemBenefitTemplates && this.variablePayoutObj.serviceItemBenefitTemplates.length > 0) {
      this.templateLists = [];
      this.variablePayoutObj.serviceItemBenefitTemplates.map(ele => {
        this.templateLists.push(ele);
      });
    }
    this.setHeaderDetails();
    this.checkViewMode();
    // Reset form dirty flags
    this.resetFormData();
    this.getRulesDetails();
    this.getExceptiionDetails(this.variablePayoutObj.id);
  }

  checkViewMode() {
    if (this.options.versionList && this.options.versionList.length) {
      if (
        this.variablePayoutObj.changeRequestStatus !== 'PENDING_APPROVAL' &&
        this.variablePayoutObj.changeRequestStatus !== 'APPROVED' &&
        this.variablePayoutObj.changeRequestStatus !== 'REJECTED' &&
        this.options.versionList[this.options.versionList.length - 1] === this.variablePayoutObj.version
      ) {
        this.hasAuthority()
          .then(data => {
            if (data) {
              this.isEditMode = true;
            } else {
              this.isEditMode = false;
            }
          })
          .catch(() => {
            this.isEditMode = false;
          });
      } else {
        this.isEditMode = false;
      }

      if (this.options.versionList[this.options.versionList.length - 1] === this.variablePayoutObj.version) {
        this.showAllBtns = true;
      } else {
        this.showAllBtns = false;
      }

      if (
        (this.variablePayoutObj.changeRequestStatus === 'APPROVED' || this.variablePayoutObj.changeRequestStatus === 'REJECTED') &&
        this.options.versionList[this.options.versionList.length - 1] === this.variablePayoutObj.version
      ) {
        this.allowToEditDocument = true;
      } else {
        this.allowToEditDocument = false;
      }
    }
  }

  getTabInfo(tabIndex) {
    this.activeTab = tabIndex;
    if (this.variablePayoutObj.id) {
      const payoutID = this.variablePayoutObj.id;
      if (tabIndex === 1) {
        this.getRulesDetails();
      }
      if (tabIndex === 2) {
        this.getExceptiionDetails(payoutID);
      }
    }
  }
  // Service Item Benefits Data
  getRulesDetails() {
    const reqObj: any = {
      variablePayoutId: this.variablePayoutObj.variablePayoutId,
      version: this.variablePayoutObj.version
    };
    if (this.variablePayoutObj.changeRequestStatus === 'APPROVED') {
      reqObj.isApproved = true;
    }
    this.variablePayoutService.getServiceItemBenifits(reqObj).subscribe((res: any) => {
      this.serviceItemBenefits = res && res.body ? res.body : [];
    });
    this.checkTemplateDivHg();
  }
  private checkTemplateDivHg(addNewTemp?) {
    if (this.isEmployeeSelected && this.activeTab === 1) {
      setTimeout(() => {
        // eslint-disable-next-line no-console
        const containerHg = document.getElementById('template-search-body').offsetHeight;
        if (containerHg > 44) {
          this.showMoreTemplateBtn = true;
          addNewTemp ? (this.showMoreTemplate = true) : '';
        } else {
          this.showMoreTemplateBtn = false;
        }
      }, 0);
    }
  }
  getExceptiionDetails(payoutID) {
    const reqObj = {
      query: `variablePayout.id:${payoutID}`
    };
    this.variablePayoutService.getServiceItemExceptionsByPayoutId(reqObj).subscribe((res: any) => {
      this.serviceItemExceptions = res && res.body ? res.body : [];
    });
  }

  checkFormDirty() {
    const mainObj = JSON.stringify(this.variablePayoutObj);
    const shadowObj = JSON.stringify(this.initVariablePayout);
    let status;
    if (
      mainObj !== shadowObj ||
      (this.basicDeatailsComponent && this.basicDeatailsComponent.form && this.basicDeatailsComponent.form.dirty) ||
      (this.variablPayoutRulesComponent &&
        this.variablPayoutRulesComponent.addRuleComponent &&
        this.variablPayoutRulesComponent.addRuleComponent.addRuleForm &&
        this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form &&
        this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form.dirty) ||
      (this.addExceptionsComponent &&
        this.addExceptionsComponent.addExceptionForm &&
        this.addExceptionsComponent.addExceptionForm.form &&
        this.addExceptionsComponent.addExceptionForm.form.dirty)
    ) {
      status = true;
    }
    return status;
  }
  resetFormData() {
    // take copy
    const shadowObj = JSON.stringify(this.variablePayoutObj);
    this.initVariablePayout = JSON.parse(shadowObj);

    if (this.basicDeatailsComponent && this.basicDeatailsComponent.form && this.basicDeatailsComponent.form.form) {
      this.basicDeatailsComponent.form.form.markAsPristine();
    }
    if (
      this.variablPayoutRulesComponent &&
      this.variablPayoutRulesComponent.addRuleComponent &&
      this.variablPayoutRulesComponent.addRuleComponent.addRuleForm &&
      this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form
    ) {
      this.variablPayoutRulesComponent.addRuleComponent.addRuleForm.form.markAsPristine();
    }
    if (this.addExceptionsComponent && this.addExceptionsComponent.addExceptionForm && this.addExceptionsComponent.addExceptionForm.form) {
      this.addExceptionsComponent.addExceptionForm.form.markAsPristine();
    }
  }

  cancel() {
    this.router.navigate(['artha/variable-payouts'], { queryParams: { currentTabIndex: 0 }, replaceUrl: true });
  }

  onEmployeeSearch(searchItem) {
    this.empList = [];
    this.getEmployeeList(searchItem);
  }

  getEmployeeDetails(id) {
    this.userService.find(id).subscribe(
      (res: any) => {
        this.options.employee = res;
        this.options.employee.employeeNo = res.employeeNumber;
      },
      (error: HttpResponse<any>) => {
        this.onError(error);
      }
    );
  }
  getEmployeeList(term) {
    if (this.previouseTerm !== term) {
      this.previouseTerm = term;
      this.departmentService
        .userOrganizationDepartmentMappingSearch({
          limit: 20,
          active: true,
          query: term.trim()
            ? (this.preferences.organization.id ? `organization.id: ${this.preferences.organization.id}` : 'active:true') +
              ' AND ' +
              `(userMaster.employeeNumber:(${term}) OR userMaster.displayName:(${term}))`
            : (this.preferences.organization.id ? `organization.id: ${this.preferences.organization.id}` : 'active:true') + ' *'
        })
        .subscribe(
          (res: any) => {
            this.empList = [];
            if (res.body && res.body.length > 0) {
              res.body.forEach(element => {
                if (element && element.userMaster) {
                  const userAlreadyExist = this.empList.find(user => user.id === element.userMaster.id);
                  if (!userAlreadyExist) {
                    this.empList.push(element.userMaster);
                  }
                }
              });
            }
          },
          (error: HttpResponse<any>) => {
            this.onError(error);
          }
        );
    }
  }

  selectedEmployee(event) {
    const data: any = event;
    this.selectedEmp = data;
    this.variablePayoutInfo(this.selectedEmp.id);
    if (this.selectedEmp) {
      this.isEmployeeSelected = true;
      this.variablePayoutObj.employee = data;
      this.variablePayoutObj.employee.employeeNo = data.employeeNumber;
      this.variablePayoutObj.employeeId = data.id;
      this.resetFormData();
    }
    this.options = new ArthaDetailHeaderOptions(
      true,
      true,
      this.selectedEmp,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      'NEW',
      0,
      null,
      true
    );
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  variablePayoutInfo(empId) {
    this.variablePayoutService
      .search({
        query: 'employeeId:' + empId,
        sort: this.sort(),
        size: 1
      })
      .subscribe((res: any) => {
        this.variablePayoutSavedInfo = res.body;
        if (this.variablePayoutSavedInfo && this.variablePayoutSavedInfo.length > 0) {
          this.resetFormData();
          this.router.navigate(['artha/variable-payouts/variable-payouts-update', this.variablePayoutSavedInfo[0].id], {
            replaceUrl: true
          });
        }
      });
  }

  sort() {
    const result = ['id' + ',' + 'desc'];
    return result;
  }

  templateSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.isTemplateSearching = term.length >= 1;
      }),
      switchMap((term: string) =>
        term.length < 1
          ? of([])
          : this.variablePayoutService
              .getResourceServiceItemBenefitTemplates({
                query: term.trim() ? this.searchTermModify.modify(term) : '*',
                size: 9999
              })
              .pipe(
                map((res: any) => {
                  return res.body;
                })
              )
      ),
      tap(() => (this.isTemplateSearching = false)),
      finalize(() => (this.isTemplateSearching = false))
    );

  templateInputFormatter = x => x.templateName;
  templateResultFormatter = x => x.templateName;

  onSelectTemplate(event) {
    if (this.templateLists.find(ele => ele.id === event.item.id)) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.templateExist');
      return;
    }
    if (event && event.item) {
      this.selectedTemplate = event.item;
      this.templateLists.push(this.selectedTemplate);
      this.checkTemplateDivHg(true);
      this.templateSearch = { item: { name: '' } };
    }
  }

  removeTemplate(index: number) {
    this.templateLists.splice(index, 1);
    this.checkTemplateDivHg();
  }

  copy() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CopyComponent, ngbModalOptions);
    this.modalRef.componentInstance.variablePayoutObj = this.variablePayoutObj;
    this.modalRef.componentInstance.versionInfo = this.latestVersionInfo;
    this.modalRef.componentInstance.isVariablePayout = true;
    this.modalRef.result.then(
      // result => {
      //     if (result) {

      //     }
      // },
      () => {}
    );
  }

  template(templateList) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      size: 'lg',
      windowClass: 'athma-modal-dialog vertical-middle-modal primary template-popup modal-width-1301'
    };
    this.modalRef = this.modalService.open(TemplateComponent, ngbModalOptions);
    this.modalRef.componentInstance.templateInformation = templateList;
    this.modalRef.result.then(
      // result => {
      //     if (result) {

      //     }
      // },
      () => {}
    );
  }

  openCommentBlock(readOnly) {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(CommentDialogComponent, ngbModalOptions);
    this.modalRef.componentInstance.headerTitle = 'Comments';
    this.modalRef.componentInstance.isDisplayMode = readOnly;
    let labelName;
    if (this.currentStatus === 'PENDING_APPROVAL') {
      labelName = 'Send For Approval';
    } else if (this.currentStatus === 'APPROVED') {
      labelName = 'Approve';
    } else if (this.currentStatus === 'REJECTED') {
      labelName = 'Reject';
    }
    this.modalRef.componentInstance.primaryBtnLabel = labelName;
     // eslint-disable-next-line no-console
     console.log("OPTIONS::", this.variablePayoutObj.transactionApprovalDetails);
    this.modalRef.componentInstance.transctionDetailList = this.variablePayoutObj.transactionApprovalDetails;


    this.modalRef.result.then(
      res => {
        const data = res;
        if (data === null || data === undefined || data.length === 0) {
          return;
        } else {
          this.approveAndRejectSendForApproval(data);
        }
      },
      () => {}
    );
  }

  approveAndRejectSendForApproval(result) {
    this.variablePayoutObj.changeRequestStatus = this.currentStatus;
    if (result) {
      result.action = this.currentStatus;
      const approvedByData: any = result.userComment.createdBy;
      const comment: any = {
        approvedBy: {
          displayName: approvedByData.displayName
            ? approvedByData.displayName
            : approvedByData.firstName && approvedByData.lastName
            ? approvedByData.firstName + ' ' + approvedByData.lastName
            : approvedByData.firstName,
          employeeNo: approvedByData.employeeNo ? approvedByData.employeeNo : approvedByData.login,
          id: approvedByData.id ? approvedByData.id : null,
          login: approvedByData.login ? approvedByData.login : null
        },
        approvedByDisplayName: approvedByData.displayName
          ? approvedByData.displayName
          : approvedByData.firstName && approvedByData.lastName
          ? approvedByData.firstName + ' ' + approvedByData.lastName
          : approvedByData.firstName,
        approvedByEmployeeNo: approvedByData.employeeNo ? approvedByData.employeeNo : approvedByData.login,
        approvedById: approvedByData.id ? approvedByData.id : '',
        approvedByLogin: approvedByData.login ? approvedByData.login : '',
        approvedDateTime: result.userComment.createdDateTime,
        comments: result.userComment.comment,
        documentType: 'VARIABLE_PAYOUT',
        type: result.action
      };
      this.variablePayoutObj.transactionApprovalDetails.unshift(comment);
      this.options.remarksListCount =
        this.variablePayoutObj.transactionApprovalDetails && this.variablePayoutObj.transactionApprovalDetails.length > 0
          ? this.variablePayoutObj.transactionApprovalDetails.length
          : 0;
      this.callApi();
    }
  }

  isNewFlow() {
    let newFlow = false;
    // new flow start when current status is approved / rejected and status should not be in draft or pending for approval
    if (
      this.allowToEditDocument ||
      this.initVariablePayout.changeRequestStatus === 'APPROVED' ||
      this.initVariablePayout.changeRequestStatus === 'REJECTED'
    ) {
      newFlow = true;
    }
    return newFlow;
  }

  callApi() {
    const userDTO = new UserDTO();
    if (this.preferences.user) {
      const firstName = this.preferences.user.firstName;
      const lastName = this.preferences.user.lastName;
      userDTO.displayName = firstName.concat(' ', lastName);
      userDTO.employeeNo = this.preferences.user.employeeNumber;
      userDTO.id = this.preferences.user.id;
    }
    if (this.variablePayoutObj.id) {
      const finalObj = JSON.parse(JSON.stringify(this.variablePayoutObj));
      // eslint-disable-next-line no-console
      console.log('FINALOBJECT ', finalObj);
      // eslint-disable-next-line no-console
      console.log("CURRENTSTATUS", this.currentStatus);
      if(this.currentStatus === 'DRAFT') {
        finalObj.createdDate = this.dateConverter.toDateTimeString(new Date());
        finalObj.createdBy = userDTO;
      }

      if (this.isNewFlow()) {
        delete finalObj.id;
        if (finalObj.departmentRevenueBenefits) {
          finalObj.departmentRevenueBenefits.forEach(dept => {
            delete dept.id;
          });
        }
        if (finalObj.lengthOfStayBenefits) {
          finalObj.lengthOfStayBenefits.forEach(stay => {
            delete stay.id;
            if (stay.stayBenefitServices && stay.stayBenefitServices.length) {
              stay.stayBenefitServices.forEach(stay1 => {
                delete stay1.id;
              });
            }
          });
        }
        if (finalObj.transactionApprovalDetails) {
          finalObj.transactionApprovalDetails.forEach(transaction => {
            delete transaction.id;
          });
        }
        if (finalObj.serviceItemBenefitTemplates) {
          finalObj.serviceItemBenefitTemplates.forEach(service => {
            if (service && service.serviceItemExceptions && service.serviceItemExceptions.length) {
              service.serviceItemExceptions.forEach(service1 => {
                delete service1.id;
              });
            }
          });
        }
        if (finalObj.serviceItemExceptions) {
          finalObj.serviceItemExceptions.forEach(exception => {
            delete exception.id;
          });
        }
      }
      this.httpBlockerService.enableHttpBlocker(true);
      this.variablePayoutService.update(finalObj).subscribe(
        res => {
          if (this.isNewFlow() && res && res.body) {
            this.resetFormData();
            this.httpBlockerService.enableHttpBlocker(false);
            this.router.navigate(['artha/variable-payouts/variable-payouts-update', res && res.body.id], {
              replaceUrl: true
            });
          } else {
            this.onSuccess(res);
            this.httpBlockerService.enableHttpBlocker(false);
          }
        },
        (err: HttpResponse<any>) => {
          this.onError(err);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    } else {
      this.httpBlockerService.enableHttpBlocker(true);
      this.variablePayoutObj.createdBy = userDTO;
      this.variablePayoutObj.createdDate = this.dateConverter.toDateTimeString(new Date());
      this.variablePayoutObj.unitCode = this.preferences && this.preferences.organization && this.preferences.organization.code;
      this.variablePayoutService.create(this.variablePayoutObj).subscribe(
        (res: any) => {
          this.onSuccess(res);
          this.httpBlockerService.enableHttpBlocker(false);
        },
        (err: HttpResponse<any>) => {
          this.onError(err);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    }
  }

  save(status) {
    this.currentStatus = status;
    if (this.variablePayoutObj.commencementDate === undefined || this.variablePayoutObj.commencementDate === null) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectCommencementDate');
    } else if (this.variablePayoutObj.contractEndDate === undefined || this.variablePayoutObj.contractEndDate === null) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectContractEndDate');
    } else if (
      this.variablePayoutObj.minAssuredAmount && this.variablePayoutObj.minAssuredAmount !== "0" &&
      (this.variablePayoutObj.minAssuredValidityDate === undefined || this.variablePayoutObj.minAssuredValidityDate === null)
    ) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.selectMinimumAssuredValidity');
    } else {
      this.variablePayoutObj.serviceItemBenefitTemplates = this.templateLists ? this.templateLists : undefined;
      if (status === 'DRAFT') {
        this.variablePayoutObj.changeRequestStatus = this.currentStatus;
        this.callApi();
      } else if (this.currentStatus === 'PENDING_APPROVAL') {
        // TODO: we need to enable below line while implementing comments
        const userDTO = new UserDTO();
        if (this.preferences.user) {
          const firstName = this.preferences.user.firstName;
          const lastName = this.preferences.user.lastName;
          userDTO.displayName = firstName.concat(' ', lastName);
          userDTO.employeeNo = this.preferences.user.employeeNumber;
          userDTO.id = this.preferences.user.id;
        }
        this.variablePayoutObj.createdBy = userDTO;
        this.openCommentBlock(false);
      } else if(this.currentStatus === 'APPROVED' || this.currentStatus === 'REJECTED') {
         this.openCommentBlock(false);
      }
    }
  }

  onSuccess(res) {
    this.variablePayoutObj = res.body;
    // update header data
    this.setHeaderDetails();
    this.loadVersionList();
    // Reset form dirty flags
    this.resetFormData();
  }

  sendForApproval() {}

  delete(variablePayoutInformation) {
    const variablePayoutInfo = variablePayoutInformation;
    // eslint-disable-next-line no-console
    console.log("variablePayoutInfo::", variablePayoutInfo);
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(VariablePayoutDeleteComponent, ngbModalOptions);
    this.modalRef.componentInstance.variablePayoutInformation = variablePayoutInformation;
    this.modalRef.result.then(
      result => {
        if (result) {
          this.variablePayoutService.delete(variablePayoutInformation.id).subscribe(() => {
            this.resetFormData();
            this.cancel();
            this.jhiAlertService.success('global.messages.response-msg', {
              msg: 'Variable Payout document of ' + '<strong>' + variablePayoutInformation.employee.displayName + '</strong>' + ' having EmployeeNo: ' + '<strong>' + variablePayoutInformation.employee.employeeNo + '</strong>' + ' deleted successfully.'
            });
          });
        }
      },
      () => {}
    );
  }
  /** Search Rule */
  search(searchTerm) {
    if (searchTerm !== undefined) {
      this.searchRuleText = searchTerm;
      if (this.searchRuleText === undefined) {
        this.searchRuleItem(this.searchRuleText);
      } else if (this.searchRuleText.trim().length === 0) {
        this.clearRuleSearch();
      } else if (this.searchRuleText.trim().length >= 3) {
        this.searchRuleItem(this.convertToSpecialString(this.searchRuleText));
      }
    }
  }

  searchRuleItem(term) {
    const reqObj: any = {
      query: term + 'AND ' + `variablePayout.id: ${this.variablePayoutObj.id}` //  AND version: ${this.variablePayoutObj.version}
    };
    this.variablePayoutService.searchServiceItemBenifits(reqObj).subscribe((res: any) => {
      this.serviceItemBenefits = res && res.body ? res.body : [];
    });
  }
  clearRuleSearch() {
    this.searchRuleText = '';
    this.getRulesDetails();
  }

  convertToSpecialString(strData) {
    let cleanStr = strData.trim();
    cleanStr = cleanStr.replace(/\+/g, ' ');
    cleanStr = cleanStr.replace(/  +/g, ' ');
    cleanStr = cleanStr.replace(/"/g, '');
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

  /**
   * onClickEditDocument method only execute when user click on Edit icon on header.
   */
  onClickEditDocument() {
    this.hasAuthority()
      .then(data => {
        if (data) {
          this.isEditMode = true;
          this.initVariablePayout.changeRequestStatus = 'DRAFT';
          this.variablePayoutObj.changeRequestStatus = 'DRAFT';
          this.options.status = this.variablePayoutObj.changeRequestStatus;
        } else {
          this.customError('Unauthorized to access this resource');
        }
      })
      .catch(() => {
        this.customError('Unauthorized to access this resource');
      });
  }
  public onClickShowMore() {
    this.showMoreTemplate = !this.showMoreTemplate;
  }
  private hasAuthority(): Promise<boolean> {
    const auth: Promise<boolean> = this.accountService
      .checkHasAuthority(this.modifyPrivileges)
      .then(data => {
        if (data) {
          return data;
        }
      })
      .catch(() => {
        return false;
      });
    return auth;
  }

  private customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }

  checkUpdates() {
    return !this.checkFormDirty();
  }

  canDeactivate() {
    return Observable.of(this.checkUpdates());
  }

  exportRules() {
    alert("Export Rules");
  }

  exportExceptions() {
    alert("Export Exceptions");
  }
}
