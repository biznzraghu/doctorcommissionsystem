import { Directive, EventEmitter, Input, Output, OnInit } from '@angular/core';

@Directive({ selector: '[jhiIsLast]' })
export class IsLastDirective implements OnInit {
  @Input() isLast: boolean;
  @Output() lastDone: EventEmitter<boolean> = new EventEmitter<boolean>();
  ngOnInit() {
    if (this.isLast) {
      this.lastDone.emit(true);
    }
  }
}
