import { Pipe } from '@angular/core';

@Pipe({
  name: 'keyValueFilter'
})
export class KeyValueFilterPipe {
  transform(value: any, args: any[] = null): any {
    return Object.keys(value).map(function(key) {
      const pair = {};
      const k = 'key';
      const v = 'value';

      pair[k] = key;
      pair[v] = value[key];

      return pair;
    });
  }
}

// Example: Usage:-
// ---------------------------
// <li *ngFor="#u of myObject |
// keyValueFilter">First Name: {{u.key}} <br> Last Name: {{u.value}}</li>
