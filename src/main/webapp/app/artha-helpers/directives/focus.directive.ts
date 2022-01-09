import { Directive, Input, ElementRef, OnChanges } from '@angular/core';
@Directive({
  selector: '[jhiFocus]'
})
export class FocusDirective implements OnChanges {
  @Input()
  focus: boolean;
  constructor(private element: ElementRef) {}
  public ngOnChanges() {
    this.element.nativeElement.select();
  }
}
