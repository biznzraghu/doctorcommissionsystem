import { Component, OnInit, Input, OnChanges, Output, EventEmitter, OnDestroy } from '@angular/core';
import { ReportService } from '../../report.service';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { concat, Observable, of, Subject, Subscription } from 'rxjs';
import { SearchTermModify } from 'app/artha-helpers';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';

@Component({
  selector: 'jhi-department',
  templateUrl: './department.component.html',
  styleUrls: []
})
export class DepartmentComponent implements OnInit, OnChanges, OnInit, OnDestroy {
  selectedDept;
  deptList;
  @Input() selectedUnit;
  public departmentInput$ = new Subject<string>();
  public departmentLoading;
  @Output() emitDepartment = new EventEmitter();
  private refreshReport$: Subscription;

  constructor(private reportService: ReportService, private searchTermModify: SearchTermModify, private destroyer: DestroyHelperService) {}

  ngOnChanges() {
    if (this.selectedUnit) {
      this.selectedDept = null;
      this.getDepartmentByUnit();
    }
  }

  ngOnInit(): void {
    this.refreshReport$ = this.reportService.refreshSubjectAction$.subscribe(res => {
      this.selectedDept = null;
      this.emitDepartment.emit({ user: '' });
    });
  }

  ngOnDestroy(): void {
    this.destroyer.unsubscribe([this.refreshReport$]);
  }

  // getDepartmentByUnit() {
  //   this.reportService.getDeptByUnit({
  //     query: 'organization.code:' + this.selectedUnit
  //   }).subscribe((dept) => {
  //     this.deptList = dept;
  //   })
  // }

  getDepartmentByUnit() {
    this.deptList = concat(
      of([]),
      this.departmentInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.departmentLoading = true)),
        switchMap((text: string) =>
          this.reportService
            .getDeptByUnit({
              query: `organization.code:${this.selectedUnit} AND ` + (text ? this.searchTermModify.modify(text) : '*')
            })
            .pipe(
              map((res: any) => {
                const departments: any = [];
                if (res && res.length > 0) {
                  res.forEach(element => {
                    if (element) {
                      departments.push(element);
                    }
                  });
                }
                return departments;
              }),
              catchError(() => of([])),
              tap(() => (this.departmentLoading = false))
            )
        )
      )
    );
  }

  onChange(event) {
    if (event) {
      this.emitDepartment.emit({ user: event.id });
    } else {
      this.emitDepartment.emit({ user: '' });
    }
  }
}
