import { JhiAlertService } from 'ng-jhipster';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, switchMap, finalize, map } from 'rxjs/operators';
import { UserService } from 'app/entities/administration/administration-services/user.service';
import { VariablePayoutService } from '../../variable-payout.service';
import { SearchTermModify } from 'app/artha-helpers';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-copy',
  templateUrl: './copy-popup.component.html'
})
export class CopyComponent {
  isDataSearching: boolean;
  searchText: string;
  entryType: string;
  selectedValue: any;
  isExistingRules: boolean;
  variablePayoutObj: any;
  versionInfo: any;
  latestVersionInfo: any;
  copyMessage: any;
  isVariablePayout: any;
  isTemplateSearching: boolean;
  templateSearch: any;
  selectedTemplate: any;
  templateInfo: any;
  templateId: any;
  remove: boolean;

  constructor(
    private userService: UserService,
    public activeModal: NgbActiveModal,
    private variablePayoutService: VariablePayoutService,
    private jhiAlertService: JhiAlertService,
    private searchTermModify: SearchTermModify,
    private httpBlockerService: HttpBlockerService,
    private router: Router
  ) {
    this.isExistingRules = false;
    this.remove = true;
  }

  employeeSearchTypeahead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(term => {
        this.isDataSearching = term.length >= 1;
      }),
      switchMap((term: string) =>
        term.length < 1
          ? of([])
          : this.userService
              .search({
                query: term.trim() ? this.searchTermModify.modify(term) : '*'
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

  employeeSearchData = x => x.displayName;

  onSelectEmployee(data) {
    if (data && data.item) {
      this.selectedValue = data.item;
      this.isExistingRules = true;
    }
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

  clearSearch() {
    this.searchText = null;
    this.selectedValue = null;
    this.isExistingRules = false;
  }

  onSelectTemplate(data) {
    if (data && data.item) {
      this.selectedTemplate = data.item;
      this.isExistingRules = true;
    }
  }

  clearTemplate() {
    this.templateSearch = null;
    this.selectedTemplate = null;
    this.isExistingRules = false;
  }

  cancel() {
    this.activeModal.dismiss();
  }

  copy() {
    if (this.isVariablePayout === true) {
      // Variable payout rule copy
      this.httpBlockerService.enableHttpBlocker(true);
      const employeeId = this.selectedValue.id;
      if (this.versionInfo === undefined) {
        this.latestVersionInfo = this.variablePayoutObj.version;
      } else {
        this.latestVersionInfo = this.versionInfo;
      }
      const params = {
        isApproved: this.variablePayoutObj.changeRequestStatus === 'APPROVED' ? true : false,
        toEmployee: employeeId,
        version: this.latestVersionInfo
      };
      this.variablePayoutService.getVariablePayoutRulesCopy(this.variablePayoutObj, params).subscribe(
        (res: any) => {
          this.copyRulesSuccess();
        },
        (err: HttpResponse<any>) => {
          this.onError(err);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    } else {
      this.httpBlockerService.enableHttpBlocker(true);
      const templateParams = {
        toTemplate: this.templateInfo.id,
        remove: this.remove
      };
      this.variablePayoutService.getVariableTemplateRulesCopy(this.selectedTemplate, templateParams).subscribe(
        (res: any) => {
          this.copyRulesSuccess();
          this.reloadRouter();
        },
        (err: HttpResponse<any>) => {
          this.onError(err);
          this.httpBlockerService.enableHttpBlocker(false);
        }
      );
    }
  }

  copyRulesSuccess() {
    this.httpBlockerService.enableHttpBlocker(false);
    this.activeModal.dismiss();
    this.jhiAlertService.success('global.messages.response-msg', { msg: 'Rules Copied Successfully.' });
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  reloadRouter() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([this.router.url]);
  }

  templateRuleSelect(e) {
    this.remove = false;
  }

  templateRuleRemove(e) {
    this.remove = true;
  }
}
