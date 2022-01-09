import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ArthaHelperService {
  constructor() {}

  getBaseURL(): string {
    const { location } = window;
    let baseUrl = `${location.protocol}//${location.hostname}`;
    // TODO: localhost for local development
    if (location.hostname === 'localhost') {
      baseUrl = location.origin;
    }
    return baseUrl;
  }
}
