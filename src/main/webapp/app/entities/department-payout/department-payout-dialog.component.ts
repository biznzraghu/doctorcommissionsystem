import { Component, Input, OnInit } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchTermModify } from 'app/artha-helpers';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { Observable } from 'rxjs';
import { DepartmentService } from '../administration/administration-services/department.service';
import { AddConsultantPopupComponent } from './add-consultant/add-consultant-popup.component';
import { AddVariablePayoutComponent } from './add-variable-range/add-variable-payout.component';
import { DepartmentConsumptionMaterialReduction, DepartmentPayout, HscConsumptionMaterialReduction } from './department-payout.model';
import { DepartmentPayoutService } from './department-payout.service';

@Component({
  selector: 'jhi-department-payout-dialog',
  templateUrl: './department-payout-dialog.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class DepartmentPayoutDialogComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  visitType: any;
  selectedVisit: any = [];
  variableRange = '';
  public searchText: string;
  modalRef: NgbModalRef;
  selectedConsultant: any;
  multiSelectDropdownSettings: any;
  multiSelectDropdownDeptSettings: any;
  rangeList: any;
  visitTypeList = [];
  hscList = [];
  netGrossList = [
    { code: 'NET', name: 'Net' },
    { code: 'GROSS', name: 'Gross' }
  ];
  applicableInvoicesList = [
    { code: 'ALL_INVOICES', name: 'All Invoices' },
    { code: 'INVOICES_WITH_SURGERY', name: 'Invoices with Surgery' },
    { code: 'INVOICES_WITH_ANESTHESIA', name: 'Invoice with Anesthesia' }
  ];
  onCostSaleList = [
    { code: 'COST', name: 'Cost' },
    { code: 'SALE', name: 'Sale' }
  ];

  departmentPayout: DepartmentPayout;
  isEditMode = true;

  @Input() selectedDept;
  @Input() preferences;
  @Input() formDisabled;
  @Input() set depPayout(p) {
    this.departmentPayout = p;
  }
  get depPayout() {
    return this.departmentPayout;
  }

  itemsPerPage: any;
  page: any;
  queryCount: any;
  consulantList = [];

  public isDataSearching: boolean;
  public showError: boolean;
  deptList = [];
  selectedDepartment: any;
  deptSelectedConsumption: any = [];
  bFormModificationCheck = false;
  isEnableAdd = false;
  links: any;
  visitDisplay = [];

  hscSearching = false;
  depSearching = false;
  hscSelected: any;
  depSelectList = [];
  hscSelectedList = [];
  departmentConsumptionMaterialReduction: Array<DepartmentConsumptionMaterialReduction> = [];
  hscConsumptionMaterialReduction: Array<HscConsumptionMaterialReduction> = [];

  constructor(
    private modalService: NgbModal,
    private jhiAlertService: JhiAlertService,
    private departmentPayoutService: DepartmentPayoutService,
    private departmentService: DepartmentService,
    private eventManager: JhiEventManager,
    private parseLinks: JhiParseLinks,
    private searchTermModify: SearchTermModify
  ) {
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
  }

  ngOnInit() {
    this.multiSelectDropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'display',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: false
    };

    this.displayVisitParams();

    this.multiSelectDropdownDeptSettings = {
      singleSelection: false,
      idField: 'departmentId',
      textField: 'departmentName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: false
    };

    this.eventManager.subscribe('department', res => {
      const data = res.content;
      if (data === 'OK') {
        this.departmentPayout = new DepartmentPayout();
        this.departmentPayout.netGross = this.netGrossList[0].code;
        this.departmentPayout.applicableInvoice = this.applicableInvoicesList[0].code;
        this.departmentPayout.onCostSale = this.onCostSaleList[0].code;
        this.variableRange = null;
        this.queryCount = null;
        this.selectedVisit = null;
      }
    });

    this.eventManager.subscribe('existDepartment', res => {
      const data = res.content;
      if (data !== null && data !== undefined) {
        this.departmentPayout = data;
        this.getVisitList();
        if (this.departmentPayout.payoutRanges.length > 0) {
          this.variableRange = this.departmentPayout.payoutRanges
            .map(p => p.fromAmount + '-' + p.toAmount + '(' + p.percentage + '%)')
            .join(',');
        }
        if (this.departmentPayout.applicableConsultants.length > 0) {
          this.queryCount = this.departmentPayout.applicableConsultants.length + ' consultants added';
        }
        if (this.departmentPayout.deptConsumption) {
          this.getDepartmentList();
        }
      } else {
        this.departmentPayout = new DepartmentPayout();
        this.variableRange = null;
        this.queryCount = null;
        this.selectedVisit = null;
      }
    });

    if (
      this.departmentPayout.id ||
      (this.departmentPayout.payoutRanges.length > 0 &&
        this.departmentPayout.visitType !== undefined &&
        this.departmentPayout.visitType !== null)
    ) {
      if (this.departmentPayout.deptConsumption) {
        if (this.departmentPayout.departmentConsumptionMaterialReductions.length > 0) {
          this.departmentPayout.departmentConsumptionMaterialReductions.map(ele => {
            this.depSelectList.push({ id: ele.departmentId, name: ele.departmentName });
          });
        }
        this.getDepartmentList();
      }
      if (this.departmentPayout.hscConsumption) {
        if (this.departmentPayout.hscConsumptionMaterialReductions.length > 0) {
          this.departmentPayout.hscConsumptionMaterialReductions.map(ele => {
            this.hscSelectedList.push({ id: ele.hscId, name: ele.hscName });
          });
        }
        this.getHSCList();
      }
      if (this.departmentPayout.applicableConsultants.length > 0) {
        this.queryCount = this.departmentPayout.applicableConsultants.length + ' consultants added';
      }
      if (this.departmentPayout.payoutRanges.length > 0) {
        this.variableRange = this.departmentPayout.payoutRanges
          .map(p => p.fromAmount + '-' + p.toAmount + '(' + p.percentage + '%)')
          .join(',');
      }
    } else {
      this.departmentPayout.netGross = this.netGrossList[0].code;
      this.departmentPayout.applicableInvoice = this.applicableInvoicesList[0].code;
      this.departmentPayout.onCostSale = this.onCostSaleList[0].code;
    }

    this.getVisitList();
    this.getConsulantList();
  }

  openPopup(isRangeAvailable) {
    if (this.departmentPayout.id) {
      this.rangeList = this.departmentPayout.payoutRanges;
    }
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    };
    this.modalRef = this.modalService.open(AddVariablePayoutComponent, ngbModalOptions);
    this.modalRef.componentInstance.payoutRangeList = this.rangeList;
    this.modalRef.componentInstance.isRangeAvailable = isRangeAvailable;
    this.modalRef.componentInstance.isEditMode = this.isEditMode;
    this.modalRef.result.then(
      result => {
        if (result != null && result !== undefined) {
          this.departmentPayout.payoutRanges = result;
          this.rangeList = result;
          this.variableRange = result.map(p => p.fromAmount + '-' + p.toAmount + '(' + p.percentage + '%)').join(',');
          this.checkEnabled();
        }
      },
      () => {}
    );
  }

  openConsultantPopup() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: true,
      windowClass: 'athma-modal-dialog vertical-middle-modal sm primary department-payout'
    };
    this.modalRef = this.modalService.open(AddConsultantPopupComponent, ngbModalOptions);
    this.modalRef.componentInstance.consultantList = this.consulantList;
    this.modalRef.componentInstance.applicableConsultant = this.departmentPayout.applicableConsultants;
    this.modalRef.componentInstance.isEditMode = this.isEditMode;
    this.modalRef.componentInstance.preferences = this.preferences;
    this.modalRef.componentInstance.links = this.links;

    this.modalRef.result.then(
      result => {
        if (result != null && result !== undefined && result.length > 0) {
          this.departmentPayout.applicableConsultants = result;
          this.queryCount = this.departmentPayout.applicableConsultants.length + ' consultants added';
          this.checkEnabled();
        } else {
          this.departmentPayout.applicableConsultants = result;
          this.queryCount = '';
          this.checkEnabled();
        }
      },
      () => {}
    );
  }

  getConsulantList() {
    this.departmentPayoutService
      .getUserOrganizationMapping({
        query: 'organization.code:' + this.preferences.organization.code + ' AND userMaster.designation: Consultant',
        size: 999
      })
      .subscribe(
        (res: any) => {
          this.onSuccess(res.body, res.headers);
        },
        (error: any) => {
          this.onError(error.error);
        }
      );
  }

  getVisitList() {
    this.departmentPayoutService.getVisit({ query: '*' }).subscribe(
      (res: any) => {
        const data = res.body;
        const arr = [];
        Object.keys(data).map(function(key) {
          arr.push({ id: data[key], display: data[key] });
        });
        this.visitTypeList = arr;
        if (this.departmentPayout.visitType !== null && this.departmentPayout.visitType !== undefined) {
          const vType = this.departmentPayout.visitType.split(',');
          this.selectedVisit = [];
          vType.map(objData => {
            const visitTypeLen = this.visitTypeList.length;
            for (let i = 0; i < visitTypeLen; i++) {
              if (objData === this.visitTypeList[i].id) {
                this.selectedVisit.push(this.visitTypeList[i]);
                break;
              }
            }
          });
        }
      },
      (error: any) => {
        this.onError(error.error);
      }
    );
  }

  private onSuccess(data, headers) {
    if (data !== null && data !== undefined) {
      this.links = this.parseLinks.parse(headers.get('link'));
      data.forEach(element => {
        this.consulantList.push(element.userMaster);
      });
    }
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  onSelectVisitType() {
    const vType = this.selectedVisit.map(p => p.id).join(',');
    this.departmentPayout.visitType = vType;
    this.checkEnabled();
  }
  onSelectAllVisitType(allSelectedVisit) {
    this.selectedVisit = allSelectedVisit;
    const vType = this.selectedVisit.map(p => p.id).join(',');
    this.departmentPayout.visitType = vType;
    this.checkEnabled();
  }

  onItemDeSelect() {
    this.onSelectVisitType();
  }

  onAllItemDeSelect(allDeselectedItem) {
    this.onSelectAllVisitType(allDeselectedItem);
  }

  getDepartmentList() {
    this.departmentService
      .search({
        query: 'active:true AND organization.id:' + this.preferences.organization.id
      })
      .subscribe(
        (res: any) => {
          const depLen = res.body;
          for (let i = 0; i < depLen.length; i++) {
            this.deptList.push({ departmentId: depLen[i].id, departmentName: depLen[i].name, id: undefined });
          }
        },
        (error: any) => {
          this.onError(error.error);
        }
      );
  }
  getHSCList() {
    this.departmentService
      .search({
        query: 'active:true AND organizationCode:' + this.preferences.organization.code
      })
      .subscribe(
        (res: any) => {
          const depLen = res.body;
          for (let i = 0; i < depLen.length; i++) {
            this.deptList.push({ hscId: depLen[i].id, name: depLen[i].name, id: undefined });
          }
        },
        (error: any) => {
          this.onError(error.error);
        }
      );
  }

  formModified() {
    this.bFormModificationCheck = true;
    this.checkEnabled();
  }

  checkEnabled() {
    if (
      (!this.departmentPayout.visitType || !this.departmentPayout.netGross || !this.departmentPayout.onCostSale) &&
      this.departmentPayout.payoutRanges.length > 0
    ) {
      this.isEnableAdd = true;
    } else {
      this.isEnableAdd = false;
    }
  }

  onClickReductions(event) {
    const data = event;
    if (data) {
      if (this.selectedDept && this.selectedDept.id) {
        this.getDepartmentList();
      }
    } else {
      this.departmentPayout.departmentConsumptionMaterialReductions = null;
    }
  }

  displayVisitParams() {
    if (this.departmentPayout.id || (this.departmentPayout.visitType !== undefined && this.departmentPayout.visitType !== null)) {
      this.visitDisplay = this.departmentPayout.visitType.split(',');
      const vType = this.departmentPayout.visitType.split(',');
      if (!vType || vType.length === 0) {
        return '-';
      }
    }
    // return vType.map(p => p).join('   ');
  }

  searchHscTypehead = (text$: Observable<string>) =>
    text$
      .distinctUntilChanged()
      .do(term => {
        this.hscSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of(this.getHSCList())
          : this.departmentPayoutService
              .getHSC({
                query: 'active:true AND organizationCode:' + this.preferences.organization.code + ' ' + this.searchTermModify.modify(term)
              })
              .map((res: any) => {
                return res.body;
              })
      )
      .do(() => {
        this.hscSearching = false;
      });

  displayFormatUserTypeheadData = (x: any) => x.code + ' | ' + x.name;

  inputFormatUserTypeheadData = (x: any) => {
    return x ? x.name : x.name;
  };

  onSelectHSCfromTyeahead(event) {
    const data = event?.item;

    if (this.hscSelectedList.find(ele => ele.id === event.item.id)) {
      this.jhiAlertService.error('arthaApp.department-payout.validation.hscExist');
      this.hscSelected = {};
      return;
    }
    if (event && event.item) {
      this.hscSelectedList.push(data);
      for (let i = 0; i < this.hscSelectedList.length; i++) {
        this.hscConsumptionMaterialReduction.push({
          hscId: this.hscSelectedList[i].id,
          hscName: this.hscSelectedList[i].name,
          id: undefined
        });
      }
      this.departmentPayout.hscConsumptionMaterialReductions = this.hscConsumptionMaterialReduction;
      this.hscSelected = {};
    }
  }

  removeHsc(index: number) {
    this.hscSelectedList.splice(index, 1);
    this.hscConsumptionMaterialReduction = [];
    for (let i = 0; i < this.hscSelectedList.length; i++) {
      this.hscConsumptionMaterialReduction.push({
        hscId: this.hscSelectedList[i].id,
        hscName: this.hscSelectedList[i].name,
        id: undefined
      });
    }
    this.departmentPayout.hscConsumptionMaterialReductions = this.hscConsumptionMaterialReduction;
  }

  onSelectDeptfromTyeahead(event) {
    const data = event?.item;

    if (this.depSelectList.find(ele => ele.id === event.item.id)) {
      this.jhiAlertService.error('arthaApp.department-payout.validation.deptExist');
      this.selectedDepartment = {};
      return;
    }
    if (event && event.item) {
      this.depSelectList.push(data);
      for (let i = 0; i < this.depSelectList.length; i++) {
        this.departmentConsumptionMaterialReduction.push({
          departmentId: this.depSelectList[i].id,
          departmentName: this.depSelectList[i].name,
          id: undefined
        });
      }
      this.departmentPayout.departmentConsumptionMaterialReductions = this.departmentConsumptionMaterialReduction;
      this.selectedDepartment = {};
    }
  }
  removeDept(index: number) {
    this.depSelectList.splice(index, 1);
    this.departmentConsumptionMaterialReduction = [];
    for (let i = 0; i < this.depSelectList.length; i++) {
      this.departmentConsumptionMaterialReduction.push({
        departmentId: this.depSelectList[i].id,
        departmentName: this.depSelectList[i].name,
        id: undefined
      });
    }
    this.departmentPayout.departmentConsumptionMaterialReductions = this.departmentConsumptionMaterialReduction;
  }

  searchDepTypehead = (text$: Observable<string>) =>
    text$
      .distinctUntilChanged()
      .do(term => {
        this.depSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of(this.getDepartmentList())
          : this.departmentService
              .search({
                query: 'active:true AND organization.id:' + this.preferences.organization.id + ' ' + this.searchTermModify.modify(term)
              })
              .map((res: any) => {
                return res.body;
              })
      )
      .do(() => {
        this.depSearching = false;
      });

  displayFormatDepTypeheadData = (x: any) => x.code + ' | ' + x.name;

  inputFormatDepTypeheadData = (x: any) => {
    return x ? x.name : x.name;
  };
}
