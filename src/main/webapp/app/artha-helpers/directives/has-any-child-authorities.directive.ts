import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from '../../shared/auth/principal.service';

@Directive({
  selector: '[jhiHasAnyChildAuthorities]'
})
export class HasAnyChildAuthoritiesDirective {
  private authorities: string[];

  constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

  @Input()
  set hasAnyChildAuthorities(value: string | string[]) {
    this.authorities = typeof value === 'string' ? [...value] : value; // : <string[]>value;
    this.authorities.push('000');
    this.updateView();
    // Get notified each time authentication state changes.
    this.principal.getAuthenticationState().subscribe(() => this.updateView());
  }

  private updateView(): void {
    this.principal.hasAnyChildAuthority(this.authorities).then(result => {
      this.viewContainerRef.clear();
      if (result) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      }
    });
  }
}
