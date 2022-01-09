import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { PayoutAdjustmentDetail } from 'app/entities/artha-models/payout-adjustment.model';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, switchMap, map, finalize } from 'rxjs/operators';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { UserInterface } from 'app/entities/artha-models/user.model';
import { PayoutHelperService } from '../../payout-helper.service';
import { JhiAlertService } from 'ng-jhipster';
import { DepartmentMasterDTO } from 'app/entities/artha-models/department.model';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import { SearchTermModify } from 'app/artha-helpers';
import { DepartmentService } from 'app/entities/administration/administration-services/department.service';
import { HttpResponse } from '@angular/common/http';
@Component({
  selector: 'jhi-add-update-entity-control',
  templateUrl: './add-update-entity-control.component.html'
})
export class AddUpdateEntityControlComponent implements OnInit {
  @Input() isEditMode = false;
  @Input() dataInstance: PayoutAdjustmentDetail;
  @Input() isPayoutForEmployee: boolean;
  @Output() onCancel = new EventEmitter();
  @Output() onSavaChanges = new EventEmitter();
  @Output() onAddRecord = new EventEmitter();
  contraEmployeeDetail: UserInterface;
  contraDepartmentDetail: DepartmentMasterDTO;
  monthList = [];
  public contraSearch: boolean;
  @ViewChild('Empinput', { static: false }) contraEmployeeRef: ElementRef;
  @ViewChild('deptinput', { static: false }) contraDepartmentRef: ElementRef;

  preference: any;
  unitId: string;

