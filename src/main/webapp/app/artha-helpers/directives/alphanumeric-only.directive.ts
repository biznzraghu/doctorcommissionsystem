/* eslint-disable @typescript-eslint/prefer-regexp-exec */
import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiAlphanumeric]'
})
export class AlphaNumericOnlyDirective {
  private regex = new RegExp(/^[a-zA-Z0-9]+$/);
  // Allow key codes for special events. Reflect :
  // Backspace, tab, end, home
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', '-'];

  constructor(private el: ElementRef) {}

  @HostListener('keydown', ['$event']) onKeyDown(event: KeyboardEvent) {
    // Allow Backspace, tab, end, and home keys
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
    const content = e.clipboardData.getData('text/plain');
    if (content && !String(content).match(this.regex)) {
      event.preventDefault();
    }
  }
}
