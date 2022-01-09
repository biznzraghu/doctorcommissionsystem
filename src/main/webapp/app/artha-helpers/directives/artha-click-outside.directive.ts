import { Directive, ElementRef, Output, EventEmitter, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiArthaClickOutside]'
})
export class ArthaClickOutsideDirective {
  @Output() clickOutsideTargetElement: EventEmitter<any> = new EventEmitter();

  constructor(private _elementRef: ElementRef) {}

  @HostListener('document:click', ['$event.target'])
  public onClick(targetElement) {
    const isClickedInside = this._elementRef.nativeElement.contains(targetElement);
    if (!isClickedInside) {
      //  console.log('CLICK OUTSIDE');
      this.clickOutsideTargetElement.emit(targetElement);
    }
  }
}
