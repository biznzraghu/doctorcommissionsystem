import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-artha-audit-log-info',
  templateUrl: './audit-log-info.component.html'
})
export class AuditLogInfoComponent {
  @Input() auditEntity: any;
}
