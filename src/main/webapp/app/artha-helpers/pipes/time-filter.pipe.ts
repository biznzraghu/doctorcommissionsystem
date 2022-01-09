import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'timeFilter',
  pure: false
})
export class TimeBlockFilterPipe implements PipeTransform {
  private blockTiming = {
    morning: {
      start: '06:00:00',
      end: '12:00:00'
    },
    afternoon: {
      start: '12:00:01',
      end: '18:00:00'
    },
    evening: {
      start: '18:00:01',
      end: '23:59:59'
    }
  };

  transform(items: any, filter: any): any {
    if (!items || !filter) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    return items.filter(item => {
      return this.applyFilter(item, filter);
    });
  }

  applyFilter(item: any, filter: string) {
    const selectedDate = moment(item.start).format('YYYY-MM-DD');
    const startTime = selectedDate + 'T' + this.blockTiming[filter].start;
    const endTime = selectedDate + 'T' + this.blockTiming[filter].end;
    const isBefore = moment(item.start).isBefore(startTime);
    const isAfter = moment(item.start).isAfter(endTime);
    return !isBefore && !isAfter;
  }
}
