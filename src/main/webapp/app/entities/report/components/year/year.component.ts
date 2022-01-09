import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';
import { ReportService } from '../../report.service';

@Component({
  selector: 'jhi-year',
  templateUrl: './year.component.html',
  styleUrls: []
})
export class YearComponent implements OnInit {
  count = 10;
  years = [];
  date = new Date();
  currentYear;
  selectedYear;
  min = 0;
  mcount = 12;
  month = [];
  currentDate = new Date();
  currentMonth;
  months = ['Jan', 'Feb', 'March', 'April', 'May', 'June', 'July', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  private refreshReport$: Subscription;
  @Output() selectedDates = new EventEmitter();

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    this.currentYear = this.date.getFullYear();
    this.loopYear();
    this.loopMonth();
    this.refreshReport$ = this.reportService.refreshSubjectAction$.subscribe(res => {
      this.selectedYear = this.currentMonth = null;
      this.select();
    });
  }

  loopYear() {
    for (let i = 0; i <= this.count; i++) {
      this.years.push({ id: this.currentYear - i, name: this.currentYear - i });
    }
  }

  loopMonth() {
    for (let i = 1; i <= this.mcount; i++) {
      this.month.push({ id: this.min + i, name: this.months[this.min + (i - 1)] });
    }
    this.selectDate();
  }

  selectDate() {
    this.currentMonth = this.month[this.currentDate.getMonth() - 1];
    if (this.currentMonth.id === 0) {
      this.currentYear = this.currentYear - 1;
    }
    this.selectedYear = this.years.find(yrs => {
      return yrs.id === this.currentYear;
    });
    this.select();
  }

  select() {
    this.selectedDates.emit({
      year: this.selectedYear ? this.selectedYear.id : null,
      month: this.currentMonth ? this.currentMonth.id : null
    });
  }
}
