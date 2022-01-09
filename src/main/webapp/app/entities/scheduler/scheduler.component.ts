import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { Principal } from 'app/shared/auth/principal.service';
import { JhiAlertService } from 'ng-jhipster';
import { Scheduler } from './scheduler.model';
import { SchedulerService } from './scheduler.service';

@Pipe({
  name: 'schedulerfilter',
  pure: false
})
export class SchedulerFilterPipe implements PipeTransform {
  transform(items: Scheduler[], filter: string): Scheduler[] {
    if (!items || !filter) {
      return items;
    }
    return items.filter((item: Scheduler) => item.scheduler.toLocaleLowerCase().includes(filter.toLocaleLowerCase()));
  }
}

@Component({
  selector: 'jhi-scheduler',
  templateUrl: './scheduler.component.html'
})
export class SchedulerComponent implements OnInit {
  options: ListHeaderOptions;
  schedulers: Scheduler[];
  currentAccount: any;
  datePipe: DatePipe = new DatePipe('en-US');
  searchQuery: string;

  constructor(private schedulerService: SchedulerService, private alertService: JhiAlertService, private principal: Principal) {
    this.schedulers = [];
  }

  loadAll() {
    this.schedulerService.query().subscribe(
      (res: any) => {
        const data = res.body;
        for (let i = 0; i < data.length; i++) {
          const scheduler = data[i];
          scheduler.nextFireDateTime = this.datePipe.transform(new Date(scheduler.nextFireTime), 'y/MM/dd HH:mm:ss');
          this.schedulers.push(scheduler);
        }
      },
      (res: HttpErrorResponse) => this.onError(res.error)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.options = new ListHeaderOptions('scheduler', false, false, false, false, false, null);
  }

  private onError(error) {
    this.alertService.error(
      error.message ? error.message : 'global.messages.response-msg',
      error.description ? { msg: error.description } : null,
      null
    );
  }

  trigger(scheduler: Scheduler) {
    scheduler.nextFireTime = new Date().getTime();
    this.schedulerService.update(scheduler).subscribe(
      (res: Scheduler) => {
        scheduler = res;
        this.alertService.success('gatewayApp.scheduler.triggered', { scheduler: scheduler.scheduler });
      },
      (res: HttpErrorResponse) => this.onSaveError(res.error)
    );
  }

  save(scheduler: Scheduler) {
    const dateTimeArray = scheduler.nextFireDateTime.split(' ');
    const dateArray = dateTimeArray[0].split('/');
    const timeArray = dateTimeArray[1].split(':');
    scheduler.nextFireTime = new Date(
      dateArray[0],
      parseInt(dateArray[1], 10) - 1,
      dateArray[2],
      timeArray[0],
      timeArray[1],
      timeArray[2],
      0
    ).getTime();
    this.schedulerService.update(scheduler).subscribe(
      (res: Scheduler) => {
        scheduler = res;
        this.alertService.success('gatewayApp.scheduler.saved', { scheduler: scheduler.scheduler });
      },
      (res: HttpErrorResponse) => this.onSaveError(res.error)
    );
  }

  private onSaveError(error) {
    this.onError(error);
  }
}
