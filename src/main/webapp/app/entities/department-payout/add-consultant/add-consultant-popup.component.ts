import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ExportHelper } from 'app/shared/util/export.helper';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { debounceTime, map } from 'rxjs/operators';
import { Observable } from 'rxjs/Rx';
import { ApplicableConsultant } from '../department-payout.model';
import { DepartmentPayoutService } from '../department-payout.service';
@Component({
  selector: 'jhi-add-consultant-popup',
  templateUrl: './add-consultant-popup.component.html'
})
export class AddConsultantPopupComponent implements OnInit {
  @Input() isEditMode = false;
  @Input() consultantList: Array<any>;
  @Input() applicableConsultant: Array<ApplicableConsultant> = [];
  @Input() preferences;
  @Input() links;

  searchingConsultant = false;
  applicableConst: ApplicableConsultant;
  consultantAddedList: any;
  itemsPerPage: any;
  page: any;
  isChecked = false;
  qurText: string;
  userIdText = '';
  activeData = false;
  initalConsultantList = [];
  initalApplicableConsultant = [];
  tempConsultantList: Array<ApplicableConsultant> = [];

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  constructor(
    public activeModal: NgbActiveModal,
    private departmentPayoutService: DepartmentPayoutService,
    private jhiAlertService: JhiAlertService,
    private parseLinks: JhiParseLinks,
    private exportHelper: ExportHelper
  ) {
    this.applicableConst = new ApplicableConsultant();
    this.itemsPerPage = 15;
    this.page = 0;
  }

  ngOnInit() {
    this.consultantAddedList = this.consultantList.slice(0);
    this.tempConsultantList = this.applicableConsultant.slice(0);
    if (this.applicableConsultant.length > 0) {
      this.applicableConsultant.filter(objData => {
        for (let i = 0; i < this.consultantList.length; i++) {
          if (this.consultantList[i].id === objData.userMasterId) {
            this.applicableConst = new ApplicableConsultant();
            this.userIdText += objData.userMasterId + ' OR ';
            this.consultantList[i]['include'] = true;
            break;
          }
        }
      });
      if (this.userIdText && this.userIdText.length > 0) {
        const ind = this.userIdText.lastIndexOf('OR');
        this.userIdText = this.userIdText.substring(0, ind);
      }
      if (!this.isEditMode) {
        this.activeData = true;
        this.populateFilteredConsultant(true);
        // this.consultantList = this.applicableConsultant;
      }
    }
  }

  process() {
    this.applicableConsultant = this.tempConsultantList.slice(0);

    this.initalConsultantList = this.consultantList;
    this.tempConsultantList = [];
    this.consultantList = [];

    this.activeModal.close(this.applicableConsultant);
  }

  close() {
    this.tempConsultantList = [];
    this.consultantList = this.consultantAddedList;
    this.activeModal.dismiss();
  }

  setConsultant(event) {
    const data = event.item;
    this.addRecord(data);
  }

  public populateFilteredConsultant(event) {
    this.isChecked = event;
    const consult = [];
    if (this.isChecked) {
      const data = this.consultantList.filter(item => {
        if (item.include) {
          return true;
        }
        return false;
      });

      this.applicableConsultant.filter(objData => {
        for (let i = 0; i < data.length; i++) {
          if (data[i].id === objData.userMasterId) {
            consult.push(data[i]);
          }
        }
      });
      this.consultantList = consult;
      if (this.directiveScroll) {
        this.directiveScroll.scrollToTop();
      }
    } else {
      this.consultantList = this.consultantAddedList;
    }
  }

  searchUserTypehead = (text$: Observable<string>) =>
    text$
      .pipe(
        debounceTime(500),
        map(term =>
          term === ''
            ? []
            : this.consultantList.filter(
                v => v.displayName.toLowerCase().indexOf(term.toLowerCase()) > -1 || v.employeeNumber.indexOf(term) > -1
              )
        )
      )
      .do(() => {
        this.searchingConsultant = false;
      });

  consultantInputFormat = () => null;
  // consultantOutputFormat = (z: any) => z.displayName;

  addRecord(data) {
    this.applicableConst = new ApplicableConsultant();
    const conIndex = this.tempConsultantList.findIndex(i => i.userMasterId === data.id);
    if (conIndex < 0) {
      this.applicableConst.displayName = data.displayName;
      this.applicableConst.employeeCode = data.employeeNumber;
      this.applicableConst.userMasterId = data.id;
      this.tempConsultantList.push(this.applicableConst);
    } else {
      this.tempConsultantList.splice(conIndex, 1);
    }
  }

  getClass(cons) {
    const conIndex = this.tempConsultantList.findIndex(i => i.userMasterId === cons.id);
    if (conIndex < 0) {
      return 'exclude';
    } else {
      return 'include';
    }
  }
  getStyle(cons) {
    const conIndex = this.tempConsultantList.findIndex(i => i.userMasterId === cons.id);
    if (conIndex < 0) {
      return 'exclude-cons';
    } else {
      return 'include-cons';
    }
  }

  removeDepartment(id) {
    for (let i = 0; i < this.applicableConsultant.length; i++) {
      if (this.applicableConsultant[i].userMasterId === id) {
        this.applicableConsultant.splice(i, 1);
      }
      if (this.tempConsultantList[i].userMasterId === id) {
        this.tempConsultantList.splice(i, 1);
      }
    }
  }

  loadPage(pageNo) {
    this.page = pageNo;
    this.getConsulantList();
  }

  getConsulantList() {
    this.departmentPayoutService
      .getUserOrganizationMapping({
        query: 'organization.code:' + this.preferences.organization.code + ' AND userMaster.designation: Consultant',
        page: this.page,
        size: this.itemsPerPage
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
      const userMapped = [];
      data.forEach(element => {
        userMapped.push(element.userMaster);
      });
      this.page === 0 ? (this.consultantList = userMapped) : (this.consultantList = [...this.consultantList, ...userMapped]);
      this.consultantAddedList = this.consultantList;
    }
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  export(): any {
    this.page = 0;
    if (this.isChecked) {
      this.qurText = 'userMaster.id :(' + this.userIdText + ')';
    }
    this.departmentPayoutService
      .exportConsultant({
        query: this.qurText
          ? this.qurText
          : 'organization.code:' + this.preferences.organization.code + ' AND userMaster.designation: Consultant'
        // size: this.itemsPerPage
        // sort: this.sort()
      })
      .subscribe(
        (res: any) => this.exportHelper.openFile(res.body),
        (res: HttpErrorResponse) => this.onError(res.error)
      );
  }
}
