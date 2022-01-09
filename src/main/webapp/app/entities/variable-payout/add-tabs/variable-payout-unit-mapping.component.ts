import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { HttpResponse } from '@angular/common/http';
import { UnitService } from './../../administration/administration-services/unit.service';
import { JhiAlertService } from 'ng-jhipster';
import { Organization } from 'app/entities/artha-models/organization.model';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { SearchTermModify } from 'app/artha-helpers';

@Component({
  selector: 'jhi-variable-payout-unit-mapping',
  templateUrl: './variable-payout-unit-mapping.component.html'
})
export class VariablePayoutUnitMappingComponent implements OnInit {
  isExceptions: boolean;
  noRecordFound: boolean;
  searchText: string;
  unitSearching: boolean;
  // unitLists: any = [];
  unitSearch: any;
  @Input() templateOrganizations: Array<Organization>;
  @Input() viewMode: boolean;

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  constructor(private router: Router, private unitService: UnitService, private jhiAlertService: JhiAlertService, public searchTermModify: SearchTermModify) {}

  ngOnInit() {
    if (this.templateOrganizations && this.templateOrganizations.length === 0) {
      this.noRecordFound = true;
    } else {
      this.noRecordFound = false;
    }
    this.unitSearching = false;
  }

  search(event) {
    this.searchText = event;
  }

  clearSearch() {
    this.searchText = null;
  }

  unitSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.unitSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.unitService
              .search({
                query: `active:true AND (code:(${this.searchTermModify.modify(term)}) OR name:(${this.searchTermModify.modify(term)}))`,
                size: 9999
              })
              .map((res: HttpResponse<any>) => {
                return res.body;
              })
      )
      .do(() => (this.unitSearching = false));

  inputFormatUnitTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatUnitTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  selectedUnit(e) {
    if (this.templateOrganizations.find((ele: any) => ele.id === e.item.id)) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.unitExist');
      return;
    }
    this.templateOrganizations.unshift(e.item);
    this.unitSearch = { item: { name: '' } };
    if (this.templateOrganizations && this.templateOrganizations.length > 0) {
      this.noRecordFound = false;
    }
  }

  removeUnit(index: number) {
    this.templateOrganizations.splice(index, 1);
    if (this.templateOrganizations.length === 0) {
      this.noRecordFound = true;
    }
  }
}
