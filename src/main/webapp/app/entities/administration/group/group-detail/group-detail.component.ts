import { Component, OnInit, OnDestroy } from '@angular/core';
import { AdministrationGroup } from 'app/entities/artha-models/group.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewDetailsHeaderOptions } from 'app/artha-helpers/artha-headers/view-details-header/view-details-header-options.model';

@Component({
  selector: 'jhi-group-detail',
  templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit, OnDestroy {
  group: AdministrationGroup;
  auditVersion: string;
  private subscription: any;
  headerOptions: ViewDetailsHeaderOptions;
  auditEntity: any; // build error fix Property does not exist
  constructor(private route: ActivatedRoute, private router: Router) {}
  ngOnInit(): void {
    this.subscription = this.route.data.subscribe(data => {
      this.group = data['group'].body;
      this.headerOptions = new ViewDetailsHeaderOptions(
        'arthaApp.group.detail.title',
        null,
        false,
        false,
        false,
        '/administrator/group/' + this.group.id + '/edit'
      );
      this.headerOptions.editPrivileges = '100117101';
      this.headerOptions.entityname = 'group';
      this.headerOptions.createPrivilege = '100117101';
      this.headerOptions.searchURL = 'api/_search/groups';
      this.headerOptions.inputformatSearchTypeheadData = (x: AdministrationGroup) => x.name;
      this.headerOptions.displayformatSearchTypeheadData = (x: AdministrationGroup) => x.code + ' | ' + x.name;
      this.headerOptions.inputPlaceholderText = 'Search Group';
      this.headerOptions.disableCreate = false;
      this.headerOptions.disableEdit = this.group.modifiable ? false : true;
    });
  }
  previousState(): any {
    this.router.navigate(['administrator/group'], { replaceUrl: true });
  }
  showAuditVersion(eve) {
    // build error fix Property does not exist
    eve ? '' : '';
  }
  searchResult(result: AdministrationGroup): any {
    this.router.navigate(['administrator/group/detail', result.id], { replaceUrl: true });
  }
  ngOnDestroy(): any {
    this.subscription.unsubscribe();
  }
}
