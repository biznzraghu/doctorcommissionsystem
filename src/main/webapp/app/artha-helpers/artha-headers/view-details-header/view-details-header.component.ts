import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';

import { NgbTypeaheadConfig } from '@ng-bootstrap/ng-bootstrap';

import { SearchTermModify } from 'app/artha-helpers';
import { ViewDetailsHeaderOptions } from './view-details-header-options.model';
import { ViewDetailsHeaderService } from './view-details-header.service';

import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, switchMap, catchError, map } from 'rxjs/operators';

@Component({
  selector: 'jhi-artha-view-details-header',
  templateUrl: './view-details-header.component.html',
  providers: [NgbTypeaheadConfig]
})
export class ViewDetailsHeaderComponent implements OnInit {
  // To get details from parent component ( like enable edit, audit log & ...) to View-details-header component
  @Input() viewDetailsHeaderOptions: ViewDetailsHeaderOptions;

  // (notify) AuditLog click to parent Component
  @Output() auditLog: EventEmitter<string> = new EventEmitter<string>();
  // (notify) Edit view click to parent component
  @Output() editView: EventEmitter<string> = new EventEmitter<string>();
  // (notify) Back to list click to parent component
  @Output() backToList: EventEmitter<string> = new EventEmitter<string>();
  // (notify) selected search result item to parent Component
  @Output() searchResult: EventEmitter<Object> = new EventEmitter<Object>();

  @Output() openNew: EventEmitter<string> = new EventEmitter<string>();

  // (notify) AuditLog click to parent Component
  @Output() showAuditVersion: EventEmitter<any> = new EventEmitter<any>();

  createLink: string;
  searching = false;
  displayFormatSearchTypeheadData: any;
  inputFormatSearchTypeheadData: any;
  inputPlaceholderText: string;
  showRevision: boolean;

  constructor(
    private router: Router,
    private viewDetailsHeaderService: ViewDetailsHeaderService,
    private searchTermModify: SearchTermModify
  ) {
    this.inputPlaceholderText = 'Search';
  }

  editPage() {
    if (this.viewDetailsHeaderOptions.editPageLink) {
      this.router.navigate([this.viewDetailsHeaderOptions.editPageLink], { replaceUrl: true });
    } else {
      this.editView.emit('');
    }
  }

  backToListView() {
    this.backToList.emit();
  }

  ngOnInit() {
    this.createLink = this.viewDetailsHeaderOptions.entityname + '-new';
    this.inputFormatSearchTypeheadData = this.viewDetailsHeaderOptions.inputformatSearchTypeheadData;
    this.displayFormatSearchTypeheadData = this.viewDetailsHeaderOptions.displayformatSearchTypeheadData;
    this.inputPlaceholderText = this.viewDetailsHeaderOptions.inputPlaceholderText;
  }

  createNew() {
    if (this.viewDetailsHeaderOptions.emitCreateNew) {
      this.openNew.emit('');
    } else {
      this.router.navigate([this.createLink], { replaceUrl: true });
    }
  }

  // search -NgbTypeaheadConfig
  searchViewHeaderTypehead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(term => (this.searching = term.length >= 3)),
      switchMap(term => {
        return term.length < 3
          ? of([])
          : this.viewDetailsHeaderService
              .search(this.viewDetailsHeaderOptions.searchURL, {
                query:
                  (this.viewDetailsHeaderOptions.searchParams ? this.viewDetailsHeaderOptions.searchParams : ' ') +
                  this.searchTermModify.modify(term)
              })
              .pipe(
                map(res => {
                  return res.body;
                }),
                tap(() => (this.searching = false)),
                catchError(() => {
                  this.searching = true;
                  return of([]);
                })
              );
      }),
      tap(() => (this.searching = false))
    );

  onSelectSearchResultItem($event, searchViewHO) {
    $event.preventDefault();
    this.searchResult.emit($event.item);
    searchViewHO.value = '';
  }

  auditLogView() {
    if (this.viewDetailsHeaderOptions.module && this.viewDetailsHeaderOptions.auditEntityType) {
      this.showRevision = true;
    }
  }

  showVersion($event) {
    this.showAuditVersion.emit($event);
  }
}
