import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ListHeaderOptions } from './list-header.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-athma-list-header',
  templateUrl: './list-header.component.html'
})
export class ListHeaderComponent implements OnInit, OnDestroy {
  // @Input() pageName = '';
  @Input() title = '';
  @Input() createBtnTitle = '';
  // To get details from parent component (here entities like role, country....) to child component
  @Input() options: ListHeaderOptions;
  // (notify) search click to parent Component
  @Output() searchItem: EventEmitter<string> = new EventEmitter<string>();
  // (notify) clear click to parent component
  @Output() clearSearch: EventEmitter<string> = new EventEmitter<string>();
  // (notify) export click to parent component
  @Output() exportCSV: EventEmitter<string> = new EventEmitter<string>();
  // (notify) new click to parent component
  @Output() openNew: EventEmitter<string> = new EventEmitter<string>();
  // (notify) audit click to parent component
  @Output() openAudit: EventEmitter<string> = new EventEmitter<string>();

  @Output() openImportDocument: EventEmitter<string> = new EventEmitter<string>();

  @Output() openAdvanceSearch: EventEmitter<string> = new EventEmitter<string>();

  @Output() onSelectedTabChange: EventEmitter<string> = new EventEmitter<string>();
  taskMode = 'Group Task';
  searchText: string;
  createLink: string;
  showClearSearch = false;
  eventClearSubscriber: Subscription;
  createPrivileges: string[];
  constructor(private router: Router, private eventManager: JhiEventManager) {}

  search(searchTerm) {
    if (searchTerm !== undefined) {
      this.searchText = searchTerm;
      if (this.searchText === undefined) {
        this.searchItem.emit(this.searchText);
      } else if (this.searchText.trim().length === 0) {
        this.searchItem.emit('*');
      } else if (this.searchText.trim().length >= 3) {
        this.searchItem.emit(this.convertToSpecialString(this.searchText));
      }
    }
  }

  convertToSpecialString(strData) {
    let cleanStr = strData.trim();
    cleanStr = cleanStr.replace(/\+/g, ' ');
    cleanStr = cleanStr.replace(/  +/g, ' ');
    cleanStr = cleanStr.replace(/"/g, '');
    // cleanStr = cleanStr.replace(/\:/g, '\\:');
    cleanStr = cleanStr.replace(/\//g, '\\/');
    cleanStr = cleanStr.replace(/\[/g, '\\[');
    cleanStr = cleanStr.replace(/\]/g, '\\]');
    cleanStr = cleanStr.replace(/\(/g, '\\(');
    cleanStr = cleanStr.replace(/\)/g, '\\)');
    const splitStr = cleanStr.split(' ');
    let convertStr = '';
    splitStr.map(function(obj) {
      // function(obj, index?)
      convertStr += '*' + obj + '*' + ' ';
    });
    return convertStr;
  }

  clear() {
    this.searchText = '';
    this.clearSearch.emit();
    this.showClearSearch = false;
  }
  export() {
    this.exportCSV.emit(this.searchText);
  }
  createNew() {
    if (this.options.openNew) {
      this.openNew.emit('');
    } else {
      this.router.navigate([this.createLink], { replaceUrl: true });
    }
  }
  ngOnInit() {
    this.createLink = this.options.entityname + '-new';
    if (this.options && this.options.createPrivileges) {
      this.createPrivileges = this.options.createPrivileges;
    }
    this.eventClearSubscriber = this.eventManager.subscribe('clearSearch', (res: any) => {
      if (res.content) {
        this.showClearSearch = true;
      } else {
        this.showClearSearch = false;
      }
    });
  }

  onTabChange(index) {
    this.options.selectedTabIndex = index;
    this.onSelectedTabChange.emit(index);
  }

  audit() {
    this.openAudit.emit('');
  }

  importDocument() {
    this.openImportDocument.emit();
  }

  saveSearch() {
    this.openAdvanceSearch.emit();
  }

  goToVariablePayout() {
    this.router.navigate(['artha/variable-payouts/variable-payouts-new'], { replaceUrl: true });
  }

  // goToTemplate() {
  //   this.router.navigate(['artha/variable-payouts/variable-payouts-template'], { replaceUrl: true });
  // }

  goToTemplate() {
    // const ngbModalOptions: NgbModalOptions = {
    //   backdrop: 'static',
    //   keyboard: true,
    //   windowClass: 'athma-modal-dialog vertical-middle-modal sm primary about-product-popup'
    // };
    // this.modalRef = this.modalService.open(InsertPayoutDialogComponent, ngbModalOptions);
    // this.modalRef.result.then(
    //   result => {
    //     if (result) {

    //     }
    //   },
    //   () => {}
    // );
    this.router.navigate(['artha/variable-payouts/variable-payouts-template-popup'], { replaceUrl: true });
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventClearSubscriber);
  }
}
