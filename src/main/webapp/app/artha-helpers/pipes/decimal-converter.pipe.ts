import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'decimalConvertor'
})
export class DecimalConvertorPipe implements PipeTransform {
  transform(value: any, numberOfDecimalPoints: any): any {
    const decimalPoints = parseInt(numberOfDecimalPoints, 0);
    const floatValue = parseFloat(value);
    return parseFloat(floatValue.toFixed(decimalPoints));
  }
}
