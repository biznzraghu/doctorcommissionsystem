import { Injectable } from '@angular/core';

@Injectable()
export class ExportHelper {
  constructor() {}
  openFile(data?: any) {
    let baseUrl = '/download-file?';
    let i = 0;
    const params = data;
    if (params.fileName) {
      params.fileName = encodeURIComponent(params.fileName);
    }
    for (const prop in params) {
      // eslint-disable-next-line no-prototype-builtins
      if (params.hasOwnProperty(prop)) {
        if (i > 0) {
          baseUrl += '&';
        }
        baseUrl += prop + '=' + params[prop];
        i++;
      }
    }
    window.open(baseUrl);
  }
  download(dataurl, filename) {
    const a = document.createElement('a');
    a.href = dataurl;
    a.setAttribute('download', filename);
    const b = document.createEvent('MouseEvents');
    b.initEvent('click', false, true);
    a.dispatchEvent(b);
    return false;
  }
}
