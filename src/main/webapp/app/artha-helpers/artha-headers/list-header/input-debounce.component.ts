import { Component, Input, Output, EventEmitter, ElementRef, ViewChild, OnInit } from '@angular/core';
import { fromEvent } from 'rxjs';
import { map, debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'jhi-input-debounce',
  template: `
    <input id="inputDebounce" #inputDebounce type="text" [class]="className" [placeholder]="placeholder" [(ngModel)]="inputValue" />
    <!-- (ngModelChange)="change($event)" -->
  `
})
export class InputDebounceComponent implements OnInit {
  @Input() placeholder: string;
  @Input() delay = 300;
  @Input() className: string;
  @Output() value: EventEmitter<string> = new EventEmitter<string>();

  @ViewChild('inputDebounce', { static: true }) inputDebounce: ElementRef;

  public inputValue: string;

  // constructor(private elementRef: ElementRef) {
  //     const eventStream = Observable.fromEvent(elementRef.nativeElement, 'keyup')
  //         .map(() => this.inputValue)
  //         .debounceTime(500)
  //         .distinctUntilChanged();

  //     eventStream.subscribe(input => this.value.emit(input));
  // }
  ngOnInit() {
    this.onKeyUpEvent();
  }

  onKeyUpEvent() {
    fromEvent(this.inputDebounce.nativeElement, 'keyup')
      .pipe(
        map((event: any) => {
          return event.target.value;
        }),
        debounceTime(500),
        distinctUntilChanged()
      )
      .subscribe((text: string) => {
        this.value.emit(text);
      });
  }
  change(newValue) {
    this.inputValue = newValue;
    // this.value.emit(newValue);
  }

  @Input() set valueChange(val) {
    this.inputValue = val;
  }
}
