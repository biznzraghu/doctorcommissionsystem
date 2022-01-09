import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'urlToTitle'
})
export class UrlToTitlePipe implements PipeTransform {
  transform(value: any) {
    if (value) {
      value = value.replace('-', ' ');
    }
    return value;
  }
}
