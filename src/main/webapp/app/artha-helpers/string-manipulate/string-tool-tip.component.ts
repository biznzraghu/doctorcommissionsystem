import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-athma-string-tool-tip',
  template: `
    <span *ngIf="placementValue === 'right'" container="body" [ngbTooltip]="displayTextValue" placement="right">{{
      displayTextValue
    }}</span>
    <span *ngIf="placementValue === 'bottom'" container="body" [ngbTooltip]="displayTextValue" placement="bottom">{{
      displayTextValue
    }}</span>
    <span *ngIf="placementValue === 'left'" container="body" [ngbTooltip]="displayTextValue" placement="left">{{ displayTextValue }}</span>
    <span *ngIf="placementValue === 'top'" container="body" [ngbTooltip]="displayTextValue" placement="top">{{ displayTextValue }}</span>
  `
})
export class AthmaStringToolTipComponent implements OnInit {
  displayTextValue: string;
  @Input() placementValue = 'bottom';
  constructor() {}

  ngOnInit(): void {}

  @Input()
  get displayString() {
    return this.displayTextValue;
  }

  set displayString(val) {
    this.displayTextValue = val;
  }
}
