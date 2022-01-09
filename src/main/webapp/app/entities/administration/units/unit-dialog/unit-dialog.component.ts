import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { VariablePayoutService } from 'app/entities/variable-payout/variable-payout.service';
import { JhiAlertService } from 'ng-jhipster';
import { DepartmentService } from '../../administration-services/department.service';
import { UnitService } from '../../administration-services/unit.service';

@Component({
  selector: 'jhi-unit-dialog',
  templateUrl: './unit-dialog.component.html'
})
export class UnitDialogComponent implements OnInit {
  options: ListHeaderOptions;
  unitDetail: any = {};
  isUnitCreation = false;
  departmentParams: any = {};
  title = '';
  defaultQuery = '';
  pageName: string;
  tablist: any;
  selectedTab = 0;
  searchText = '';
  organizationUnits: any[] = [];
  departments: any[] = [];
  selectedUnit: any;
  currentSearch = '';
  predicate = 'createdDate';
  reverse = false;
  organizationId: any;
  organization: any;
  isSaving = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private deptService: DepartmentService,
    private jhiAlertService: JhiAlertService,
    private unitService: UnitService,
    private variablePayoutService: VariablePayoutService
  ) {
    this.route.params.subscribe((params: any) => {
      if (params['id']) {
        this.organizationId = params.id;
      }

      this.isUnitCreation = params['department'] === 'y' ? false : true;
      if (!this.isUnitCreation) {
        this.departmentParams = { department: 'y', departmentId: params['departmentId'] };
        if (params['departmentId']) {
          this.loadOrganization(params['departmentId']);
        }
        if (params['organizationId']) {
          this.organizationId = params['organizationId'];
        }

        this.selectedTab = 1;
        this.loadDepartment();
        this.getUnitDetail(this.organizationId);
      } else {
        this.unitService.query().subscribe(
          (res: HttpResponse<any>) => {
            this.organizationUnits = res.body;
          },
          (res: HttpErrorResponse) => this.onError(res.error)
        );
        this.getUnitDetail(this.organizationId);
      }
    });

    this.route.data.subscribe(data => {
      if (data['unit']) {
        this.unitDetail = data['unit'];
        this.title = `${this.unitDetail.name}`;
        this.defaultQuery = `organization.code:${this.unitDetail.code}`;
      }
    });
  }

  private getUnitDetail(id) {
    this.unitService.find(id).subscribe((res: HttpResponse<any>) => {
      this.organization = res.body;
      this.title = this.organization.name;
      this.selectedUnit = this.organization;
    });
  }

  ngOnInit() {
    this.tablist = [
      { label: 'Unit Details', value: 'Unit Details' },
      { label: 'Departments', value: 'Departments' }
    ];
    this.pageName = 'unit';

    if (this.organizationId) {
      this.options = new ListHeaderOptions(
        'artha/administrator/unit',
        false,
        true,
        false,
        true,
        false,
        false,
        null,
        true,
        this.tablist,
        this.selectedTab,
        true
      );
    } else {
      this.title = 'Unit';
      this.options = new ListHeaderOptions('artha/administrator/unit', false, true, false, true, false, false, null, false);
    }
  }

  private loadDepartment() {
    this.variablePayoutService
      .getDepartment({
        query: this.currentSearch === '' ? this.defaultQuery : this.currentSearch + `AND  organization.code:${this.unitDetail.code}`,
        page: 0,
        size: 9999,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.departments = res.body;
        },
        (error: any) => {
          this.onError(error.json);
        }
      );
  }

  private sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  private loadOrganization(id) {
    this.deptService.find(id).subscribe(
      (res: HttpResponse<any>) => {
        this.unitDetail = res.body;
        this.title = this.unitDetail.organization.name;
      },
      error => {
        this.onError(error.json);
      }
    );
  }

  private onError(error) {
    this.jhiAlertService.error(
      error && error.message ? error.message : 'global.messages.response-msg',
      error && error.description ? { msg: error.description } : null,
      null
    );
  }

  public assignOrganizationCode(event): any {
    this.unitDetail = event;
    const sourceList = this.isUnitCreation ? this.organizationUnits : this.departments;
    for (let i = 0; i < sourceList.length; i++) {
      if (this.unitDetail.name === sourceList[i].name) {
        this.unitDetail = sourceList[i];
        this.title = this.unitDetail.name;
        break;
      }
    }
  }

  public save() {
    this.isSaving = true;
    if (this.departmentParams && this.departmentParams.department === 'y') {
      this.saveDepartment();
    } else {
      this.saveUnit();
    }
  }

  private saveDepartment() {
    if (this.unitDetail.id) {
      this.deptService.update(this.unitDetail).subscribe(
        () => {
          this.departmentParams = { department: 'y', departmentId: this.unitDetail.id };
          this.router.navigate(['artha/administrator/unit-dept', this.organizationId, this.departmentParams]);
        },
        error => {
          this.onError(error.json);
        }
      );
    } else {
      this.unitDetail.organization = this.organization;
      this.deptService.create(this.unitDetail).subscribe(
        (res: any) => {
          this.departmentParams = { department: 'y', departmentId: res.id };
          this.router.navigate(['artha/administrator/unit-dept', this.organizationId, this.departmentParams]);
        },
        error => {
          this.onError(error.json);
        }
      );
    }
  }

  private saveUnit() {
    if (this.unitDetail.id) {
      this.unitService.update(this.unitDetail).subscribe(
        () => {
          this.jhiAlertService.success('arthaApp.unitValidation.updated');
          this.router.navigate([`artha/administrator/unit/${this.organizationId}/detail`]); // detail page
        },
        error => {
          this.onError(error.json);
        }
      );
    } else {
      this.unitService.create(this.unitDetail).subscribe(
        (res: any) => {
          this.jhiAlertService.success('arthaApp.unitValidation.created');
          this.router.navigate([`artha/administrator/unit/${res.id}/detail`]); // detail page
        },
        error => {
          this.onError(error.json);
        }
      );
    }
  }

  public onTabChange(tabIndex) {
    this.selectedTab = tabIndex;
    this.router.navigate([`artha/administrator/unit/${this.organizationId}/detail`]); // detail page
  }

  public clear() {
    this.router.navigate([`artha/administrator/unit/${this.organizationId}/detail`]);
  }

  public backToList() {
    this.router.navigate([`artha/administrator/units`]);
  }
}
