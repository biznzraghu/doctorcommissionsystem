import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ArthaDetailHeaderOptions, DepartmentDTO } from 'app/artha-helpers/artha-headers/artha-detail-header/artha-detail-header.model';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { DepartmentService } from '../../administration/administration-services/department.service';
import { UserService } from '../../administration/administration-services/user.service';
import { AddConsultantPopupComponent } from './../add-consultant/add-consultant-popup.component';
import { AddVariablePayoutComponent } from './../add-variable-range/add-variable-payout.component';
import { DepartmentPayout } from './../department-payout.model';
import { DepartmentPayoutService } from '../department-payout.service';

@Component({
  selector: 'jhi-department-payout-detail',
  templateUrl: './department-payout-detail.component.html',
  styles: [
    `
      .disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    `
  ]
})
export class DepartmentPayoutDetailComponent implements OnInit {
  visitType: any;
  selectedVisit: any = [];
  variableRange = '';
  public searchText: string;
  modalRef: NgbModalRef;
  selectedConsultant: any;
  multiSelectDropdownHscSettings: any;
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

  @Input() departmentPayout: DepartmentPayout;
  @Input() preferences;
  isEditMode = false;

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  itemsPerPage: any;
  page: any;
  queryCount: any;
  consulantList = [];
  selectedOnCost: any;
  selectedNetGross: any;
  selectedApplicable: any;

  public isDataSearching: boolean;
  public showError: boolean;
  deptList = [];
  selectedDepartment: any;
  deptSelectedConsumption: any = [];
  bFormModificationCheck = false;
  isEnableAdd = false;
  options: ArthaDetailHeaderOptions;
  departmentDTO: DepartmentDTO;
  links: any;
  visitDisplay = [];

  @Input() selectedDept;
  @Input() formDisabled;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private userService: UserService,
    private jhiAlertService: JhiAlertService,
    private departmentService: DepartmentService,
    private departmentPayoutService: DepartmentPayoutService,
    private parseLinks: JhiParseLinks
  ) {
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0
    };
  }

  ngOnInit() {
    this.multiSelectDropdownHscSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 1,
      allowSearchFilter: false
    };
    this.displayVisitParams();

    if (
      this.departmentPayout.id ||
      (this.departmentPayout.payoutRanges.length > 0 &&
        this.departmentPayout.visitType !== undefined &&
        this.departmentPayout.visitType !== null)
    ) {
      if (this.departmentPayout.netGross !== null && this.departmentPayout.netGross !== undefined) {
        const costLen = this.netGrossList.length;
        for (let i = 0; i < costLen; i++) {
          if (this.departmentPayout.netGross === this.netGrossList[i].code) {
            this.selectedNetGross = this.netGrossList[i];
            break;
          }
        }
      }
      if (this.departmentPayout.netGross !== null && this.departmentPayout.netGross !== undefined) {
        const saleLen = this.onCostSaleList.length;
        for (let i = 0; i < saleLen; i++) {
          if (this.departmentPayout.onCostSale === this.onCostSaleList[i].code) {
            this.selectedOnCost = this.onCostSaleList[i];
            break;
          }
        }
      }
      if (this.departmentPayout.applicableInvoice !== null && this.departmentPayout.applicableInvoice !== undefined) {
        const invoiceLen = this.applicableInvoicesList.length;
        for (let i = 0; i < invoiceLen; i++) {
          if (this.departmentPayout.applicableInvoice === this.applicableInvoicesList[i].code) {
            this.selectedApplicable = this.applicableInvoicesList[i];
            break;
          }
        }
      }
      if (this.departmentPayout.deptConsumption) {
        this.getDepartmentList(this.departmentPayout.departmentId);
      }
      if (this.departmentPayout.applicableConsultants.length > 0) {
        this.queryCount = this.departmentPayout.applicableConsultants.length + ' consultants added';
      }
      if (this.departmentPayout.payoutRanges.length > 0) {
        this.variableRange = this.departmentPayout.payoutRanges
          .map(p => p.fromAmount + '-' + p.toAmount + '(' + p.percentage + '%)')
          .join(',');
      }
    }

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
  private onSuccess(data, headers) {
    if (data !== null && data !== undefined) {
      this.links = this.parseLinks.parse(headers.get('link'));
      data.forEach(element => {
        this.consulantList.push(element.userMaster), headers;
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

  getDepartmentList(id) {
    this.departmentService
      .search({
        query: 'active:true AND id:' + id
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

  onClickReductions(event) {
    const data = event;
    if (data) {
      if (this.selectedDept && this.selectedDept.id) {
        this.getDepartmentList(this.selectedDept.id);
      }
    } else {
      this.departmentPayout.departmentConsumptionMaterialReductions = null;
    }
  }

  close() {
    this.router.navigate(['artha/department-payout'], { replaceUrl: true });
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

  displayDeptParams() {
    const dpt = this.departmentPayout.departmentConsumptionMaterialReductions;
    if (!dpt || dpt.length === 0) {
      return '';
    }
    return dpt.map(p => p.departmentName).join('   ');
  }
  displayHSCParam() {
    const hsc = this.departmentPayout.hscConsumptionMaterialReductions;
    if (!hsc || hsc.length === 0) {
      return '';
    }
    return hsc.map(p => p.hscName).join('   ');
  }
}
