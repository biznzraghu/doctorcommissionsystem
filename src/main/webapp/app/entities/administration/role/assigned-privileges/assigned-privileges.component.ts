import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsHelperService } from 'app/artha-helpers';
import { ModuleFeaturesModalComponent } from '../..';
import { Feature } from '../models/feature.model';
import { Privilege } from '../models/privilege.model';
import { RoleService } from '../role.service';

@Component({
  selector: 'jhi-assigned-privileges',
  templateUrl: './assigned-privileges.component.html'
})
export class AssignedPrivilegesComponent implements OnInit {
  @Input() privileges: Feature[];
  @Output() privilegesAdded: EventEmitter<Privilege[]> = new EventEmitter<Privilege[]>();
  @Input() showAddFeatureBtn = true;
  privilegesWithModuleAndFeatures = {};
  removedPrivileges = [];
  unAssignedPrivileges: Privilege[];
  privilegeColorCodes = {
    List: 'list',
    All: 'all',
    View: 'view',
    Approval: 'approval',
    Modify: 'create',
    Others: 'default'
  };
  privilegesToFilter: Map<string, boolean> = new Map<string, boolean>();
  constructor(private modalService: NgbModal, private utils: UtilsHelperService, private roleService: RoleService) {}

  public ngOnInit() {
    if (!this.utils.isEmpty(this.privileges)) {
      this.privilegesWithModuleAndFeatures = this.getPrivligesByModuleAndFeatures(this.privileges);
    }
  }
  showModuleFeatures() {
    const modalRef = this.modalService.open(ModuleFeaturesModalComponent, {
      windowClass: 'vertical-middle-modal athma-modal-dialog sm primary',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.result.then((features: Feature[]) => {
      this.OnSelectFeatures(features);
    });
  }

  privilegeLabels(): Array<string> {
    return Object.keys(this.privilegeColorCodes).sort((n1, n2): number => {
      if (n1 < n2) return -1;
      if (n1 > n2) return 1;
      return 0;
    });
  }

  removeFilters() {}

  initFilters(): void {
    this.privilegeLabels().forEach(element => {
      this.privilegesToFilter[element] = false;
    });
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  filterPrivileges(value: boolean): void {
    const filerList = [];
    this.privilegeLabels().forEach(element => {
      if (true === this.privilegesToFilter[element]) {
        filerList.push(element);
      }
    });
  }

  OnSelectFeatures(features: Feature[]) {
    if (this.utils.isEmpty(features)) {
      return;
    }
    const featureId = this.utils.pluck(features, 'id').join(' OR ');
    this.roleService
      .searchPrivileges({
        query: `active:true AND feature.id: (${featureId})`,
        size: 1000
      })
      .subscribe((res: Privilege[]) => {
        const result = res;
        this.privilegesWithModuleAndFeatures = this.getPrivligesByModuleAndFeatures(result);
        this.emitPrivilges();
      });
  }

  getLength(val) {
    return Object.keys(val).length;
  }

  unAssignPrivilege(...param) {
    const [module, feature, privilege, index] = param;
    this.removedPrivileges = [...this.removedPrivileges, privilege];
    this.privilegesWithModuleAndFeatures[module.key][feature.key] = this.privilegesWithModuleAndFeatures[module.key][feature.key].filter(
      (item, _index) => index !== _index
    );
    this.emitPrivilges();
  }

  assignPrivilege(privilege: Privilege) {
    const {
      feature: { name: _featureName }
    } = privilege;
    const {
      feature: {
        module: { name: moduleName }
      }
    } = privilege;
    this.privilegesWithModuleAndFeatures[moduleName][_featureName] = [
      ...this.privilegesWithModuleAndFeatures[moduleName][_featureName],
      privilege
    ];
    this.removedPrivileges = this.removedPrivileges.filter(_prev => _prev.id !== privilege.id);
    this.emitPrivilges();
  }

  showUnAssignedPrivileges(feature) {
    this.unAssignedPrivileges = this.removedPrivileges.filter(_privilege => {
      return feature.key === _privilege.feature.name;
    });
  }

  emitPrivilges() {
    const privileges = this.getPrivilgesInList(this.privilegesWithModuleAndFeatures);
    this.privilegesAdded.emit(privileges);
  }

  private getPrivilgesInList(privileges): Feature[] {
    return Object.keys(privileges).reduce((acc, current) => {
      const features = Object.keys(privileges[current]).reduce((_acc, _current) => {
        _acc = [..._acc, ...privileges[current][_current]];
        return _acc;
      }, []);
      acc = [...acc, ...features];
      return acc;
    }, []);
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
