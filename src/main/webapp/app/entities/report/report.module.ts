import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReportComponent } from './report.component';
import { ReportRoute } from './report.route';
import { ArthaHelperModule } from 'app/artha-helpers/artha-helper.module';
// import { PreferencesDataResolver } from './report.route'
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DepartmentComponent } from './components/department/department.component';
import { MisReportModule } from './module/mis-report/mis-report.module';
import { UnitComponent } from './components/unit/unit.component';
import { YearComponent } from './components/year/year.component';
import { EmployeeComponent } from './components/employee/employee.component';
import { NgbPopover } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [ReportComponent, DepartmentComponent, UnitComponent, YearComponent, EmployeeComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(ReportRoute),
    ArthaHelperModule,
    NgSelectModule,
    FormsModule,
    ReactiveFormsModule,
    MisReportModule
  ],
  providers: [
    // PreferencesDataResolver,
    NgbPopover
  ]
})
export class ReportModule {}
