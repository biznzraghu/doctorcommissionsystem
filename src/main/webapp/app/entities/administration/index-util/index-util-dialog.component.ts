import { Component, OnInit, OnDestroy } from '@angular/core';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { INgxMyDpOptions } from 'ngx-mydatepicker';
import { ActivatedRoute } from '@angular/router';
import { IndexUtil } from 'app/entities/artha-models/index-util.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IndexUtilService } from '../administration-services/index-util.service';
import * as moment from 'moment';
import { TabInfo } from 'app/entities/artha-models/tab-info.model';
import { HttpErrorResponse } from '@angular/common/http';
import { IndexUtilHelper } from './index-util.helper';

@Component({
  selector: 'jhi-index-util-dialog',
  templateUrl: './index-util-dialog.component.html',
  providers: [IndexUtilHelper],
  styles: [
    `
      .indexDisabled {
        opacity: 0.8;
        pointer-events: none;
      }
    `
  ]
})
export class IndexUtilDialogComponent implements OnInit, OnDestroy {
  public subscription: any;
  public title: string;
  public createBtnTitle: string;
  public options: ListHeaderOptions;
  public tablist: any;
  public selectedTabIndex = 0;
  indexUtilDatepicker: INgxMyDpOptions = {
    dateFormat: 'dd/mm/yyyy',
    firstDayOfWeek: 'su',
    showSelectorArrow: false,
    selectorHeight: '200px',
    selectorWidth: '200px'
  };
  // ngx-mydatepicker;
  ngxFromDateModel: any = { jsdate: new Date() };
  ngxToDateModel: any = { jsdate: new Date() };
  indexUtil: IndexUtil;
  indexUtils: IndexUtil[];
  indexUtilList: any[] = [];
  utilTabDetail = [];
  isSaving: boolean;
  tabStatus = 'Department Payout';

  constructor(
    private route: ActivatedRoute,
    private eventManager: JhiEventManager,
    private alertService: JhiAlertService,
    private indexUtilService: IndexUtilService,
    private indexHelperService: IndexUtilHelper
  ) {
    this.title = 'Index Utility';
    this.createBtnTitle = 'Index All';
    this.tablist = [
      { label: 'Department Payout', value: 'Department Payout' },
      { label: 'Variable Payout', value: 'Variable Payout' },
      { label: 'Payout Adjustment', value: 'Payout Adjustment' },
      { label: 'MDM', value: 'MDM' }
    ];
    this.options = new ListHeaderOptions(
      'artha/administrator/value-set',
      true,
      false,
      true,
      true,
      false,
      true,
      null,
      true,
      this.tablist,
      this.selectedTabIndex
    );

    this.subscription = this.route.data.subscribe(data => {
      this.indexUtil = data['indexUtil'];
    });
    this.indexUtilList = this.indexHelperService.getIndexes();
    this.utilTabDetail = [];
  }
  ngOnInit() {
    this.isSaving = false;
    this.utilTabDetail.push(new TabInfo('Department Payout', 'Department Payout', 0, 0));
    this.utilTabDetail.push(new TabInfo('Variable Payout', 'Variable Payout', 1, 1));
    this.utilTabDetail.push(new TabInfo('Payout Adjustment', 'Payout Adjustment', 2, 2));
    this.utilTabDetail.push(new TabInfo('MDM', 'MDM', 3, 3));
  }
  indexAllUtility() {
    // alert('hey')
    this.indexAll(this.tabStatus);
  }
  indexAll(status: string) {
    const tabStatusOrder = this.checkTabStatus(status);
    for (let i = 0; i < this.indexUtilList.length; i++) {
      if (i === tabStatusOrder) {
        for (let j = 0; j < this.indexUtilList[i].length; j++) {
          for (let k = 0; k < this.indexUtilList[i][j].value.length; ++k) {
            this.doIndex(this.indexUtilList[i][j].value[k], i, j);
          }
        }
      }
    }
  }
  search(event) {
    event ? '' : '';
  }
  onTabChange(event) {
    this.selectedTabIndex = event;
    this.tabStatus = this.tablist[event].value;
  }
  checkTabStatus(status: string) {
    const selectedTab = this.utilTabDetail.filter(tabinfo => tabinfo.status === status);
    return selectedTab[0].displayOrder;
  }
  doIndex(indexUtil: IndexUtil, tabIndexVal, subTabIndex) {
    if (!this.validateDate()) {
      return;
    }

    const fromDt = moment(this.ngxFromDateModel.jsdate).format('YYYY-MM-DD');
    const toDt = moment(this.ngxToDateModel.jsdate).format('YYYY-MM-DD');

    indexUtil.isIndexing = true;
    // this.indexUtilService.query(indexUtil.master, { id: indexUtil.id })
    this.indexUtilService
      .query(indexUtil.master, { fromDate: fromDt, toDate: toDt }, tabIndexVal, subTabIndex, indexUtil.contain_index)
      .subscribe(
        (res: any) => {
          this.onSaveSuccess(res);
          indexUtil.isIndexing = false;
        },
        (res: HttpErrorResponse) => {
          this.onSaveError(res.error);
          indexUtil.isIndexing = false;
        }
      );
  }
  private onSaveSuccess(result: any) {
    result ? '' : '';
    this.eventManager.broadcast({ name: 'indexUtilListModification', content: 'OK' });
    this.isSaving = false;
  }

  private onSaveError(error) {
    this.isSaving = false;
    this.onError(error);
  }

  private onError(error) {
    this.alertService.error(error.message, null, null);
  }
  validateDate(): boolean {
    if (this.ngxFromDateModel.jsdate > this.ngxToDateModel.jsdate) {
      const errorMessage = '<strong>To Date</strong> should be Greater than OR Equal to <strong>From Date</strong>';
      this.alertService.error('global.messages.response-msg', { msg: errorMessage });
      return false;
    }
    return true;
  }
  clear() {}
  ngOnDestroy() {}
}
