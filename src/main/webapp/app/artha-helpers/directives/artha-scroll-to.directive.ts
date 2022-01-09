import { Directive, HostListener, Input, OnInit } from '@angular/core';
declare const $: any;

@Directive({
  selector: '[jhiArthaScrollTo]'
})
export class ArthaScrollToDirective implements OnInit {
  // tslint:disable-next-line:no-input-rename
  @Input('arthaScrollTo') arthaScrollTo: any;
  // tslint:disable-next-line:no-input-rename
  @Input('scrollBoxID') scrollBoxID: any;

  constructor() {}

  ngOnInit(): void {}

  @HostListener('mousedown')
  onMouseClick() {
    const id = this.arthaScrollTo;
    const scrollBoxID = this.scrollBoxID;
    const elementOffset = $(scrollBoxID).offset().top + 10;

    $(scrollBoxID).animate(
      {
        scrollTop: $(scrollBoxID).scrollTop() + ($(id).offset().top - elementOffset)
      },
      1000
    );
  }
}
