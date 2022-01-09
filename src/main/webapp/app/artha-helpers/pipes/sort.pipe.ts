import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sort'
})
export class SortPipe implements PipeTransform {
  transform(value: any[], args?: string): any {
    if (!Array.isArray(value)) {
      return;
    }
    return value.sort((paramFirst, paramSecond) => {
      if (paramFirst[args] < paramSecond[args]) return -1;
      if (paramSecond[args] > paramFirst[args]) return 1;
      return 0;
    });
  }
}
