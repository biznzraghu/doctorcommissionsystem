import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'activeAsStatus' })
export class ActiveAsStatusPipe implements PipeTransform {
  transform(value: boolean): string {
    return value ? 'Active' : 'InActive';
  }
}
