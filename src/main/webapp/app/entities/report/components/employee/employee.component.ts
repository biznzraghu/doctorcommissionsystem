import { Component, OnInit, Input, OnChanges, Output, EventEmitter, OnDestroy } from '@angular/core';
import { ReportService } from '../../report.service';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { concat, Observable, of, Subject, Subscription } from 'rxjs';
import { SearchTermModify } from 'app/artha-helpers';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';

@Component({
  selector: 'jhi-employee',
  templateUrl: './employee.component.html',
  styleUrls: []
})
export class EmployeeComponent implements OnInit, OnChanges, OnDestroy {
  @Input() selectedUnit;
  employeeList: Observable<any[]>;
  selectedEmployee;
  @Output() emitEmployee = new EventEmitter();
  public employeeLoading;
  public employeeInput$ = new Subject<string>();
  private refreshReport$: Subscription;
  constructor(private reportService: ReportService, private searchTermModify: SearchTermModify, private destroyer: DestroyHelperService) {}

  ngOnChanges() {
    if (this.selectedUnit) {
      this.selectedEmployee = null;
      this.getUSer();
    }
  }

  ngOnInit(): void {
    this.refreshReport$ = this.reportService.refreshSubjectAction$.subscribe(res => {
      this.selectedEmployee = null;
      this.emitEmployee.emit({ user: '' });
    });
  }

  ngOnDestroy(): void {
    this.destroyer.unsubscribe([this.refreshReport$]);
  }

  // getUSer() {
  //   this.reportService.getUserOrganizationMapping({
  //     query: 'organization.code:' + this.selectedUnit,
  //     size: 9999
  //   }).subscribe((result) => {
  //     this.employeeList = [];
  //     result.forEach(element => {
  //       this.employeeList.push(element.userMaster);
  //     });
  //   });
  // }

  getUSer() {
    this.employeeList = concat(
      of([]),
      this.employeeInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.employeeLoading = true)),
        switchMap((text: string) =>
          this.reportService
            .getUserOrganizationMapping({
              query: `organization.code:${this.selectedUnit} AND ` + (text ? this.searchTermModify.modify(text) : '*')
            })
            .pipe(
              map((res: any) => {
                const units: any = [];
                if (res && res.length > 0) {
                  res.forEach(element => {
                    if (element && element.userMaster) {
                      units.push(element.userMaster);
                    }
                  });
                }
                return units;
              }),
              catchError(() => of([])),
              tap(() => (this.employeeLoading = false))
            )
        )
      )
    );
  }

  onChange(event) {
    if (event) {
      this.emitEmployee.emit({ user: event.id });
    } else {
      this.emitEmployee.emit({ user: '' });
    }
  }
}
