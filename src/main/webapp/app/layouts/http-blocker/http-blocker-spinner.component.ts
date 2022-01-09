import { Component, OnInit } from '@angular/core';
import { HttpBlockerService } from './http-blocker-spinner.service';

@Component({
  selector: 'jhi-http-blocker-spinner',
  templateUrl: './http-blocker-spinner.component.html'
})
export class HttpBlockerSpinnerComponent implements OnInit {
  public displayBlocker: boolean;
  constructor(private httpBlockerService: HttpBlockerService) {
    this.displayBlocker = false;
  }
  ngOnInit() {
    this.httpBlockerService.enableHttpBlocker$.subscribe((isEnable: boolean) => {
      this.displayBlocker = isEnable;
    });
  }
}
