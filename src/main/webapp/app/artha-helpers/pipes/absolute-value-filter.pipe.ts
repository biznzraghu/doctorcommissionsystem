import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'absVal',
  pure: false
})
export class AbsoluteValueFilterPipe implements PipeTransform {
  transform(val): any {
    return Math.abs(val);
  }
}
