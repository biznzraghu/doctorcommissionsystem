import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SearchTermModify, UtilsHelperService } from 'app/artha-helpers';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
import { Role } from '../models/role.model';
import { RoleService } from '../role.service';

@Component({
  selector: 'jhi-role-privileges-search',
  templateUrl: './role-privileges-search.component.html'
})
export class RolePrivilegesSearchComponent implements OnInit {
  @Input() findDetOfRole: Role;
  public searching = false;
  public modulesList = [];
  public selectedRole: Role;
  public searchFailed;
  public addRole: Role;
  privilegesWithModuleAndFeatures = {};
  privilegeColorCodes = {
    List: 'list',
    All: 'all',
    View: 'view',
    Approval: 'approval',
    Modify: 'create',
    Others: 'default'
  };
  privilegesToFilter: Map<string, boolean> = new Map<string, boolean>();
  constructor(
    public activeModal: NgbActiveModal,
    private roleService: RoleService,
    private searchTermModify: SearchTermModify,
    private utils: UtilsHelperService
  ) {}

  ngOnInit() {
    if (!this.utils.isEmpty(this.findDetOfRole)) {
      this.OnSelectRole(this.findDetOfRole);
    }
  }

  //  features -NgbTypeaheadConfig
  searchRolesTypehead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(term => (this.searching = term.length >= 3)),
      switchMap(term =>
        term.length < 1
          ? of([])
          : this.search(term).pipe(
              tap(() => (this.searchFailed = false)),
              catchError(() => {
                this.searchFailed = true;
                return of([]);
              })
            )
      ),
      tap(() => (this.searching = false))
    );

  search(term): Observable<Role[]> {
    return this.roleService.searchRoles({ query: 'active:true name:' + this.searchTermModify.modify(term) });
  }

  formatRolesTypeheadData = (x: Role) => x.name;
  inputFormatRolesTypeheadData = (x: Role) => x.name;

  OnSelectRole(role: Role) {
    if (!role) {
      return;
    }
    this.selectedRole = role;
    this.privilegesWithModuleAndFeatures = this.getPrivligesByModuleAndFeatures(this.selectedRole.privileges);
  }

  public addSelectedRole() {
    this.activeModal.close(this.selectedRole);
  }

  private getPrivligesByModuleAndFeatures(result) {
    const privilegesWithModuleAndFeatures = this.privilegesWithModuleAndFeatures;
    const sortWithModule = this.utils.groupBy(result, 'feature.module.name');
    const results = Object.keys(sortWithModule).reduce((acc, current) => {
      const feature = this.utils.groupBy(sortWithModule[current], 'feature.name');
      acc[current] = feature;
      return acc;
    }, {});
    Object.keys(results).forEach(key => {
      privilegesWithModuleAndFeatures[key] = { ...privilegesWithModuleAndFeatures[key], ...results[key] };
    });
    return privilegesWithModuleAndFeatures;
  }
}
