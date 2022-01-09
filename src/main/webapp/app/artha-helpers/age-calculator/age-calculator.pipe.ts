import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';
@Pipe({ name: 'ageFromDOB' })
export class AgeFromDateOfBirthPipe implements PipeTransform {
  // transform(birthdate: string): string {
  //     let birthday = new Date(birthdate);
  //     let currentDate = new Date();
  //     let ageArray = [0, 0, 0];
  //     let ageUnitArray = ['Y', 'M', 'D'];
  //     ageArray[0] = this.calculateAge(birthday);
  //     ageArray[1] = this.monthDiff(birthday, currentDate);
  //     if (ageArray[0] == 0 || ageArray[1] == 0)
  //         ageArray[2] = this.daysDiff(birthday, currentDate);

  //     let age = "";
  //     for (let i = 0; i < ageArray.length; i++) {
  //         age += (age.length > 0 ? ' ' : '') + this.createAgeString(ageArray[i], ageUnitArray[i]);
  //     }
  //     return age;
  // }

  // private calculateAge(birthday) { // birthday is a date
  //     let ageDifMs = Date.now() - birthday.getTime();
  //     let ageDate = new Date(ageDifMs); // miliseconds from epoch
  //     return Math.abs(ageDate.getUTCFullYear() - 1970);
  // }
  // private monthDiff(d1, d2) {
  //     if (d1 < d2) {
  //         let months = d2.getMonth() - d1.getMonth();
  //         return months <= 0 ? 0 : months;
  //     }
  //     return 0;
  // }
  // private daysDiff(d1, d2) {
  //     if (d1 < d2) {
  //         let days = d2.getDate() - d1.getDate();
  //         return days <= 0 ? 0 : days;
  //     }
  //     return 0;
  // }
  // private createAgeString(age, ageUnit) {
  //     return age == 0 ? "" : age + ageUnit;
  // }
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
