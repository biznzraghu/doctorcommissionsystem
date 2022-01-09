import { PipeTransform, Pipe } from '@angular/core';

@Pipe({
  name: 'truncatenumber'
})
export class TruncateNumberPipe implements PipeTransform {
  transform(number) {
    if (!number) {
      number = 0;
    }
    const s = number.toString().split('.');
    if (s.length < 2) {
      return s[0] + '.00';
    }
    return s[0] + '.' + (s[1].length === 1 ? s[1] + '0' : s[1].substring(0, 2));
  }

  transformDecimals(number, noOfDecimals) {
    if (!number) {
      number = 0;
    }
    if (!noOfDecimals) {
      noOfDecimals = 0;
    }
    const s = number.toString().split('.');
    if (s.length < 2) {
      return number;
    }
    return s[0] + '.' + s[1].substring(0, noOfDecimals);
  }
}
