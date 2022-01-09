/* eslint-disable no-useless-escape */
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchTermModify {
  constructor() {}
  modify(term: string): string {
    let cleanStr = term.trim();
    cleanStr = cleanStr.replace(/\+/g, ' ');
    cleanStr = cleanStr.replace(/  +/g, ' ');
    cleanStr = cleanStr.replace(/"/g, '');
    cleanStr = cleanStr.replace(/\:/g, '\\:');
    cleanStr = cleanStr.replace(/\//g, '\\/');
    cleanStr = cleanStr.replace(/\[/g, '\\[');
    cleanStr = cleanStr.replace(/\]/g, '\\]');
    cleanStr = cleanStr.replace(/\(/g, '\\(');
    cleanStr = cleanStr.replace(/\)/g, '\\)');
    const splitStr = cleanStr.split(' ');
    let convertStr = '';
    splitStr.map(function(obj) {
      convertStr += '*' + obj + '*' + ' ';
    });
    if (convertStr.trim() === '**') {
      convertStr = '*';
    }
    return convertStr;
  }
}
