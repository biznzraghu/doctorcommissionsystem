import { Injectable } from '@angular/core';
import { VariablePayoutService } from '../variable-payout.service';

@Injectable()
export class AddRuleHelperService {
  public activeDepartmentsList: any[] = [];
  constructor(private variablePayoutService: VariablePayoutService) {}
  getDepartmentList(organizationCode, employeeId?) {
    return new Promise((resolve, reject) => {
      if (this.activeDepartmentsList.length > 0) {
        resolve(this.activeDepartmentsList);
      } else {
        if (employeeId) {
          this.variablePayoutService
            .getUserOrgDeptSearch({
              query: `organization.active:true AND organization.code:${organizationCode} AND userMaster.id:${employeeId}`
            })
            .subscribe(
              (res: any) => {
                if (res.body && res.body.length > 0 && res.body[0].department) {
                  this.activeDepartmentsList = res.body[0].department.filter(dept => (dept.codeName = dept.code + '|' + dept.name));
                } else {
                  this.activeDepartmentsList = [];
                }
                resolve(this.activeDepartmentsList);
              },
              () => {
                reject({ error: 'Department not found!' });
              }
            );
        } else {
          this.variablePayoutService.getDepartment({ query: `active:true AND organization.code:${organizationCode} ` }).subscribe(
            (res: any) => {
              this.activeDepartmentsList = res.body.filter((dept: any) => (dept.codeName = dept.code + '|' + dept.name));
              resolve(this.activeDepartmentsList);
            },
            () => {
              reject({ error: 'Department not found!' });
            }
          );
        }
      }
    });
  }
  getValyeApiBasedOnType(ruleType) {
    let api: string;
    if (ruleType === 'SERVICE_NAME' || ruleType === 'SERVICE_INSIDE_PACKAGE' || ruleType === 'ALL_SERVICES') {
      api = 'api/_search/service-masters';
    } else if (ruleType === 'SERVICE_GROUP') {
      api = 'api/_search/groups';
    } else if (ruleType === 'SERVICE_TYPE') {
      api = 'api/_search/service-types';
    } else if (ruleType === 'PACKAGE_CATEGORY') {
      api = 'api/package-category-type';
    } else if (
      ruleType === 'ALL_PACKAGES' ||
      ruleType === 'PACKAGE_MINUS_MATERIAL_COST' ||
      ruleType === 'PACKAGE' || // no need
      ruleType === 'PACKAGE_NAME' ||
      ruleType === 'PACKAGE_GROUP' // disscyssion
    ) {
      api = 'api/_search/package-masters';
    } else if (ruleType === 'ITEM_NAME') {
      api = 'api/_search/items';
    } else if (ruleType === 'ALL_ITEMS') {
      api = 'api/items';
    } else if (ruleType === 'ITEM_CATEGORY') {
      api = 'api/_search/item-categories';
    } else if (ruleType === 'ITEM_GROUP') {
      api = 'api/_search/value-set-codes';
    } else if (ruleType === 'INVOICE' || ruleType === 'Invoice_With_Anaesthesia' || ruleType === 'Invoice_With_Surgery') {
      api = 'api/invoice-value-type';
    }
    return api;
  }
  typeValueMapping(ruleData) {
    return new Promise((resolve, reject) => {
      const rule = { ...ruleData };
      const type = rule.type;
      switch (type) {
        case 'SERVICE_GROUP': {
          const selectedValue = rule.serviceGroup;
          resolve({ selectedValue });
          break;
        }
        case 'SERVICE_NAME': {
          const selectedValue = rule.components;
          resolve({ selectedValue });
          break;
        }
        case 'PACKAGE_CATEGORY': {
          const value = {
            code: rule.packageCategory,
            name: rule.templateValueDisplay
          };
          const selectedValue = value;
          resolve({ selectedValue });
          break;
        }
        case 'SERVICE_INSIDE_PACKAGE': {
          const selectedValue = rule.components;
          resolve({ selectedValue });
          break;
        }
        case 'ITEM_NAME': {
          const selectedValue = rule.components;
          resolve({ selectedValue });
          break;
        }
        case 'ITEM_GROUP': {
          const selectedValue = rule.itemGroup;
          resolve({ selectedValue });
          break;
        }
        // case 'PACKAGE_MINUS_MATERIAL_COST': {

        // }
        case 'PACKAGE_NAME': {
          const selectedValue = rule.components;
          resolve({ selectedValue });
          break;
        }
        case 'ITEM_CATEGORY': {
          const selectedValue = rule.itemCategory;
          resolve({ selectedValue });
          break;
        }
        case 'SERVICE_TYPE': {
          const selectedValue = rule.serviceType;
          resolve({ selectedValue });
          break;
        }
        case 'INVOICE': {
          const value = {
            code: rule.invoiceValue,
            name: rule.templateValueDisplay
          };
          const selectedValue = value;
          resolve({ selectedValue });
          break;
        }
        case 'Invoice_With_Anaesthesia': {
          const value = {
            code: rule.invoiceValue,
            name: rule.templateValueDisplay
          };
          const selectedValue = value;
          resolve({ selectedValue });
          break;
        }
        case 'Invoice_With_Surgery': {
          const value = {
            code: rule.invoiceValue,
            name: rule.templateValueDisplay
          };
          const selectedValue = value;
          resolve({ selectedValue });
          break;
        }
        // case 'PACKAGE_GROUP': {
        default: {
          if (type === 'ALL_SERVICES' || type === 'ALL_PACKAGES' || type === 'ALL_ITEMS') {
            reject();
          } else {
            reject({ error: 'type value mapping not found!' });
          }
        }
        // }
      }
    });
  }
}
