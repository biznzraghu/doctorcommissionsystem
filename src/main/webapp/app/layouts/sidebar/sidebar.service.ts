import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';

@Injectable()
export class SidebarService {
  private userMenus = new BehaviorSubject<Array<any>>([]);
  userMenusCast = this.userMenus.asObservable();
  private menuUrl = 'api/user-menus';

  constructor(private http: HttpClient) {}


  load(): Observable<any> {
    return this.http.get(this.menuUrl);
  }

  updatedUserMenu(data) {
    this.userMenus.next(data);
  }
}
