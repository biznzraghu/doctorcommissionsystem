import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'ArthaDateofBirth',
  pure: false
})
export class ArthaDateOfBirthPipe implements PipeTransform {
  transform(val): any {
    const date = val;
    // return Math.abs(val);
    if (date != null) {
      const a = moment(new Date());
      const b = moment(date);
      const years = a.diff(b, 'year');
      b.add(years, 'years');
      const months = a.diff(b, 'months');
      b.add(months, 'months');
      const days = a.diff(b, 'days');
      if (years < 1) {
        if (months !== 0) {
          return (months ? months + 'm' : '') + (days ? ' ' + days + 'd' : '');
        } else {
          return days ? days + 'd' : '';
        }
      } else if (years >= 1 && years < 10) {
        return (years ? years + 'y' : '') + (months ? ' ' + months + 'm' : '');
      } else if (years >= 10) {
        return years ? years + 'y' : '';
      }
    } else {
      return 'NA';
    }
  }
}
