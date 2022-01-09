import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nameTrimFilter',
  pure: false
})
export class NameTrimFilterPipe implements PipeTransform {
  transform(items: string): any {
    if (!items) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    return items.replace(/^\s+|\s+$/g, '');
  }
}
