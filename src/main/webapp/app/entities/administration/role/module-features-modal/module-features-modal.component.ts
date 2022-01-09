import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Feature } from '../models/feature.model';
import { RoleService } from '../role.service';

@Component({
  selector: 'jhi-module-features-modal',
  templateUrl: './module-features-modal.component.html'
})
export class ModuleFeaturesModalComponent {
  modules$ = this.roleService.modules$;
  modulesWithFeatures$ = this.roleService.modulesWithFeatures$;
  public modulesWithFeatures: any = {};
  selectedFeatures = [];
  constructor(private roleService: RoleService, private activeModal: NgbActiveModal) {}

  onModuleChange(id: number): void {
    if (!id) {
      return;
    }
    this.roleService.changeModule(id);
  }

  close(): void {
    this.activeModal.dismiss();
  }

  addOrRemoveFeature(feature: Feature, index) {
    if (this.isSelected(feature)) {
      this.selectedFeatures = this.selectedFeatures.filter((_feature, idx) => idx !== index);
      return;
    }
    this.selectedFeatures = [...this.selectedFeatures, feature];
  }

  isSelected(feature) {
    return this.selectedFeatures.some(_feature => _feature.id === feature.id);
  }

  addSelectedFeature() {
    this.activeModal.close(this.selectedFeatures);
  }
}
