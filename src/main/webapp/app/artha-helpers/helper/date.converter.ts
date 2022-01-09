import { Injectable } from '@angular/core';

import * as moment from 'moment';

export class BootstrapDate {
  constructor(public year?: number, public month?: number, public day?: number) {}
}

@Injectable()
export class AthmaDateConverter {
  constructor() {}

  toISOString(date: BootstrapDate) {
    let d = new Date(date.year, date.month - 1, date.day);
    d = new Date(d.getTime() - d.getTimezoneOffset() * 60000);
    return d.toISOString();
  }

  toDate(date: BootstrapDate) {
    return new Date(date.year, date.month - 1, date.day);
  }

  toDateTime(date: BootstrapDate) {
    return new Date(date.year, date.month - 1, date.day);
  }

  toDateString(date: BootstrapDate) {
    return moment(new Date(date.year, date.month - 1, date.day)).format('YYYY-MM-DDTHH:mm:ss');
  }

  toDateStringFromBootstrap(date: BootstrapDate) {
    return moment(new Date(date.year, date.month - 1, date.day)).format('YYYY-MM-DD');
  }

  toDateTimeString(date: any) {
    return moment(date).format('YYYY-MM-DDTHH:mm:ss');
  }

  toDateTimeStringForFormat(date: any, format: string) {
    return moment(date).format(format);
  }

  toDateTimeStringFromBootstrap(date: BootstrapDate) {
    const time = new Date();
    return moment(new Date(date.year, date.month - 1, date.day, time.getHours(), time.getMinutes(), time.getSeconds())).format(
      'YYYY-MM-DDTHH:mm:ss'
    );
  }

  toDateWithTimezoneString(date: BootstrapDate) {
    return moment(new Date(date.year, date.month - 1, date.day)).format();
  }

  toBootstrapDate(date: Date) {
    return { year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() };
  }

  currentDateForBootstrap() {
    return this.toBootstrapDate(new Date());
  }

  toDateStringFromJsDate(date: Date) {
    return moment(date).format('YYYY-MM-DDTHH:mm:ss');
  }

  normalDateFormat(date: Date) {
    return moment(date).format('YYYY-MM-DD');
  }

  normalDateTimeFormat(date: Date) {
    return moment(date).format('DD-MM-YYYY HH:mm');
  }

  customCommentsTime(date: Date) {
    if (moment(date).format('DD/MM/YYYY') === moment(new Date()).format('DD/MM/YYYY')) {
      return moment(date).fromNow() + ' - ' + moment(date).format('hh:mm:ss');
    } else {
      return moment(date).format('MMMM DD hh:mm:ss');
    }
  }

  notificationsTime(date: Date) {
    return moment(date).fromNow();
  }

  isToday(date: string | Date): boolean {
    return moment(date).isSame(moment(), 'day');
  }

  addDates(date: string | Date, durations = {}) {
    durations = moment.duration(durations);
    return moment(date).add(durations);
  }
}
