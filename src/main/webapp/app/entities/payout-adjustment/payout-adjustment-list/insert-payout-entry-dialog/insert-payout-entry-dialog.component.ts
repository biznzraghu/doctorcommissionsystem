import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, switchMap, finalize, map } from 'rxjs/operators';
import { DepartmentService } from 'app/entities/administration/administration-services/department.service';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { SearchTermModify } from 'app/artha-helpers';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-insert-payout-entry-dialog',
  templateUrl: './insert-payout-entry-dialog.component.html'
})
export class InsertPayoutDialogComponent {
  // Search box variables
  public searchText: string;
  public entryType = '';
  public isDataSearching: boolean;
  private selectedValue = {
    data: null,
    entryType: this.entryType
  };
  public errorMessage: string;
  public showError: boolean;
  public unitId: string;

  constructor(
    public activeModal: NgbActiveModal,
    public searchTermModify: SearchTermModify,
    public departmentService: DepartmentService,
    private userService: UserService
  ) {
    this.entryType = 'Employee';
  }
  process() {
    this.selectedValue.entryType = this.entryType;
    if (this.selectedValue.entryType && this.selectedValue.data) {
      this.activeModal.close(this.selectedValue);
    } else {
      this.showError = true;
      this.entryType === 'Employee' ? (this.errorMessage = 'Please select Employee !') : (this.errorMessage = 'Please select Department !');
    }
  }
  close() {
    this.activeModal.dismiss();
  }
  /**
   *
   *
   *
   * (serviceMaster.code:(*abc* ) OR serviceMaster.name:(*abc* ) OR serviceMaster.shortName:(*abc* )) AND
   *  serviceMaster.visitType.code:AMB AND serviceMaster.applicableGender.code:male
   */
  departmentSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.isDataSearching = term.length >= 1;
      }),
      switchMap((term: string) =>
        term.length < 1
          ? of([])
          : this.departmentService // this.searchTermModify.modify(term)
              .search({
                limit: 20,
                active: true,
                query: term.trim()
                  ? (this.unitId ? `active:true AND organization.id: ${this.unitId} ` : 'active:true ') + this.searchTermModify.modify(term)
                  : // ' AND ' +
                    // `(code:(*${term}*) OR name:(*${term}*))`
                    (this.unitId ? `active:true AND organization.id: ${this.unitId}` : '') + ' *'
              })
              .pipe(
                map((res: any) => {
                  return res.body;
                })
              )
      ),
      tap(() => (this.isDataSearching = false)),
      finalize(() => (this.isDataSearching = false))
    );

  departmentSearchData = x => x.name;

  onSelectDepartment(event) {
    if (event && event.item) {
      this.showError = false;
      this.selectedValue.data = event.item;
    }
  }

  employeeSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.isDataSearching = term.length >= 1;
      }),
      switchMap(
        (term: string) =>
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
        // : this.userService
        //     .search({
        //       // limit: 20,
        //       query: term.trim() ? this.searchTermModify.modify(term) : '*'
        //     })
        //     .pipe(
        //       map((res: any) => {
        //         return res.body;
        //       })
        //     )
      ),
      tap(() => (this.isDataSearching = false)),
      finalize(() => (this.isDataSearching = false))
    );

  employeeSearchData = x => x.displayName;

  onSelectEmployee(event) {
    if (event && event.item) {
      this.showError = false;
      this.selectedValue.data = event.item;
    }
  }
  public entryTypeChange() {
    this.showError = false;
    this.selectedValue.entryType = this.entryType;
  }
  clearSearch() {
    this.searchText = null;
    this.selectedValue.data = null;
  }
}
