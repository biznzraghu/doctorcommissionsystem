import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AdministrationGroup, AdministrationGroupType, AdministrationGroupMember } from 'app/entities/artha-models/group.model';
import { Organization } from 'app/entities/artha-models/organization.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiLanguageService, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { GroupService } from '../group.service';
import { ErrorParser } from 'app/shared/util/error-parser.service';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UnitService } from '../../administration-services/unit.service';
import { GroupTypeService } from '../group-type';
import { UserOrganizationMappingService } from '../../administration-services/user-organization-mapping.service';

@Component({
  selector: 'jhi-group-dialog',
  templateUrl: './group-dialog.component.html'
})
export class GroupDialogComponent implements OnInit, OnDestroy {
  @ViewChild('editForm', { static: false }) editForm: NgForm;
  group: AdministrationGroup;
  authorities: any[];
  isSaving: boolean;
  subscription: any;

  grouptypes: AdministrationGroupType[];
  groupMember: AdministrationGroupMember;
  groups: AdministrationGroup[];

  organizations: Organization[];
  contextList: string[];

  itemRemoved = false;
  selectPartOf: any;
  unitStatus: any = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jhiLanguageService: JhiLanguageService,
    private alertService: JhiAlertService,
    private groupService: GroupService,
    private groupTypeService: GroupTypeService,
    private organizationService: UnitService,
    private eventManager: JhiEventManager,
    private userOrganizationMappingService: UserOrganizationMappingService,
    private errorParser: ErrorParser
  ) {
    this.group = new AdministrationGroup();
    this.subscription = this.route.data.subscribe(data => {
      if (data['group'] && data['group'].body) {
        this.group = data['group'].body;
        if (this.group.context) {
          this.contextChange(this.group.context);
        }
      }
    });
  }

  ngOnInit() {
    this.isSaving = false;
    this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    this.groupTypeService.search({ query: '*', size: 9999 }).subscribe(
      (res: HttpResponse<any>) => {
        this.grouptypes = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res)
    );
    this.organizationService.search({ query: '*', size: 9999 }).subscribe(
      // query: 'type.code.raw:prov', size: 9999, sort: ['name.raw,asc']
      (res: HttpResponse<any>) => {
        this.organizations = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res)
    );
    this.populateContextList();
  }

  clear() {
    this.router.navigate(['administrator/group']);
  }

  save() {
    this.isSaving = true;
    if (this.group.id !== undefined) {
      this.groupService.update(this.group).subscribe(
        (res: any) => this.onSaveSuccess(res),
        (res: HttpResponse<any>) => this.onSaveError(res)
      );
    } else {
      this.groupService.create(this.group).subscribe(
        (res: any) => this.onSaveSuccess(res),
        (res: HttpResponse<any>) => this.onSaveError(res)
      );
    }
  }

  private onSaveSuccess(result: AdministrationGroup) {
    this.alertService.success('global.messages.response-msg', { msg: 'Changes successfully saved.' });
    this.eventManager.broadcast({ name: 'groupListModification', content: 'OK' });
    this.group = result;
    this.clear();
  }

  private onSaveError(error) {
    this.isSaving = false;
    this.onError(error);
  }

  private onError(error) {
    if (error.message === '10087') {
      this.errorParser.alertWithParams(error, { uniqueField: 'Group Code' });
    } else {
      this.errorParser.parse(error);
    }
  }
  trackGroupTypeById(index: number, item: AdministrationGroupType) {
    return item.id;
  }

  trackOrganizationById(index: number, item: Organization) {
    return item.id;
  }

  trackGroupById(index: number, item: AdministrationGroup) {
    return item.id;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  editGroupMember(groupMember) {
    this.groupMember = groupMember;
  }

  createGroupMember() {
    this.groupMember = new AdministrationGroupMember();
  }

  clearGroupMember() {
    this.groupMember = null;
  }

  removeGroupMember(code) {
    for (let i = 0; i < this.group.members.length; i++) {
      if (this.group.members[i].member.code === code) {
        this.group.members.splice(i, 1);
        this.itemRemoved = true;
        break;
      }
    }
  }

  addGroupMember() {
    this.unitStatus = [];
    let added = false;
    for (let i = 0; i < this.group.members.length; i++) {
      if (this.group.members[i].member.code === this.groupMember.member.code) {
        this.group.members[i] = this.groupMember;
        added = true;
        break;
      }
    }
    this.checkWithUnitMappingAndAddToMemberList(added);
  }

  private checkWithUnitMappingAndAddToMemberList(isAlreadyAdded) {
    if (!this.group.partOf) {
      if (!isAlreadyAdded) {
        this.group.members.push(this.groupMember);
      }
      this.groupMember = null;
    }
    // const selectPartOf = this.group.partOf.code;
    this.userOrganizationMappingService
      .search({ query: 'user.login:' + this.groupMember.member.code + ' organization.code:' + this.group.partOf.code })
      .map((res: HttpResponse<any>) => res.body)
      .subscribe(
        data => {
          if (data.length === 0) {
            this.errorParser.parse({ key: 'arthaApp.group.messages.userIsNotMappedToUnit' });
          } else {
            if (!isAlreadyAdded) {
              this.group.members.push(this.groupMember);
            }
            this.groupMember = null;
          }
        },
        err => {
          this.errorParser.parse(err);
        }
      );
  }

  canDeactivate() {
    if (this.editForm.dirty && !this.isSaving) {
      return Observable.of(false);
    } else {
      return Observable.of(true);
    }
  }

  populateContextList() {
    this.contextList = [];
    this.contextList.push('Service_Group');
    this.contextList.push('Billing_Group');
    this.contextList.push('Financial_Group');
    this.contextList.sort();
  }

  contextChange(context: string) {
    context ? context : context;
    this.groupService.search({ query: 'context:' + this.group.context, size: 9999 }).subscribe(
      (res: HttpResponse<any>) => {
        this.groups = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res)
    );
  }
}
