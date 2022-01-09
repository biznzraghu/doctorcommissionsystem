import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NavbarService } from '../../navbar.service';
import { JhiAlertService } from 'ng-jhipster';
import { Preferences } from '../../../../entities/artha-models/preferences.model';
// import { Subscription } from 'rxjs/internal/Subscription';
import { AccountService } from 'app/core/auth/account.service';
// import { Observable } from 'rxjs/Observable';
// import { map } from 'rxjs/operators';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { concat, Observable, of, Subject, Subscription } from 'rxjs';
import { SearchTermModify } from 'app/artha-helpers';

@Component({
  selector: 'jhi-unit-change-dialog',
  templateUrl: './unit-change-dialog.component.html'
})
export class UnitChangeComponent implements OnInit, OnDestroy {
  // public unitListData: any[] = [];
  public unitListData: Observable<any[]>;
  public unit: any;
  private defaultPreference: Preferences;
  private subscriptions: Subscription;
  userIdentity;
  userDetailObservable$: Observable<any>;
  public unitInput$ = new Subject<string>();
  public unitLoading;

  constructor(
    public activeModal: NgbActiveModal,
    private navbarService: NavbarService,
    private alertService: JhiAlertService,
    private accountService: AccountService,
    private stateStorageService: StateStorageService,
    private router: Router,
    private searchTermModify: SearchTermModify
  ) {
    this.userDetailObservable$ = this.accountService.getAuthenticationState();
    if (this.userDetailObservable$) {
      this.userDetailObservable$.subscribe((res: any) => {
        this.userIdentity = res;
      });
      if (!this.userIdentity) {
        this.accountService.identity().subscribe((res: any) => {
          this.userIdentity = res;
        });
      }
    }

    this.subscriptions = this.navbarService.getPreference({ userId: this.userIdentity.id }).subscribe((preference: Preferences) => {
      if (preference) {
        this.defaultPreference = preference;
        this.unit = preference.organization;
      }
    });
  }

  ngOnInit() {
    this.getUnits();
  }

  no() {
    this.activeModal.close(false);
  }

  yes() {
    this.navbarService.savePreferenceUnit(this.unit.id).subscribe(result => {
      // console.log(result);
      if (result.status === 200) {
        this.activeModal.close(true);
        this.accountService.identity(true).subscribe(() => {});
        this.stateStorageService.clearFromSessionStorage('preferences');
        this.stateStorageService.setValue('preferences', this.defaultPreference);
        this.alertService.success('global.messages.response-msg', { msg: 'Preference Successfully Saved' });
      }
    });
  }

  // getUnits() {
  //   this.navbarService.getUnitByUser({ query: 'userMaster.id:' + this.userIdentity.id }).subscribe(res => {
  //     this.unitListData = [];
  //     res.forEach(element => {
  //       this.unitListData.push(element.organization);
  //     });
  //   });
  // }

  getUnits() {
    this.unitListData = concat(
      of([]),
      this.unitInput$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.unitLoading = true)),
        switchMap((text: string) =>
          this.navbarService
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

  save() {}

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
