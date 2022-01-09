import { Directive } from '@angular/core';

@Directive({
  selector: 'input[jhiTrimmed]',
  // tslint:disable-next-line:no-host-metadata-property
  host: {
    '(input)': 'onChange($event)',
    '(blur)': 'onBlur($event)',
    '[value]': 'value'
  }
})
export class InputTrimmedDirective {
  value: string;
  constructor() {
    this.value = '';
  }

  onInit() {}

  onChange($event) {
    this.value = $event.target.value.trim();
  }

  onBlur($event) {
    this.value = $event.target.value.trim();
    // console.log("blur after",($event.target.value).trim());
    // console.log("blur after replace",($event.target.value).replace(/^\s+|\s+$/g,''));
  }
}
