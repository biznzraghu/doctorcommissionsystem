import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'renderingHscMappingCategoryFilter',
  pure: true
})
export class RenderingHscMappingCategoryFilterPipe implements PipeTransform {
  transform(items: any[], filter: any): any {
    if (!items || !filter || !filter.value) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    let fArray = [];
    switch (filter.field) {
      case 'sourceHSC':
        {
          fArray = [];
          if (filter.value.trim().length === 0) {
            fArray = items;
          } else {
            fArray = items.filter(
              f =>
                f.sourceHSC.serviceCategory.code.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1 ||
                f.sourceHSC.serviceCategory.display.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1
            );
          }
        }
        break;
      case 'destinationHSC':
        {
          fArray = [];
          if (filter.value.trim().length === 0) {
            fArray = items;
          } else {
            fArray = items.filter(
              f =>
                f.destinationHSC.serviceCategory.code.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1 ||
                f.destinationHSC.serviceCategory.display.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1
            );
          }
        }
        break;
      default:
        {
          fArray = items.filter(
            f =>
              f.sourceHSC.serviceCategory.code.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1 ||
              f.sourceHSC.serviceCategory.display.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1 ||
              f.destinationHSC.serviceCategory.code.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1 ||
              f.destinationHSC.serviceCategory.display.toLowerCase().indexOf(filter.value.toLowerCase()) !== -1
          );
        }
        break;
    }
    return fArray;
  }
}
