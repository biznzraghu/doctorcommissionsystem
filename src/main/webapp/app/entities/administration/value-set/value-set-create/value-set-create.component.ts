import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { ValueSet } from 'app/entities/artha-models/value-set.model';
import { ValueSetCode } from 'app/entities/artha-models/value-set-code.model';
import { JhiAlertService } from 'ng-jhipster';
import { ValueSetService } from '../../administration-services/value-set.service';
import { ErrorParser } from '../../../../artha-helpers/helper/error-parser.service';
import { ValueSetCodeService } from '../../administration-services/value-set-codes.service';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';

@Component({
  selector: 'jhi-value-set-create',
  templateUrl: './value-set-create.component.html'
})
export class ValueSetCreateComponent {
  options: ListHeaderOptions;
  valueSet: ValueSet = new ValueSet();
  valueSetCode: ValueSetCode;
  valueSetCodes: ValueSetCode[] = [];
  removeValueSetCodesId: number[] = [];
  title: string;
  pageEdit: boolean;
  isSaving: boolean;
  isEditingValueSetCode: boolean;
  editingValueSetCodeInd: number;
  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  public mainDivConfig: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  constructor(
    private elementRef: ElementRef,
    private router: Router,
    private route: ActivatedRoute,
    private valueSetService: ValueSetService,
    private valueSetCodeService: ValueSetCodeService,
    private jhiAlertService: JhiAlertService,
    private errorParser: ErrorParser,
    private httpBlockerService: HttpBlockerService
  ) {
    this.title = 'Value Set';
    this.options = new ListHeaderOptions(null, true, true, true, true, true, true, null, false);
    this.route.data.subscribe(data => {
      // eslint-disable-next-line no-console
      console.log('route.data ', data);
      if (data['valueSet'] && data['valueSet'].body) {
        this.valueSet = data['valueSet'].body;
        this.valueSetCodeService
          .search({
            page: 0,
            size: 1000,
            query: 'valueSet.id:' + this.valueSet.id
          })
          .subscribe((dataRes: any) => {
            // eslint-disable-next-line no-console
            console.log(dataRes);
            this.valueSetCodes = dataRes.body;
          });
      }
      this.valueSet.modifiable = true;
    });
  }
  save(editForm) {
    this.httpBlockerService.enableHttpBlocker(true);
    if (editForm.form.status === 'VALID') {
      // eslint-disable-next-line no-console
      console.log('valueSet ', this.valueSet);
      if (this.valueSet.id !== undefined) {
        this.valueSetService.update(this.valueSet).subscribe(
          (res: ValueSet) => this.onValueSetSaveSuccess(res),
          (err: Response) => this.onSaveError(err)
        );
      } else {
        this.valueSetService.create(this.valueSet).subscribe(
          (res: ValueSet) => this.onValueSetSaveSuccess(res),
          (err: Response) => this.onSaveError(err)
        );
      }
    } else {
      this.jhiAlertService.error('global.messages.response-msg', { msg: 'In-valid Form' }, null);
    }
  }
  private onValueSetSaveSuccess(res) {
    this.httpBlockerService.enableHttpBlocker(false);
    this.jhiAlertService.success('global.messages.response-msg', { msg: 'Changes successfully saved.' });
    this.saveValueSetCodes(res);
    this.clear();
  }
  private onSaveError(error) {
    this.httpBlockerService.enableHttpBlocker(false);
    this.isSaving = false;
    // eslint-disable-next-line no-console
    console.log('valueSet ', this.valueSet);
    this.onError(error);
  }
  saveValueSetCodes(res) {
    // eslint-disable-next-line no-console
    console.log(res);
    const newUnsavedValueSetCodes = this.valueSetCodes.filter((data: ValueSetCode) => data.id === null || data.id === undefined);
    newUnsavedValueSetCodes.forEach(data => (data.valueSet = res));
    // eslint-disable-next-line no-console
    console.log('newUnsavedValueSetCodes ', newUnsavedValueSetCodes);
    if (newUnsavedValueSetCodes.length > 0) {
      newUnsavedValueSetCodes.forEach((data: ValueSetCode) => {
        this.valueSetCodeService.create(data).subscribe(createRes => {
          // eslint-disable-next-line no-console
          console.log('new ValueSetCodes saved ', createRes);
        });
      });
    }
    if (this.removeValueSetCodesId.length > 0) {
      this.removeValueSetCodesId.forEach((id: number) => {
        this.valueSetCodeService.delete(id).subscribe(deleteRes => {
          // eslint-disable-next-line no-console
          console.log(`ValueSetCodes delete for ${id} `, deleteRes);
        });
      });
    }
  }
  private onError(error) {
    if (error.message === '10087') {
      this.errorParser.alertWithParams(error, { uniqueField: 'Value Set Code' });
    } else {
      this.jhiAlertService.error(
        error.message ? error.message : 'global.messages.response-msg',
        error.description ? { msg: error.description } : null,
        null
      );
    }
  }
  createValueSetCode() {
    this.valueSetCode = new ValueSetCode();
  }
  addValueSetCode() {
    // this.isEditingValueSetCode = false;
    // this.editingValueSetCodeInd = undefined;
    if (this.isEditingValueSetCode) {
      this.valueSetCodes[this.editingValueSetCodeInd] = this.valueSetCode;
    } else {
      this.valueSetCodes.push(this.valueSetCode);
    }
    this.pageEdit = true;
    this.clearValueSetCode();
  }
  clearValueSetCode() {
    this.valueSetCode = null;
    this.isEditingValueSetCode = false;
    this.editingValueSetCodeInd = undefined;
  }
  clear() {
    this.router.navigate(['/artha/administrator/value-set']);
  }
  editValueSetCode(valueSetCode, ind) {
    const editValueSetCode = { ...valueSetCode };
    this.valueSetCode = editValueSetCode;
    this.isEditingValueSetCode = true;
    this.editingValueSetCodeInd = ind;
    this.pageEdit = true;
  }
  removeValueSetCode(ind) {
    if (this.valueSetCodes[ind].id !== undefined) {
      this.removeValueSetCodesId.push(this.valueSetCodes[ind].id);
    }
    if (this.isEditingValueSetCode && this.editingValueSetCodeInd === ind) {
      this.clearValueSetCode();
    }
    // eslint-disable-next-line no-console
    console.log('this.removeValueSetCodesId ', this.removeValueSetCodesId);
    this.valueSetCodes.splice(ind, 1);
  }
  public setScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight;
  }
}
