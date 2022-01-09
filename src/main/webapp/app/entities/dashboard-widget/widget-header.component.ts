import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { ErrorParser } from 'app/shared/util/error-parser.service';

import { DashboardService, UpdateWidget } from './../dashboard/dashboard.service';

@Component({
  selector: 'jhi-widget-header',
  template: `
    <header
      class="widget-header d-flex justify-content-between"
      [style.background]="
        widgetData?.customSettings?.headerBG ? widgetData?.customSettings?.headerBG : widgetData?.widgetMaster.settings?.headerBG
      "
      [style.borderColor]="
        widgetData?.customSettings?.headerBG ? widgetData?.customSettings?.headerBG : widgetData?.widgetMaster.settings?.headerBG
      "
      style="border-style:solid;border-width:0px 1px;"
    >
      <h4 class="mb-0">
        <span style="vertical-align: text-top; ">{{ widgetData.widgetMaster.title }}</span>
        <span
          class="icon-info h6 mb-0 athma-pointer information "
          ngbPopover="{{ widgetData?.widgetMaster?.description }}"
          #descPopover="ngbPopover"
          (clickOutside)="descPopover.close()"
          placement="bottom"
        ></span>
      </h4>

      <div>
        <span
          class="icon-admin_filled pull-right h6 mb-0 ml-1 widget-setting-popover athma-pointer"
          [ngbPopover]="widgetSetting"
          #settingPopover="ngbPopover"
          [autoClose]="'outside'"
          placement="bottom-right"
          width="inherit"
        >
          <ng-template #widgetSetting>
            <span>Header color</span
            ><span
              class="header-color pull-right athma-pointer ml-2"
              [style.background]="selectedColor"
              (click)="showPalette($event)"
            ></span>
            <br />
            <div *ngIf="showColorPalette" class="mt-2 p-2" style="background: #f1f0f0;">
              <span>Select header color</span>
              <div class="color-picker">
                <span
                  class="color-palette col-md-3"
                  *ngFor="let color of defaultColors"
                  [style.background]="color"
                  (click)="setHeaderBg($event, color)"
                ></span>
              </div>
            </div>
            <div *ngIf="showColorPalette">
              <span
                *ngIf="showSetBtn"
                class="pull-right athma-pointer pl-1 pr-1 mt-1"
                style="color: #0078d7;border: 1px solid #cacad3; border-radius: 2px;"
                (click)="setColor()"
              >
                Set
              </span>
            </div>
          </ng-template>
        </span>
        <span class="icon-refresh pull-right h6 mb-0 ml-1  athma-pointer" (click)="refresh()"></span>
      </div>
    </header>
  `
})
export class WidgetHeaderComponent implements OnInit {
  @Input() widgetData: any;
  @Output() refreshWidget: EventEmitter<string> = new EventEmitter<string>();
  defaultColors: any;
  selectedColor: any;
  showColorPalette: boolean;
  showSetBtn: boolean;

  constructor(
    private dashboardService: DashboardService,
    private updateWidget: UpdateWidget,
    private httpBlockerService: HttpBlockerService,
    private errorParser: ErrorParser
  ) {
    this.defaultColors = ['#E53935', '#D81B60', '#8E24AA', '#1E88E5', '#43A047', '#E65100', '#795548', '#616161'];
  }
  ngOnInit() {
    this.selectedColor = this.widgetData.customSettings.headerBG
      ? this.widgetData.customSettings.headerBG
      : this.widgetData.widgetMaster.settings.headerBG;
    this.showColorPalette = false;
    this.showSetBtn = false;
  }
  showPalette(event) {
    event.stopPropagation();
    this.showColorPalette = !this.showColorPalette;
  }

  setHeaderBg(event, color) {
    event.stopPropagation();
    this.selectedColor = color;
    this.setColor();
    // this.showSetBtn = true;
  }
  setColor() {
    this.widgetData.customSettings.headerBG = this.selectedColor;
    this.showSetBtn = false;
    this.showColorPalette = false;
    this.dashboardService.saveWidgets(this.widgetData).subscribe(
      (res: HttpResponse<any>) => {
        this.updateWidget.setChengedWidget(res);
      },
      (res: HttpErrorResponse) => {
        // this.httpBlockerService.enableHttpBlocker(false);
        this.onError(res.error);
      }
    );
  }
  refresh() {
    this.refreshWidget.emit();
  }

  private onError(errorObj): any {
    this.errorParser.parse(errorObj);
  }
}
