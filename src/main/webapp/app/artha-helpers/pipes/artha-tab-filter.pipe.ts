import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'arthaTabFilter',
  pure: false
})
export class ArthaTabFilterPipe implements PipeTransform {
  transform(items: any[], key: string): any {
    if (!items || !key) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    return items.filter(f => f[key] === true);
  }
}
