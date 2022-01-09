import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
// import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HttpBlockerService {
  private enableBlocker = new Subject<boolean>();
  enableHttpBlocker$ = this.enableBlocker.asObservable();

  enableHttpBlocker(isEnable: boolean) {
    this.enableBlocker.next(isEnable);
  }
}
