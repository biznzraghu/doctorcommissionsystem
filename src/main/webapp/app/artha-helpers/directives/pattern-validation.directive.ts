/* eslint-disable @typescript-eslint/prefer-regexp-exec */
import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[jhiValidatePattern]'
})
export class PatternValidatorDirective {
  @Input() regex: RegExp;
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home'];

  constructor(private el: ElementRef) {}

  @HostListener('keypress', ['$event']) onKeyDown(event) {
    if (this.specialKeys.includes(event.key)) {
      return;
    }
    const current: string = this.el.nativeElement.value;
    const next: string = current.concat(event.key);
    if (next && !String(next).match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event']) onPaste(e) {
    const pastedData = e.clipboardData.getData('text/plain');
    const content = pastedData + e.target.value;
    if (content && !String(content).match(this.regex)) {
      event.preventDefault();
    }
  }
}
