/* eslint-disable no-useless-escape */
import { Directive, ElementRef, HostListener, Input, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[jhiNumeric]'
})
export class NumberOnlyDirective implements AfterViewInit {
  @Input() integerOnly = false;
  @Input() isPercent = false;
  codesList = [48, 49, 50, 51, 52, 53, 54, 55, 56, 57];
  constructor(private el: ElementRef) {}

  @HostListener('keypress', ['$event']) onKeyDown(e) {
    const k = e.which || e.keyCode;
    if (!this.integerOnly) {
      this.codesList.push(46);
    }
    if (this.isPercent) {
      this.codesList.push(37);
    }
    if (!this.codesList.includes(k)) {
      e.preventDefault();
    }
    const value = e.target.value;
    if (!this.integerOnly && k === 46 && value.indexOf('.') !== -1) {
      event.preventDefault();
    }
    if (this.isPercent && k === 37 && value.indexOf('%') !== -1) {
      event.preventDefault();
    }
  }
  @HostListener('paste', ['$event']) onPaste(e) {
    const content = e.clipboardData.getData('text/plain');
    if (this.integerOnly) {
      if (/^\+?\d+$/.test(content) === false) e.preventDefault();
    } else {
      if (/^[+]?([0-9]+(?:[\.][0-9]*)?|\.[0-9]+)$/.test(content) === false) e.preventDefault();
    }
  }
  @HostListener('drop', ['$event']) onDrop(e) {
    e.preventDefault();
  }
  ngAfterViewInit() {
    // console.log('inited');
    // this.el.nativeElement.querySelector('input').addEventListener('paste',()=> {
    //     console.log('pasted')
    // });
  }
}
