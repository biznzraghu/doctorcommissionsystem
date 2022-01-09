import { Directive, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[arthaSort]'
})
export class ArthaSortDirective {
  ascendingVal: boolean;
  @Input() callback: Function;

  predicateVal: string;
  @Input() set predicate(v) {
    const isSame = this.predicateVal === v;
    this.predicateVal = v;
    if (!isSame) {
      this.resetClasses();
      this.eventManager.broadcast({ name: 'predicateChange', content: false });
    }
  }

  @Input() set ascending(v) {
    const isSame = this.ascendingVal === v;
    this.ascendingVal = v;
    if (!isSame) {
      this.resetClasses();
      this.eventManager.broadcast({ name: 'predicateChange', content: false });
    }
  }

  sortIcon = 'fa-sort';
  sortAscIcon = 'fa-sort-asc';
  sortDescIcon = 'fa-sort-desc';
  sortIconSelector = 'span.fa';

  @Output() predicateChange: EventEmitter<any> = new EventEmitter();
  @Output() ascendingChange: EventEmitter<any> = new EventEmitter();

  element: any;

  constructor(el: ElementRef, private eventManager: JhiEventManager) {
    this.element = el.nativeElement;
  }

  sort(field: any) {
    this.resetClasses();
    if (field !== this.predicateVal) {
      this.ascendingVal = true;
    } else {
      this.ascendingVal = !this.ascendingVal;
    }
    this.predicateVal = field;
    this.predicateChange.emit(field);
    this.ascendingChange.emit(this.ascendingVal);
    this.callback();
  }

  private resetClasses() {
    const allThIcons = this.element.querySelectorAll(this.sortIconSelector);
    for (let i = 0; i < allThIcons.length; i++) {
      allThIcons[i].classList.remove(this.sortAscIcon);
      allThIcons[i].classList.remove(this.sortDescIcon);
      allThIcons[i].classList.add(this.sortIcon);
    }
  }
}
