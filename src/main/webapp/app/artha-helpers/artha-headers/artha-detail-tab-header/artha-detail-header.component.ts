import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { ArthaDetailTabHeaderOptions } from './artha-detail-tab-header.model';

@Component({
  selector: 'jhi-artha-detail-tab-header',
  templateUrl: './artha-detail-tab-header.component.html'
})
export class ArthaDetailTapHeaderComponent implements OnInit {
  // To get details from parent component (here entities like role, country....) to child component
  @Input() options: ArthaDetailTabHeaderOptions;
  // (notify) search click to parent Component
  @Output() searchItem: EventEmitter<string> = new EventEmitter<string>();
  // (notify) clear click to parent component
  @Output() clearSearch: EventEmitter<string> = new EventEmitter<string>();
  // (notify) export click to parent component
  @Output() exportCSV: EventEmitter<string> = new EventEmitter<string>();

  @Output() onSelectedTabChange: EventEmitter<string> = new EventEmitter<string>();
  taskMode = 'Group Task';
  searchText: string;
  createLink: string;

  constructor(private router: Router) {}

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
  }
  export() {
    this.exportCSV.emit(this.searchText);
  }

  ngOnInit() {}

  onTabChange(index) {
    this.options.selectedTabIndex = index;
    this.onSelectedTabChange.emit(index);
  }
}
