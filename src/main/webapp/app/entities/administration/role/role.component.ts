import { Component, OnInit, OnDestroy } from '@angular/core';
import { ListHeaderOptions } from 'app/artha-helpers/artha-headers/list-header/list-header.model';
import { Router } from '@angular/router';
import { RoleService } from './role.service';
import { Role } from './models/role.model';
import { Subscription } from 'rxjs';
import { DestroyHelperService } from 'app/artha-helpers/helper/destroy-helper.service';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ErrorParser } from 'app/shared/util/error-parser.service';

@Component({
  selector: 'jhi-role',
  templateUrl: './role.component.html'
})
export class RoleComponent implements OnInit, OnDestroy {
  title: string;
  pageName: string;
  createBtnTitle: string;
  options: any;
  roles: Role[];
  private currentSearch;
  private roles$: Subscription;
  page = 0;
  predicate: any;
  reverse: any;
  constructor(
    private router: Router, 
    private roleService: RoleService, 
    private destroyer: DestroyHelperService,
    private httpBlockerService: HttpBlockerService,
    private errorParser: ErrorParser
  ) {
    this.pageName = 'role';
    this.title = 'Role';
    this.createBtnTitle = 'CREATE NEW';
    this.reverse = false;
    this.predicate = 'name.sort';
    this.options = new ListHeaderOptions(
      'artha/administrator/users', 
      false, 
      false, 
      false, 
      false, 
      true, 
      true, 
      ['104100', '104102'], 
      false);
  }

  ngOnInit() {
    this.loadRole();
  }

  ngOnDestroy() {
    this.destroyer.unsubscribe([this.roles$]);
  }
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  search(query) {
    if (!query) {
      return this.clear();
    }
    // this.reset();
    this.page = 0;
    this.currentSearch = query;
    this.loadRole();
  }

  public reset() {
    this.page = 0;
    this.loadRole();
  }

  clear() {
    this.page = 0;
    this.currentSearch = '';
    this.loadRole();
  }

  createUser() {
    this.router.navigate(['/artha/administrator/new-role']);
  }

  loadRole() {
    this.httpBlockerService.enableHttpBlocker(true);
    this.roles$ = this.roleService
      .searchRoles({
        page: this.page,
        query: this.currentSearch ? this.currentSearch : '*',
        size: 20,
        sort: this.sort()
      })
      .subscribe(
        (res: any) => {
        this.roles = res;
        this.httpBlockerService.enableHttpBlocker(false);
      },
      (res: HttpErrorResponse) => {
        this.onError(res.error);
        this.httpBlockerService.enableHttpBlocker(false);
      }
    );
  }

  private onError(error) {
    this.errorParser.parse(error);
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  gotoRole({ id }) {
    this.router.navigate([`/artha/administrator/role/${id}`]);
  }
}
