import { DomSanitizer } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'renderResourceURLsafe' })
export class RenderResourceURLSafePipe implements PipeTransform {
  constructor(private sanitized: DomSanitizer) {}
  transform(value) {
    // console.log(this.sanitized.bypassSecurityTrustResourceUrl(value));
    return this.sanitized.bypassSecurityTrustResourceUrl(value);
  }
}
