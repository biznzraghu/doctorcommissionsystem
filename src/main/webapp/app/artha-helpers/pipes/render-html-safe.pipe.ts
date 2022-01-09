import { DomSanitizer } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'renderHtmlsafe' })
export class RenderHtmlSafePipe implements PipeTransform {
  constructor(private sanitized: DomSanitizer) {}
  transform(value) {
    // console.log(this.sanitized.bypassSecurityTrustHtml(value));
    return this.sanitized.bypassSecurityTrustHtml(value);
  }
}
