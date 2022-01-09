import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { Preferences } from '../artha-models/preferences.model';
import { DashboardSettingComponent } from './dashboard-setting.component';

@Component({
  selector: 'jhi-dashboard-header',
  templateUrl: './dashboard-header.component.html'
})
export class DashboardHeaderComponent implements OnInit {
  // list:any;
  activeTab: number;
  @Input() tabs;
  @Input() authorities;
  @Input() preferences: Preferences;
  @Output() tabChange: EventEmitter<object> = new EventEmitter<object>();
  @Output() settingModalClose: EventEmitter<object> = new EventEmitter<object>();
  constructor(private modalService: NgbModal) {}

  ngOnInit() {
    this.activeTab = 0;
  }

  removeMovedItem(item, list) {
    list.splice(list.indexOf(item), 1);
  }
  selectTab(item) {
    item.edit = true;
  }

  deSelectTab(item) {
    item.edit = false;
  }

  changeTab(item, index) {
    this.activeTab = index;
    this.tabChange.emit(item);
  }

  openSettingPopup() {
    const ngModaloption: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      windowClass: 'athma-modal-dialog vertical-middle-modal md primary dashboard-setting-modal'
    };

    const moDalData = this.modalService.open(DashboardSettingComponent, ngModaloption);
    moDalData.componentInstance.pageName = 'Dashboard Setting';
    moDalData.componentInstance.tabs = this.tabs;
    moDalData.componentInstance.preferences = this.preferences;
    moDalData.componentInstance.authorities = this.authorities;

    moDalData.result.then(result => {
      if (result) {
        this.settingModalClose.emit();
        this.activeTab = 0;
      }
    });
  }
}
