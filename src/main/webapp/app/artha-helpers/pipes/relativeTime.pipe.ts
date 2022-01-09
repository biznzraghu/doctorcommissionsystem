import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'relativeTime'
})
export class RelativeTimeFilterPipe implements PipeTransform {
  transform(inputDate: string, updatedDate: string, status: any): string {
    const current = new Date().valueOf();
    const input = new Date(inputDate).valueOf();
    const elapsed = current - input;

    if (updatedDate && (status === 'DISCHARGED' || status === 'ADMITTED_TO_IP')) {
      const uDate = new Date(updatedDate).valueOf();
      const elapsedTime = uDate - input;
      return this.convertMS(elapsedTime);
    }
    return this.convertMS(elapsed);
  }

  private convertMS(milliseconds: number): string {
    let day: string | number, hour, minute, seconds;
    seconds = Math.floor(milliseconds / 1000);
    minute = Math.floor(seconds / 60);
    seconds = seconds % 60;
    hour = Math.floor(minute / 60);
    minute = minute % 60;
    // eslint-disable-next-line prefer-const
    day = Math.floor(hour / 24);
    hour = hour % 24;
    return (day > 0 ? day + ' days, ' : '') + (hour < 10 ? '0' + hour : hour) + ':' + (minute < 10 ? '0' + minute : minute) + ' hrs';
  }
}
