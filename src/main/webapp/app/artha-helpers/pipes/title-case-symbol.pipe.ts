import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'titleCaseSymbol',
  pure: false
})
export class TitleCaseSymbolPipe implements PipeTransform {
  transform(value: string, symbol = ' '): string {
    if (!value) {
      return;
    }
    const words = value.split(symbol);
    for (let i = 0; i < words.length; i++) {
      words[i] = words[i].charAt(0).toUpperCase() + words[i].substr(1).toLowerCase();
    }
    return words.join(' ');
  }
}
