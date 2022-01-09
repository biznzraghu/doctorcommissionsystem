import { Component, OnInit, OnDestroy } from '@angular/core';
import { Role } from '../models/role.model';
import { RoleService } from '../role.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ViewDetailsHeaderOptions } from 'app/artha-helpers/artha-headers/view-details-header/view-details-header-options.model';

@Component({
  selector: 'jhi-role-detail',
  templateUrl: './role-detail.component.html'
})
export class RoleDetailComponent implements OnInit, OnDestroy {
  role: Role;
  private route$: Subscription;
  headerOptions;
  constructor(private roleService: RoleService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.route$ = this.route.data.subscribe(data => {
      this.role = data['role'];
      this.headerOptions = new ViewDetailsHeaderOptions(
        'arthaApp.group.detail.title',
        null,
        false,
        false,
        false,
        '/administrator/role/' + this.role.id + '/edit'
      );
      this.headerOptions.editPrivileges = '100100101';
      this.headerOptions.showSearchBox = true;
      this.headerOptions.entityname = 'role';
      this.headerOptions.createPrivilege = '100100101';
      this.headerOptions.searchURL = 'api/_search/roles';
      this.headerOptions.inputformatSearchTypeheadData = (x: Role) => x.name;
      this.headerOptions.displayformatSearchTypeheadData = (x: Role) => x.name + ' | ' + x.description;
      this.headerOptions.inputPlaceholderText = 'Search Role';
    });
  }

  previousState() {
    this.router.navigate(['/administrator/role'], { replaceUrl: true });
  }

  ngOnDestroy() {
    this.route$.unsubscribe();
  }

  searchResult(chosenRole: Role) {
    this.router.navigate(['/administrator/role', chosenRole.id], { replaceUrl: true });
  }

  showAuditVersion() {}
}
