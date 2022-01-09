import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DestroyHelperService {
  constructor() {}

  public unsubscribe(subscriber: Subscription[]) {
    subscriber.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    });
  }
}
