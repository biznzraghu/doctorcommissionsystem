import { Injectable } from '@angular/core';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, Input, OnInit, ChangeDetectorRef, ElementRef, Renderer2 } from '@angular/core'; // , ElementRef, Renderer2

@Component({
  template: `
    <div class="modal-body athma-modal-popup">
      <div class="modal-popup-container">
        <div class="d-flex align-items-center modal-popup-body">
          <i class="icon-alert modal-alert-icon" aria-hidden="true"></i>
          <p [innerHTML]="message"></p>
        </div>
        <div class="d-flex justify-content-end modal-popup-footer">
          <button type="button" id="cancelBtn" class="athma-btn athma-btn-secondary" (click)="activeModal.close(false)">
            Cancel
          </button>
          <button type="button" class="athma-btn athma-btn-primary athma-highlight-focus" (click)="activeModal.close(true)">Ok</button>
        </div>
      </div>
    </div>
  `
})
export class DialogComponent implements OnInit {
  @Input() title;
  @Input() message;
  @Input() name;

  constructor(
    public activeModal: NgbActiveModal,
    public changeRef: ChangeDetectorRef,
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) {}

  ngOnInit() {
    if (this.elementRef.nativeElement.querySelector('#cancelBtn')) {
      this.renderer.selectRootElement('#cancelBtn', true).scrollIntoView();
    }
  }
}

@Injectable()
export class DialogService {
  constructor(private modalService: NgbModal) {}
  public confirm() {
    const modalRef = this.modalService.open(DialogComponent, {
      backdrop: 'static',
      windowClass: 'athma-modal-dialog vertical-middle-modal xs  athma-warning-modal'
    });
    modalRef.componentInstance.name = 'You have unsaved changes';
    modalRef.componentInstance.message =
      'You have unsaved changes.<br> Click <strong>OK</strong> to discard the changes or <strong>Cancel</strong> to stay on the page.';
    // You have unsaved changes. Press OK to continue or Cancel to stay on the current page.
    modalRef.componentInstance.changeRef.markForCheck();
    return modalRef.result;
  }
}
