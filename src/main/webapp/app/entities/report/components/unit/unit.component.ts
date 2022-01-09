import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { ReportComponent } from 'app/entities/report/report.component';
import { ReportService } from 'app/entities/report/report.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { SearchTermModify } from 'app/artha-helpers';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { concat, Observable, of, Subject, Subscription } from 'rxjs';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';

@Component({
  selector: 'jhi-unit',
  templateUrl: './unit.component.html',
  styleUrls: []
})
export class UnitComponent implements OnInit, OnDestroy {
  public unitListData: Observable<any[]>;
  unit;
  @Input() userIdentity: any;
  @Output() selectedUnit = new EventEmitter();
  public unitInput$ = new Subject<string>();
  public unitLoading;
  private refreshReport$: Subscription;

  constructor(private reportService: ReportService, private searchTermModify: SearchTermModify, private destroyer: DestroyHelperService) {
    // this.initUnitSearch();
  }

  ngOnInit(): void {
    if (this.userIdentity) {
      this.getUnits();
    }
    this.refreshReport$ = this.reportService.refreshSubjectAction$.subscribe(res => {
      this.unit = null;
      this.selectedUnit.emit('');
    });
  }

  ngOnDestroy(): void {
    this.destroyer.unsubscribe([this.refreshReport$]);
  }
  // getUnits() {
  //   this.reportService.getUnitByUser({ query: 'userMaster.id:' + this.userIdentity.id })
  //     .subscribe((res) => {
  //       this.unitListData = [];
  //       res.forEach(element => {
  //         this.unitListData.push(element.organization);
  //       });
  //     })
  // }

  getUnits() {
    this.unitListData = concat(
      of([]),
      this.unitInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.unitLoading = true)),
        switchMap((text: string) =>
          this.reportService
            .getUnitByUser({
              query: `userMaster.id:${this.userIdentity.id} AND ` + (text ? this.searchTermModify.modify(text) : '*')
            })
            .pipe(
              map((res: any) => {
                const units: any = [];
                if (res && res.length > 0) {
                  res.forEach(element => {
                    if (element && element.organization) {
                      units.push(element.organization);
                    }
                  });
                }

                return units;
              }),
              catchError(() => of([])),
              tap(() => (this.unitLoading = false))
            )
        )
      )
    );
  }

  onChange(event) {
    this.selectedUnit.emit(event);
  }
}
