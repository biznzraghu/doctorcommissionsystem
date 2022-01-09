import { Component, Output, EventEmitter, Input, OnChanges } from '@angular/core';
import { ArthaDetailHeaderOptions } from './artha-detail-header.model';
import { PayoutStatusClassMap, PayoutStatusCss } from 'app/entities/artha-models/status.enum';

@Component({
  selector: 'jhi-artha-detail-header',
  templateUrl: './artha-detail-header.component.html'
})
export class ArthaDetailHeaderComponent implements OnChanges {
  departmentIcon = '../../../../content/images/header_images/department.svg';
  commentIcon = '../../../../content/images/header_images/comments.svg';
  enableCommentIcon = '../../../../content/images/header_images/enable-Comments.svg';
  displayName = 'DemoUser';
  showSearchIcon = true;
  validatePatient = true;
  departmentSearch = false;
  showSearchBox = false;
  version: any;
  dateFormate = 'dd/MM/yyyy  HH:mm';
  public statusClassMap = PayoutStatusClassMap;
  public statusCssClassMap = PayoutStatusCss;
  @Input() options: ArthaDetailHeaderOptions;
  @Input() searchResultList: any;
  @Input() showEditIcon = false;
  @Output() onOpenComment = new EventEmitter();
  @Output() onUserSearchType = new EventEmitter();
  @Output() onUserSelect = new EventEmitter();
  @Output() addNewDepartment = new EventEmitter();
  @Output() onVersionChange = new EventEmitter();
  @Output() onEditDocument = new EventEmitter();

  approvedByCount: number;
  approvedByDisplayName: any;
  viewModeCommentIcon = true;
  constructor() {}

  ngOnChanges() {
    if (this.options) {
      // eslint-disable-next-line no-console
      console.log('OPTIONS::', this.options);
      if (this.options.isHeaderForEmployee && this.options.employee === null) {
        this.showSearchBox = true;
      }

      if (!this.options.isHeaderForEmployee && this.options.department === null) {
        this.showSearchBox = true;
        this.departmentSearch = true;
      }

      if (this.options.employee !== null || this.options.department !== null) {
        this.showSearchBox = false;
      }
      if (this.options.approvedBy && this.options.approvedBy.length > 1) {
        this.sortApprovedDateTime();
      }
      this.setApprovedByCount();
    } else {
      this.showSearchBox = true;
    }
  }

  sortApprovedDateTime() {
    this.options.approvedBy = this.options.approvedBy.sort(function(a: any, b: any) {
      return new Date(b.onDate).getTime() - new Date(a.onDate).getTime();
    });
  }

  onVersionClick(version: any) {
    this.version = version;
    this.onVersionChange.emit(version);
  }

  onUserSelection(user) {
    this.onUserSelect.emit(user);
  }

  onSearch(searchUser) {
    this.onUserSearchType.emit(searchUser);
  }

  onDepartmentSearch() {
    this.showSearchBox = true;
    this.departmentSearch = true;
    this.options.department = null;
    this.onUserSearchType.emit(null);
  }

  onAddDepartment() {
    this.addNewDepartment.emit();
  }

  openPatientDetails() {}
  onOpenCommentBlock() {
    if (this.options.remarksListCount && this.options.remarksListCount > 0) {
      this.onOpenComment.emit();
    }
  }
  /**
   * onClickEditDocument method only execute when user click on Edit icon on header.
   */
  onClickEditDocument() {
    this.viewModeCommentIcon = false;
    this.onEditDocument.emit();
  }
  setApprovedByCount() {
    this.approvedByCount = 0;
    if (this.options.approvedBy !== null && this.options.approvedBy !== undefined && this.options.approvedBy.length > 0) {
      this.options.approvedBy.forEach(appr => {
        if (appr.status === 'REJECTED' || appr.status === 'APPROVED') {
          this.approvedByDisplayName = appr.displayName;
          this.approvedByCount = ++this.approvedByCount;
        }
      });
      this.approvedByCount = this.approvedByCount - 1;
    } else {
      this.approvedByCount = 0;
    }
  }
}