  constructor(
    private userService: UserService,
    private payoutHelperService: PayoutHelperService,
    private jhiAlertService: JhiAlertService,
    public searchTermModify: SearchTermModify,
    public departmentService: DepartmentService,
    private prefrencesService: PreferenceService
  ) {
    this.monthList = this.payoutHelperService.getYearAndMonth();
    this.preference = this.prefrencesService.currentUser();
    if (this.preference && this.preference.organization && this.preference.organization.id) {
      this.unitId = this.preference.organization.id;
    }
  }
  ngOnInit() {
    if (this.dataInstance && this.dataInstance.month === '') {
      this.dataInstance.month = this.monthList[0].value;
    }
    if (this.dataInstance && this.dataInstance.year === '') {
      this.dataInstance.year = this.monthList[0].year;
    }
  }
  onClickSaveChanges() {
    this.onSavaChanges.emit();
  }
  onClickCancel() {
    this.onCancel.emit();
  }
  onClickAddRecord() {
    if (this.dataInstance.amount > 9999999.99) {
      this.jhiAlertService.error('global.messages.response-msg', { msg: `Amount should not be more than-9999999.99` });
      return;
    }
    this.payoutHelperService
      .payoutAdjustmentValidation(this.dataInstance, this.isPayoutForEmployee)
      .then(res => {
        if (res) {
          this.onAddRecord.emit();
          this.resetInstance();
        }
      })
      .catch(err => {
        if (err && err.msg === 'Please select Employee') {
          if (this.isPayoutForEmployee) {
            this.focusToSelectEmployee();
          } else {
            // err.msg === 'Please select Department'
            this.focusToSelectDepartment();
          }
        }
      });
    // if ((this.dataInstance.description.trim().length > 0) && (this.dataInstance.amount > 0)) {
    //   this.onAddRecord.emit();
    //   this.resetInstance();
    // } else {
    //   if(this.dataInstance.description.trim().length === 0) {
    //       this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Description!` });
    //   } else {
    //     if(this.dataInstance.amount <= 0) {
    //       this.jhiAlertService.error('global.messages.response-msg', { msg: `Please Enter Positive Amount !` });
    //     }
    //   }

    // }
  }
  focusToSelectEmployee() {
    setTimeout(() => {
      this.contraEmployeeRef.nativeElement.focus();
    }, 100);
  }
  focusToSelectDepartment() {
    setTimeout(() => {
      this.contraDepartmentRef.nativeElement.focus();
    }, 100);
  }
  resetInstance() {
    this.dataInstance = new PayoutAdjustmentDetail();
    if (this.dataInstance && this.dataInstance.month === '') {
      this.dataInstance.month = this.monthList[0].value;
    }
    if (this.dataInstance && this.dataInstance.year === '') {
      this.dataInstance.year = this.monthList[0].year;
    }
    this.dataInstance.contra = 'No';
    this.contraEmployeeDetail = undefined;
    this.contraDepartmentDetail = undefined;
  }
  employeeSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.contraSearch = term.length >= 1;
      }),
      switchMap((term: string) =>
        term.length < 1
          ? of([])
          : this.departmentService
              .userOrganizationDepartmentMappingSearch({
               // query: `organization.id:${this.unitId} AND userMaster.displayName:` + this.searchTermModify.modify(term) + '*'
               query: term.trim()
               ? (this.unitId ? `organization.id: ${this.unitId}` : 'active:true') +
                 ' AND ' +
                 `(userMaster.employeeNumber:( *${term}* ) OR userMaster.displayName:( *${term}* ))`
               : (this.unitId ? `organization.id: ${this.unitId}` : 'active:true') + ' *'
              })
              .pipe(
                map((res: HttpResponse<any>) => {
                  const users: any = [];
                  if (res.body && res.body.length > 0) {
                    res.body.forEach(element => {
                      if (element && element.userMaster) {
                        const userAlreadyExist = users.find(user => user.id === element.userMaster.id);
                        if (!userAlreadyExist) {
                          users.push(element.userMaster);
                        }
                      }
                    });
                  }
                  // const userMaster: any = res.body && res.body.length > 0 && res.body[0].userMaster ? res.body[0].userMaster : [];
                  return users;
                })
              )
      ),
      tap(() => (this.contraSearch = false)),
      finalize(() => (this.contraSearch = false))
    );

  employeeSearchData = x => x.displayName;

  departmentSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.contraSearch = term.length >= 1;
      }),
      switchMap((term: string) =>
        term.length < 1
          ? of([])
          : this.departmentService
              .search({
                limit: 20,
                query: term.trim()
                  ? (this.unitId ? `active:true AND organization.id: ${this.unitId}` : '') + ' ' + this.searchTermModify.modify(term)
                  : (this.unitId ? `active:true AND organization.id: ${this.unitId}` : '') + ' *'
              })
              .pipe(
                map((res: any) => {
                  return res.body;
                })
              )
      ),
      tap(() => (this.contraSearch = false)),
      finalize(() => (this.contraSearch = false))
    );
  departmentSearchData = x => x.name;

  onSelectEmployee(event) {
    if (event && event.item) {
      const selectedEmployee = event.item;
      this.dataInstance.contraEmployeeDetail = selectedEmployee;
      this.dataInstance.contraEmployeeId = selectedEmployee.employeeNumber;
      // eslint-disable-next-line no-console
      console.log('selectedEmployee ', selectedEmployee);
    }
  }
  onSelectDepartment(event) {
    if (event && event.item) {
      const selectedDepartment = event.item;
      this.dataInstance.contraDepartment = selectedDepartment;
      // eslint-disable-next-line no-console
      console.log('selectedDepartment ', selectedDepartment);
    }
  }
  onContraChange() {
    if (this.dataInstance.contra === 'No') {
      this.contraEmployeeDetail = undefined;
      this.dataInstance.contraEmployeeDetail = undefined;
      this.dataInstance.contraEmployeeId = undefined;
      // for department
      this.contraDepartmentDetail = undefined;
      this.dataInstance.contraDepartment = undefined;
    }
  }
  onMonthChange() {
    const ind = this.monthList.findIndex((data: any) => data.value === this.dataInstance.month);
    if (ind > -1) {
      this.dataInstance.year = this.monthList[ind].year;
    }
  }

  amountChanged() {
    if(this.dataInstance.amount) {
      const payoutAmountChanged = this.dataInstance.amount.split('.');
      if(payoutAmountChanged[0].length > 7) {
        this.jhiAlertService.error('arthaApp.payoutAdjustment.validation.payoutAmountValidity'); 
        this.dataInstance.amount = undefined;
        this.dataInstance.amount = null;
        return;
      }
    }
  }
}
