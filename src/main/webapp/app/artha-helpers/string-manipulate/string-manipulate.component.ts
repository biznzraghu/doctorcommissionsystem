import { Component, Input, OnInit, OnChanges } from '@angular/core';

@Component({
  selector: 'jhi-athma-string-manipulation',
  template: `
    <span *ngIf="isTrimmed && !tooltipData">{{ trimmedText }}</span>
    <span
      *ngIf="isTrimmed && tooltipData && placementValue === 'right'"
      container="body"
      [ngbTooltip]="displayTextValue"
      placement="right"
      >{{ trimmedText }}</span
    >
    <span
      *ngIf="isTrimmed && tooltipData && placementValue === 'bottom'"
      container="body"
      [ngbTooltip]="displayTextValue"
      placement="bottom"
      >{{ trimmedText }}</span
    >
    <span *ngIf="isTrimmed && tooltipData && placementValue === 'left'" container="body" [ngbTooltip]="displayTextValue" placement="left">{{
      trimmedText
    }}</span>
    <span *ngIf="!isTrimmed">{{ trimmedText }}</span>
  `
})
export class AthmaStringManipulationComponent implements OnInit, OnChanges {
  displayTextValue: string;
  trimmedText: string;
  isTrimmed: boolean;
  stringLengthValue: number;
  tooltipData: boolean;
  trimMethod: string;
  trimMethodChoices: any;
  @Input() placementValue = 'bottom';
  @Input() suffixCharacterLength = 9;
  @Input() prefixCharacterLength = 14;
  constructor() {
    this.isTrimmed = false;
    this.tooltipData = false;
    this.trimMethodChoices = {
      stringTrim: 'string-trim',
      stringModified: 'string-modified'
    };
  }

  ngOnInit(): void {}

  ngOnChanges(): void {
    // if (this.trimMethod === this.trimMethodChoices.stringTrim) {
    //   this.stringTrim = this.stringTrim;
    // }
    // if (this.trimMethod === this.trimMethodChoices.stringModified) {
    //   this.stringModified = this.stringModified;
    // }
  }

  @Input() get stringLength() {
    return this.stringLengthValue;
  }
  set stringLength(val) {
    this.stringLengthValue = val;
  }

  @Input()
  get tooltipDisplay() {
    return this.tooltipData;
  }
  set tooltipDisplay(val) {
    this.tooltipData = val;
  }

  @Input()
  get stringModified() {
    return this.displayTextValue;
  }

  set stringModified(val) {
    this.displayTextValue = val;
    this.trimMethod = this.trimMethodChoices.stringModified;
    if (this.stringLengthValue) {
      this.isTrimmed = this.displayTextValue ? this.displayTextValue.length > this.stringLengthValue : false;
      this.trimmedText = this.isTrimmed
        ? this.displayTextValue.substr(0, this.stringLengthValue - this.prefixCharacterLength) +
          ' ...' +
          this.displayTextValue.substr(-this.suffixCharacterLength)
        : this.displayTextValue;
    } else {
      this.isTrimmed = this.displayTextValue ? this.displayTextValue.length > 50 : false;
      this.trimmedText = this.isTrimmed
        ? this.displayTextValue.substr(0, this.prefixCharacterLength) + ' ...' + this.displayTextValue.substr(-this.suffixCharacterLength)
        : this.displayTextValue;
    }
  }

  @Input()
  get stringTrim() {
    return this.displayTextValue;
  }

  set stringTrim(val) {
    this.displayTextValue = val;
    this.trimMethod = this.trimMethodChoices.stringTrim;

    if (this.stringLengthValue) {
      this.isTrimmed = this.displayTextValue ? this.displayTextValue.length >= this.stringLengthValue : false;
      this.trimmedText = this.isTrimmed ? this.displayTextValue.substr(0, this.stringLengthValue - 4) + '...' : this.displayTextValue;
    } else {
      this.isTrimmed = this.displayTextValue ? this.displayTextValue.length > 11 : false;
      this.trimmedText = this.isTrimmed ? this.displayTextValue.substr(0, 7) + '...' : this.displayTextValue;
    }
  }
}
