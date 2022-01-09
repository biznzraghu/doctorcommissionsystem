import { Directive } from '@angular/core';
import { MainCommunicationService } from 'app/layouts/main/main-communication.service';

@Directive({
  selector: '[jhiDisableSidebar]'
})
export class DisableSidebarDirective {
  constructor(private mainCommunicationService: MainCommunicationService) {
    this.mainCommunicationService.updateHeaderView(null);
    this.mainCommunicationService.updateSidebarMenu(null);
  }
}
