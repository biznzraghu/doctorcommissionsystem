import { Component, OnInit, ElementRef } from '@angular/core';
import { ValueSet } from 'app/entities/artha-models/value-set.model';
import { ValueSetCode } from 'app/entities/artha-models/value-set-code.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ValueSetCodeService } from '../../administration-services/value-set-codes.service';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/switchMap';
import { Observable } from 'rxjs/Rx';
import { SearchTermModify } from 'app/artha-helpers';
import { ValueSetService } from '../../administration-services/value-set.service';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
@Component({
  selector: 'jhi-value-set-detail',
  templateUrl: './value-set-detail.component.html'
})
export class ValueSetDetailComponent implements OnInit {
  options: ListHeaderOptions;
  pageName: string;
  title: string;
  createBtnTitle: string;
  searchText: string;
  isSearching: boolean;
  valueSet: ValueSet;
  valueSetCodes: ValueSetCode[] = [];
  constructor(
    private elementRef: ElementRef,
    private router: Router,
    private route: ActivatedRoute,
    private valueSetCodeService: ValueSetCodeService,
    private valueSetService: ValueSetService,
    private searchTermModify: SearchTermModify
  ) {
    this.route.data.subscribe(data => {
      // eslint-disable-next-line no-console
      console.log('route.data ', data);
      this.valueSet = data['valueSet'].body;
      this.valueSetCodeService
        .search({
          page: 0,
          size: 1000,
          query: 'valueSet.id:' + this.valueSet.id
        })
        .subscribe((dataRes: any) => {
          this.valueSetCodes = dataRes.body;
        });
    });
  }
  ngOnInit() {
    this.pageName = 'valueSet';
    this.title = 'Value Set';
    this.createBtnTitle = 'Create New';
    this.options = new ListHeaderOptions('artha/administrator/value-set', false, false, false, true, false, false, null, false);
  }
  public setScrollHeight() {
    const headerElementHeight = document.getElementsByTagName('header')[0].offsetHeight;
    const pageHeaderHeight = this.elementRef.nativeElement.querySelector('div.athma-page-header').offsetHeight;
    return window.innerHeight - headerElementHeight - pageHeaderHeight;
  }
  editValueSet() {
    this.router.navigate([`artha/administrator/value-set/${this.valueSet.id}/edit`]); // /edit /detail
  }
  clear() {
    this.searchText = undefined;
  }
  backToList() {
    this.router.navigate(['/artha/administrator/value-set']);
  }
  // search -NgbTypeaheadConfig
  searchViewHeaderTypehead = (text: Observable<string>) =>
    text
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => (this.isSearching = term.length >= 1))
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.valueSetService
              .search({
                page: 0,
                size: 1000,
                query: this.searchTermModify.modify(term)
              })
              .map(res => {
                return res.body;
              })
      )
      .do(() => (this.isSearching = false));

  inputformatSearchTypeheadData = (x: ValueSet) => x.name;
  displayformatSearchTypeheadData = (x: ValueSet) => x.code + ' | ' + x.name;

  onSelectSearchResultItem($event, searchValueSet) {
    // $event.preventDefault();
    const data = $event && $event.item;
    searchValueSet ? '' : '';
    // searchValueSet.value = '';
    this.searchResult(data);
  }
  searchResult(result: ValueSet) {
    this.router.navigate([`artha/administrator/value-set/${result.id}/detail`], { replaceUrl: true });
  }
}
