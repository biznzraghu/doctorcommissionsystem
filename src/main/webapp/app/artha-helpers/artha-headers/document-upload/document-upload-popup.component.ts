import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
// import { Observable, of } from 'rxjs';
// import { debounceTime, distinctUntilChanged, tap, switchMap, finalize, map } from 'rxjs/operators';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-document-upload-popup',
  templateUrl: './document-upload-popup.component.html'
})
export class DocumentUploadPopUpComponent {
  @Input() headerTitle = 'Select and Upload Document';
  @Input() isMultipleFiles = false;
  @Input() acceptFiles = 'application/pdf, text/plain';

  @Output() importAction: EventEmitter<any> = new EventEmitter<any>();
  @Output() dismiss: EventEmitter<string> = new EventEmitter<string>();

  documentRecord: any;
  isSaving: boolean;
  preferences: Preferences;
  patientInformation: any;
  fileHolderList: any = [];
  saveDocuments: any[] = [];
  documentClass: any; // default Document type
  isDocumentUploading: boolean;
  // document: NewDocument;
  selectedTab: string;
  unit: any;
  valueSetList: any[] = [];
  constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService) {}

  removeFile(index) {
    this.fileHolderList.splice(index, 1);
    this.saveDocuments.splice(index, 1);
  }
  uploadFile(fileData) {
    const files = fileData.files;
    if (files.length > 0) {
      for (let i = 0; i < files.length; i++) {
        const formData = new FormData();
        formData.append('file', files[i]);
        this.fileHolderList.push(files[i]);
        const documentData: any = {
          fileName: files[i].name,
          fileSize: files[i].size,
          documentClass: this.documentClass,
          typeList: [],
          type: null,
          patient: this.patientInformation,
          unit: this.unit
        };
        this.saveDocuments.push(documentData);
        const lastIndex = this.saveDocuments.length - 1;
        if (lastIndex >= 0) {
          // this.documentClassSelect(lastIndex);
        }
      }
    }
  }

  onDrop(data) {
    if (data.dataTransfer.files) {
      const FILES = data.dataTransfer.files;
      for (let i = 0; i < FILES.length; i++) {
        if (FILES[i].type !== '') {
          this.fileHolderList.push(FILES[i]);
          const documentData: any = {
            fileName: FILES[i].name,
            fileSize: FILES[i].size,
            documentClass: this.documentClass,
            type: null,
            patient: this.patientInformation,
            unit: this.unit
          };
          this.saveDocuments.push(documentData);
        }
      }
    }
  }

  documentClassSelect(index) {
    if (this.saveDocuments[index].documentClass && this.saveDocuments[index].documentClass.code !== undefined) {
      // this.getDocumentValueSetCode(index, this.saveDocuments[index].documentClass.code);
    }
  }

  cancel() {
    this.dismiss.emit('Cancel');
  }

  importFromFile() {
    this.importAction.emit(this.fileHolderList);
  }
}
