import { Injectable } from '@angular/core';
import { StateStorageService } from 'app/core/auth/state-storage.service';


@Injectable({
    providedIn: 'root'
})
export class PreferenceService {
    constructor(
        private stateStorageService: StateStorageService
    ) { }

    currentUser() {
        return this.stateStorageService.getValue('preferences');
    }

    public modify(term: string): string {
        let cleanStr = term.trim();
        cleanStr = cleanStr.replace (/\+/g,' ');
        cleanStr = cleanStr.replace(/  +/g, ' ');
        cleanStr = cleanStr.replace (/"/g,'');
        cleanStr = cleanStr.replace (/-/g,'');
        // cleanStr = cleanStr.replace (/\:/g,'\\:');
        cleanStr = cleanStr.replace (/\//g,'\\/');
        cleanStr = cleanStr.replace (/\[/g,'\\[');
        cleanStr = cleanStr.replace (/\]/g,'\\]');
        cleanStr = cleanStr.replace (/\(/g,'\\(');
        cleanStr = cleanStr.replace (/\)/g,'\\)');
         const splitStr = cleanStr.split(" ");
         let convertStr = "";
         splitStr.map(function(obj, index){
             convertStr += "*" + obj + "*" + ' ';
         });
        if(convertStr.trim() === '**'){
               convertStr = '*';
            }
         return convertStr;
       }
}
