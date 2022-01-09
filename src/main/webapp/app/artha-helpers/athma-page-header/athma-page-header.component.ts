import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-athma-page-header',
  template: `
    <div class="athma-page-header" [style.height.px]="height" [ngStyle]="{ 'padding-top.px': paddingTop }">
      <ng-content></ng-content>
      <div class="clearfix"></div>
    </div>
  `
})
export class AthmaPageHeaderComponent implements OnInit {
  @Input() height = 56; // 56 for non-tab view and 70 for tab-view
  @Input() paddingTop: number;
  constructor() {}

  ngOnInit() {}
}
