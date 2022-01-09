import { Injectable } from '@angular/core';
import { IndexUtil } from 'app/entities/artha-models/index-util.model';

@Injectable()
export class IndexUtilHelper {
  private indexes: any[] = [];
  getIndexes() {
    this.indexes = [
      // Department Payout
      [
        {
          title: 'Department Payout',
          value: [
            new IndexUtil('department-payout', 'department-payout', false),
            new IndexUtil('department-revenues', 'department-revenues', false),
            new IndexUtil('healthcare-department-centre', 'healthcare-department-centre', false)
          ]
        }
      ],
      // Variable Payout
      [
        {
          title: 'Variable Payout',
          value: [
            new IndexUtil('doctor-payout', 'doctor-payout', false),
            new IndexUtil('variable-payout', 'variable-payout', false),
            new IndexUtil('service-item-benefit-templates', 'service-item-benefit-templates', false),
            new IndexUtil('service-item-benefits', 'rules',false)
          ]
        }
      ],
      // Payout Adjustment
      [
        {
          title: 'Payout Adjustment',
          value: [new IndexUtil('payout-adjustment', 'payout-adjustment', false), new IndexUtil('payoutdetails', 'payoutdetails', false)]
        }
      ],
      // Administration
      [
        {
          title: 'Administration',
          value: [
            new IndexUtil('user-master', 'user', false, true),
            new IndexUtil('department', 'department', false, false),
            new IndexUtil('organizations', 'organizations', false, true),
            new IndexUtil('user-organization-department', 'user-organization-mapping', false, true),
            new IndexUtil('groups', 'group', false, true),
            new IndexUtil('value-sets', 'value-set', false, true),
            new IndexUtil('value-set-codes', 'value-set-code', false, true),
            new IndexUtil('feature', 'feature', false, false), // i
            new IndexUtil('privilege', 'privilege', false, true),
            new IndexUtil('role', 'role', false, true),
            new IndexUtil('module', 'module', false, true),
            new IndexUtil('user-dashboards', 'user-dashboards', false, true),
            new IndexUtil('user-dashboard-widgets','user-dashboard-widgets', false, true),
            new IndexUtil('widget-masters','widget-masters', false, true)
          ]
        }
      ]
    ];
    // Sorting index list
    for (let i = 0; i < this.indexes.length; i++) {
      for (let j = 0; j < this.indexes[i].length; j++) {
        this.indexes[i][j].value = this.indexes[i][j].value.sort(function(a, b) {
          const x = a.entity.toLowerCase();
          const y = b.entity.toLowerCase();
          if (x < y) {
            return -1;
          }
          if (x > y) {
            return 1;
          }
          return 0;
        });
      }
    }
    return this.indexes;
  }
}
