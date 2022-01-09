import { Pipe, PipeTransform } from '@angular/core';
// import { Item } from '../../entities/item/';
@Pipe({
  name: 'lineItemNameFilter',
  pure: true
})
export class LineItemNameFilterPipe implements PipeTransform {
  transform(items: any[], filter: any): any {
    if (!items || !filter) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    return (items || [])
      .filter(
        f =>
          f.item.name.toLowerCase().indexOf(filter.name.toLowerCase()) !== -1 ||
          (f.item.dispensableGenericName && f.item.dispensableGenericName.toLowerCase().indexOf(filter.name.toLowerCase()) !== -1) ||
          f.item.code.toLowerCase().indexOf(filter.code.toLowerCase()) !== -1
      )
      .splice(0);
  }
}
