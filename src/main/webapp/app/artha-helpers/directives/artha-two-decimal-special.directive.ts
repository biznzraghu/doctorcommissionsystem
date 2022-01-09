/* eslint-disable */
import { Directive, ElementRef, HostListener } from '@angular/core';
@Directive({
  selector: '[jhiDecimal]'
})
export class ArthaDecimalDirective {
  private regex = new RegExp(/^\d[0-9\%]*\.?\d{0,2}$/g);
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Del', 'Delete', 'Control', 'V', 'v'];
  constructor(private el: ElementRef) {}
  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    // console.log(this.el.nativeElement.value);
    // Allow Backspace, tab, end, and home keys
    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }
    const current: string = this.el.nativeElement.value;
    const position = this.el.nativeElement.selectionStart;
    const next: string = [current.slice(0, position), event.key === 'Decimal' ? '.' : event.key, current.slice(position)].join('');
    if (next && !String(next).match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event']) onPaste(e) {
    const content = e.clipboardData.getData('text/plain');
    if (content && !String(content).match(this.regex)) {
      event.preventDefault();
    }
  }
}
