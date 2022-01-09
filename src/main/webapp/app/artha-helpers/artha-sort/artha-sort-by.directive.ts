import { Directive, Input, Host, ElementRef, HostListener, AfterViewInit, OnDestroy, Renderer2 } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { ArthaSortDirective } from './artha-sort.directive';
import { Subscription } from 'rxjs';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[arthaSortBy]'
})
export class ArthaSortByDirective implements AfterViewInit, OnDestroy {
  @Input() arthaSortBy: string;

  sortAscIcon = 'fa-sort-asc';
  sortDescIcon = 'fa-sort-desc';

  athmaSort: ArthaSortDirective;
  subscription: Subscription;

  constructor(
    @Host() athmaSort: ArthaSortDirective,
    private el: ElementRef,
    private renderer: Renderer2,
    private eventManager: JhiEventManager
  ) {
    this.athmaSort = athmaSort;
    this.subscription = this.eventManager.subscribe('predicateChange', () => {
      if (!this.athmaSort.predicateVal) {
        this.athmaSort.predicateVal = 'id';
      }
      if (this.athmaSort.predicateVal && this.athmaSort.predicateVal !== '_score' && this.athmaSort.predicateVal === this.arthaSortBy) {
        this.applyClass();
      }
    });
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.subscription);
  }

  ngAfterViewInit(): void {
    if (this.athmaSort.predicateVal && this.athmaSort.predicateVal !== '_score' && this.athmaSort.predicateVal === this.arthaSortBy) {
      this.applyClass();
    }
  }

  @HostListener('click') onClick() {
    if (this.athmaSort.predicateVal && this.athmaSort.predicateVal !== '_score') {
      this.athmaSort.sort(this.arthaSortBy);
      this.applyClass();
    }
  }

  private applyClass() {
    const childSpan = this.el.nativeElement.children[1];
    let add = this.sortAscIcon;
    if (!this.athmaSort.ascendingVal) {
      add = this.sortDescIcon;
    }
    this.renderer.addClass(childSpan, add);
  }
}
