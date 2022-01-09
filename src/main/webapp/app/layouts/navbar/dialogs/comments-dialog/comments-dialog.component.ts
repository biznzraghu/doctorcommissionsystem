import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { PreferenceService } from 'app/artha-helpers/services/preference.service';
import * as moment from 'moment';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { TransactionApprovalDetailsDTO, UserCommentModel } from 'app/entities/artha-models/comment.model';
@Component({
  selector: 'jhi-comments-dialog',
  templateUrl: './comments-dialog.component.html'
})
export class CommentDialogComponent implements OnInit {
  displayFormat = 'dd/MM/yyyy HH:mm';
  @Input() transctionDetailList: Array<TransactionApprovalDetailsDTO> = []; // trsnstion list contain array of commnts
  @Input() isDisplayMode = false; // if isDisplayMode true then comment area will not visible and only cancel btn will display
  @Input() headerTitle = 'Add Comment';
  @Input() displayRejectBtn = false; // if user need reject button for handling reject functanility so display Reject btn should be true
  @Input() primaryBtnLabel = 'Send For Approval'; // label for primary functional btn name like Approve , Send For Approval etc. if isDisplayMode false then this btn will display
  @Input() primaryBtnClass = 'athma-btn-primary';

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };
  comment = '';

  preferences: any;
  constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService, private preferencesService: PreferenceService) {
    this.preferences = this.preferencesService.currentUser();
  }

  ngOnInit() {
    if (this.isDisplayMode) {
      if (!this.headerTitle) {
        this.headerTitle = 'Comments';
      }
      this.displayRejectBtn = false;
      this.sortApprovedDateTime();
    }
  }

  sortApprovedDateTime() {
    this.transctionDetailList = this.transctionDetailList.sort(function(a: any, b: any) {
      return new Date(b.approvedDateTime).getTime() - new Date(a.approvedDateTime).getTime();
    });
  }
  

  no() {
    this.activeModal.close();
  }

  yes(actionName?) {
    const commentData: string = this.comment.trim();
    if (commentData.length > 0) {
      const action = actionName ? actionName : this.primaryBtnLabel;

      const userComment: UserCommentModel = {
        comment: commentData,
        createdDateTime: moment().format('YYYY-MM-DDTHH:mm:ss[Z]'),
        createdBy: this.preferences.user
      };
      const result = {
        userComment,
        action
      };
      this.activeModal.close(result);
    } else {
      this.jhiAlertService.error('global.messages.response-msg', { msg: 'Enter Comment.' });
    }
  }
}
