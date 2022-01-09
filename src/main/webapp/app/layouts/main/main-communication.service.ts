import { Injectable, TemplateRef } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable()
export class MainCommunicationService {
  // Observable string sources
  private headerViewSource = new Subject<TemplateRef<any>>();

  private moduleMenuSource = new Subject<string>();

  private naviageToTopMenuSource = new Subject<string>();

  private isIdealModalSource = new BehaviorSubject(false);

  // Observable string streams
  headerView$ = this.headerViewSource.asObservable();
  moduleMenu$ = this.moduleMenuSource.asObservable();
  naviageToTopMenu$ = this.naviageToTopMenuSource.asObservable();
  idealModal$ = this.isIdealModalSource.asObservable();

  // Service message commands
  updateHeaderView(template: TemplateRef<any>) {
    this.headerViewSource.next(template);
  }

  updateSidebarMenu(module: string) {
    this.moduleMenuSource.next(module);
  }

  navigateToTopMenu(module: string) {
    this.naviageToTopMenuSource.next(module);
  }

  idealModal(value: boolean) {
    this.isIdealModalSource.next(value);
  }
}
