import { Injectable } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { VariablePayoutTemplate } from 'app/entities/artha-models/variable-payout-template.model';
import { HttpBlockerService } from 'app/layouts/http-blocker/http-blocker-spinner.service';

@Injectable({
  providedIn: 'root'
})
export class TemplateValidationService {
  constructor(private jhiAlertService: JhiAlertService, private httpBlockerService: HttpBlockerService) {}
  public validateQuantityFromTo(quantityFromTo) {
    return new Promise((resolve, reject) => {
      if (quantityFromTo) {
        const splitArray = quantityFromTo.split('-');
        let fromQuantity: number;
        let toQuantity: number;
        if (splitArray.length === 2) {
          fromQuantity = +splitArray[0];
          toQuantity = +splitArray[1];
          if (isNaN(fromQuantity)) {
            // quantityFromTo = undefined;
            const errorMessage: any = 'Please enter valid <strong>From Quantity</strong>';
            reject({ error: errorMessage });
            // this.customError(errorMessage);
            // return;
          } else {
            if (isNaN(toQuantity)) {
              // quantityFromTo = undefined;
              const errorMessage: any = 'Please enter valid <strong>To Quantity</strong>';
              reject({ error: errorMessage });
              // this.customError(errorMessage);
              // return;
            } else {
              if (!isNaN(fromQuantity) && !isNaN(toQuantity)) {
                if(fromQuantity > 99999999 || toQuantity > 99999999) {
                  const errorMessage: any = fromQuantity > 99999999 ? '<strong>From Quantity</strong> should not be greater than 9,99,99,999' : '<strong>To Quantity</strong> should not be greater than 9,99,99,999';
                  reject({ error: errorMessage });

                } else {
                  if (fromQuantity > toQuantity || fromQuantity === toQuantity) {
                    // quantityFromTo = undefined;
                    const errorMessage: any = '<strong>To Quantity</strong> should be Greater than <strong>From Quantity</strong>';
                    reject({ error: errorMessage });
                    // this.customError(errorMessage);
                    // return;
                } else {
                  resolve({ minQuantity: fromQuantity, maxQuantity: toQuantity });
                }
                }
              } else {
                resolve({ minQuantity: fromQuantity, maxQuantity: toQuantity });
              }
            }
          }
        } else {
          quantityFromTo = undefined;
          const errorMessage = 'Please enter valid <strong>From Quantity</strong> and <strong>To Quantity</strong>';
          reject({ error: errorMessage });
          // this.customError(errorMessage);
        }
      } else {
        reject();
      }
    });
  }
  public addNewRuleValidation(rule, selectedValue, selectedVisitList) {
    return new Promise((resolve, reject) => {
      this.typeValidation(rule)
        .then(typeValidatedRule => {
          this.typeValueMappingValidation(typeValidatedRule, selectedValue)
            .then((typeValueValidatedRule: any) => {
              const ruleWithMapping = { ...typeValueValidatedRule };
              this.typeVisitValidation(selectedVisitList)
                .then(() => {
                  this.typeBeneficiaryValidation(ruleWithMapping)
                    .then(finalValidatedRule => {
                      resolve(finalValidatedRule);
                    })
                    .catch(() => {
                      const message = `Please fill all required data.
                                  <strong>Type</strong>,
                                  <strong>Visit Type</strong>,
                                  <strong>Beneficiary Type</strong>, and <strong>selected type</strong>`;
                      reject({ message });
                    })
                    .catch(error => {
                      this.customError(error.message);
                      return;
                    });
                })
                .catch(error => {
                  this.customError(error.message);
                  return;
                });
            })
            .catch(error => {
              this.customError(error.message);
              return;
            });
        })
        .catch(error => {
          this.customError(error.message);
          return;
        });
    });
  }
  private typeValidation(rule) {
    return new Promise((resolve, reject) => {
      const cpyRule = { ...rule };
      if (cpyRule.type) {
        resolve(cpyRule);
      } else {
        const message = 'Please select <strong>Type</strong>';
        reject({ message });
      }
    });
  }
  private typeVisitValidation(selectedVisitList) {
    return new Promise((resolve, reject) => {
      if (selectedVisitList && selectedVisitList.length > 0) {
        resolve(true);
      } else {
        const message = 'Please select <strong>Visit Type</strong>';
        reject({ message });
      }
    });
  }
  private typeBeneficiaryValidation(rule) {
    return new Promise((resolve, reject) => {
      if (rule.beneficiary) {
        resolve(rule);
      } else {
        const message = 'Please select <strong>Beneficiary Type</strong>';
        reject({ message });
      }
    });
  }
  private typeValueMappingValidation(typeValidatedrule, selectedValue) {
    return new Promise((resolve, reject) => {
      let message: string;
      if (typeValidatedrule.type) {
        if (
          typeValidatedrule.type === 'ALL_SERVICES' ||
          typeValidatedrule.type === 'ALL_PACKAGES' ||
          typeValidatedrule.type === 'ALL_ITEMS'
        ) {
          resolve(typeValidatedrule);
          return;
        }
        if (typeValidatedrule.type === 'SERVICE_GROUP') {
          typeValidatedrule.serviceGroup = selectedValue;
          typeValidatedrule.itemCategory = undefined;
          typeValidatedrule.itemGroup = undefined;
          typeValidatedrule.serviceType = undefined;
          typeValidatedrule.components = undefined;
          typeValidatedrule.packageCategory = undefined;
          typeValidatedrule.invoiceValue = undefined;
          typeValidatedrule.templateValueDisplay = undefined;
          resolve(typeValidatedrule);
          return;
        }
        if (selectedValue && typeof selectedValue === 'object') {
          switch (typeValidatedrule.type) {
            // case 'SERVICE_GROUP': {
            //   typeValidatedrule.serviceGroup = selectedValue;
            //   typeValidatedrule.itemCategory = undefined;
            //   typeValidatedrule.itemGroup = undefined;
            //   typeValidatedrule.serviceType = undefined;
            //   typeValidatedrule.components = undefined;
            //   typeValidatedrule.packageCategory = undefined;
            //   typeValidatedrule.invoiceValue = undefined;
            //   typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
            //   resolve(typeValidatedrule);
            //   break;
            // }

            case 'SERVICE_NAME': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = selectedValue;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'PACKAGE_CATEGORY': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = selectedValue.code;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'SERVICE_INSIDE_PACKAGE': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = selectedValue;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'ITEM_NAME': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = selectedValue;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'ITEM_CATEGORY': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = selectedValue;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'ITEM_GROUP': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = selectedValue;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }
            // case 'PACKAGE_MINUS_MATERIAL_COST': {

            // }
            // case 'PACKAGE' : {

            // }

            case 'PACKAGE_NAME': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = selectedValue;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }

            case 'SERVICE_TYPE': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = selectedValue;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              // typeValidatedrule.packageMaster = undefined;
              // typeValidatedrule.rule_key = undefined;
              // typeValidatedrule.serviceGroup = undefined;
              // typeValidatedrule.serviceItemExceptions = undefined;
              // typeValidatedrule.serviceMaster = undefined;
              typeValidatedrule.invoiceValue = undefined;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }
            case 'INVOICE': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = selectedValue.code;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }
            case 'Invoice_With_Anaesthesia': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = selectedValue.code;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }
            case 'Invoice_With_Surgery': {
              typeValidatedrule.serviceGroup = undefined;
              typeValidatedrule.itemCategory = undefined;
              typeValidatedrule.itemGroup = undefined;
              typeValidatedrule.serviceType = undefined;
              typeValidatedrule.components = undefined;
              typeValidatedrule.packageCategory = undefined;
              typeValidatedrule.invoiceValue = selectedValue.code;
              typeValidatedrule.templateValueDisplay = this.getDisplayValue(selectedValue);
              resolve(typeValidatedrule);
              break;
            }
            // case 'PACKAGE_GROUP': {

            // }

            default: {
              message = 'Property not found for <strong>selected type</strong>';
              reject({ message });
            }
          }
        } else {
          message = 'Please select a <strong>value</strong> for <strong>type</strong>';
          reject({ message });
        }
      } else {
        message = 'Please select <strong>type</strong>';
        reject({ message });
      }
    });
  }
  private getDisplayValue(selectedValue): string {
    if (selectedValue.display) {
      const templateValueDisplay = selectedValue.display;
      return templateValueDisplay;
    } else {
      if (selectedValue.name) {
        const templateValueDisplay = selectedValue.name;
        return templateValueDisplay;
      } else {
        if (selectedValue.code) {
          const templateValueDisplay = selectedValue.code;
          return templateValueDisplay;
        } else {
          const templateValueDisplay = selectedValue;
          return templateValueDisplay;
        }
      }
    }
  }

  // ///////////////       ADD RULES Validation END      //////////////////// //
  // ///////////////          Template Validation      //////////////////// //
  public saveTemplateValidation(template: VariablePayoutTemplate) {
    return new Promise((resolve, reject) => {
      this.checkCodeNotEmpty(template)
        .then((codeVarifiedTemplate: VariablePayoutTemplate) => {
          this.checkNameNotEmpty(codeVarifiedTemplate)
            .then((nameVarifiedTemplate: VariablePayoutTemplate) => {
              this.checkRulesNotEmpty(nameVarifiedTemplate)
                .then((ruleVarifiedTemplate: VariablePayoutTemplate) => {
                  resolve(ruleVarifiedTemplate);
                })
                .catch(error => {
                  this.customError(error.message);
                  reject({ error: error.message });
                  return;
                });
            })
            .catch(error => {
              this.customError(error.message);
              return;
            });
        })
        .catch(error => {
          this.customError(error.message);
          return;
        });
    });
  }
  /* --------------------- DUPLICATE RULES VALIDATION    ----------------------- */
  public isSameRuleExist(rule: any, rulesArray: any[]) {
    /**
     * type
     * templateValueDisplay
     * visitType
     * beneficiary
     * minQuantity
     * maxQuantity
     * patientCategory
     * tariffClass
     * department
     * applicableSponsor
     * rule.exceptionSponsor.applicable (Applicable sponsors)
     *
     * Req 2)
     * if Type === 'INVOICE' || 'Invoice_With_Anaesthesia' || 'Invoice_With_Surgery'
     * and with some Visit type and that visit type already exist in other rule so don't add rule
     */

    return new Promise((resolve, reject) => {
      const copyRule: any = JSON.parse(JSON.stringify(rule));
      let reservedValueSet: any[] = [];
      let reservedValueSetForInvoice: any[] = [];
      const specificKeyRule: any = {};
      if (copyRule.type) {
        specificKeyRule.type = copyRule.type;
      }
      if (copyRule.templateValueDisplay) {
        specificKeyRule.templateValueDisplay = copyRule.templateValueDisplay;
      }
      if (copyRule.visitType) {
        specificKeyRule.visitType = [];
        specificKeyRule.visitType = copyRule.visitType;
        if (specificKeyRule.visitType.length > 0) {
          specificKeyRule.visitType.sort();
        }
      }
      if (copyRule.beneficiary) {
        specificKeyRule.beneficiary = copyRule.beneficiary;
      }
      if (copyRule.minQuantity) {
        specificKeyRule.minQuantity = copyRule.minQuantity;
      }
      if (copyRule.maxQuantity) {
        specificKeyRule.maxQuantity = copyRule.maxQuantity;
      }
      if (copyRule.patientCategory) {
        specificKeyRule.patientCategory = copyRule.patientCategory;
      }
      if (copyRule.tariffClass) {
        specificKeyRule.tariffClass = copyRule.tariffClass;
        if (specificKeyRule.tariffClass.length > 0) {
          specificKeyRule.tariffClass.sort((a, b) => {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          });
        }
      }
      if (copyRule.department) {
        specificKeyRule.department = copyRule.department.map(dept => dept.id);
        if (specificKeyRule.department.length > 0) {
          specificKeyRule.department.sort((a, b) => {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          });
        }
      }
      if (copyRule.applicableSponsor) {
        specificKeyRule.applicableSponsor = copyRule.applicableSponsor;
      }
      const stringifyNewRule: any = JSON.stringify(specificKeyRule);
      // const stringifyExistingRules: any[] = this.rulesDataInstance.map(data => {
      const stringifyExistingRules: any[] = rulesArray.map(data => {
        const specificInfoObj: any = {};
        if (data.type) {
          specificInfoObj.type = data.type;
        }
        if (data.templateValueDisplay) {
          specificInfoObj.templateValueDisplay = data.templateValueDisplay;
        }
        if (
          (data.type === 'INVOICE' || data.type === 'Invoice_With_Anaesthesia' || data.type === 'Invoice_With_Surgery') &&
          data.visitType
        ) {
          const deepVisitTypeCopy = JSON.parse(JSON.stringify(data.visitType));
          if (reservedValueSetForInvoice.length === 0) {
            reservedValueSetForInvoice = deepVisitTypeCopy.sort();
          } else {
            deepVisitTypeCopy.forEach(element => {
              const isElementPresent = reservedValueSetForInvoice.some(ele => ele === element);
              if (!isElementPresent) {
                reservedValueSetForInvoice.push(element);
                reservedValueSetForInvoice.sort();
              }
            });
          }
        }
        if (data.visitType) {
          const deepVisitTypeCopy = JSON.parse(JSON.stringify(data.visitType));
          specificInfoObj.visitType = [];
          specificInfoObj.visitType = data.visitType;
          if (specificInfoObj.visitType.length > 0) {
            specificInfoObj.visitType.sort();
          }
          if (reservedValueSet.length === 0) {
            reservedValueSet = deepVisitTypeCopy.sort();
          } else {
            deepVisitTypeCopy.forEach(element => {
              const isElementPresent = reservedValueSet.some(ele => ele === element);
              if (!isElementPresent) {
                reservedValueSet.push(element);
                reservedValueSet.sort();
              }
            });
          }
        }
        if (data.beneficiary) {
          specificInfoObj.beneficiary = data.beneficiary;
        }
        if (data.minQuantity) {
          specificInfoObj.minQuantity = data.minQuantity;
        }
        if (data.maxQuantity) {
          specificInfoObj.maxQuantity = data.maxQuantity;
        }
        if (data.patientCategory) {
          specificInfoObj.patientCategory = data.patientCategory;
        }
        if (data.tariffClass) {
          specificInfoObj.tariffClass = data.tariffClass;
          if (specificInfoObj.tariffClass.length > 0) {
            specificInfoObj.tariffClass.sort((a, b) => {
              if (a.id < b.id) {
                return -1;
              }
              if (a.id > b.id) {
                return 1;
              }
              return 0;
            });
          }
        } else {
          specificInfoObj.tariffClass = [];
        }
        if (data.exceptionSponsor && data.exceptionSponsor.applicable) {
          specificInfoObj.applicable = data.exceptionSponsor.applicable;
        }
        if (data.department) {
          specificInfoObj.department = data.department.map(dept => dept.id);
          if (specificInfoObj.department.length > 0) {
            specificInfoObj.department.sort((a, b) => {
              if (a.id < b.id) {
                return -1;
              }
              if (a.id > b.id) {
                return 1;
              }
              return 0;
            });
          }
        } else {
          specificInfoObj.department = [];
        }
        return JSON.stringify(specificInfoObj);
      });
      // eslint-disable-next-line no-console
      // console.log('reservedValueSet ==== ', reservedValueSet);
      // eslint-disable-next-line no-console
      // console.log('reservedValueSetForInvoice ==== ', reservedValueSetForInvoice);
      if (
        (copyRule.type === 'INVOICE' || copyRule.type === 'Invoice_With_Anaesthesia' || copyRule.type === 'Invoice_With_Surgery') &&
        copyRule.visitType
      ) {
        if (reservedValueSet.length !== 0) {
          copyRule.visitType.forEach(element => {
            const isElementPresent = reservedValueSet.some(ele => ele === element);
            if (isElementPresent) {
              // eslint-disable-next-line no-console
              // console.log('Value set present with Invoice type/s');
              reject('Visit type already mapped with other rule');
              return;
            }
          });
        }
      } else {
        if (reservedValueSetForInvoice.length !== 0) {
          copyRule.visitType.forEach(element => {
            const isElementPresent = reservedValueSetForInvoice.some(ele => ele === element);
            if (isElementPresent) {
              // eslint-disable-next-line no-console
              // console.log('Value set present with Invoice type/s');
              reject('Visit type already mapped with other rule');
              return;
            }
          });
        }
      }
      // eslint-disable-next-line no-console
      // console.log('stringifyRule ', stringifyNewRule);
      // eslint-disable-next-line no-console
      // console.log('stringifyExistingRules ', stringifyExistingRules);
      if (stringifyExistingRules && stringifyExistingRules.length > 0) {
        const isRuleExist = stringifyExistingRules.find(stringifyRule => stringifyRule === stringifyNewRule);
        // eslint-disable-next-line no-console
        // console.log('isRuleExist ', isRuleExist);
        if (!isRuleExist) {
          resolve(rule);
        } else {
          reject();
        }
      } else {
        resolve(rule);
      }
    });
  }
  private checkCodeNotEmpty(template: VariablePayoutTemplate) {
    return new Promise((resolve, reject) => {
      if (template.templateName) {
        resolve(template);
      } else {
        this.httpBlockerService.enableHttpBlocker(false);
        const message = 'Please enter <strong>Template Name</strong>';
        reject({ message });
      }
    });
  }
  private checkNameNotEmpty(template: VariablePayoutTemplate) {
    return new Promise((resolve, reject) => {
      if (template.code) {
        resolve(template);
      } else {
        this.httpBlockerService.enableHttpBlocker(false);
        const message = 'Please enter <strong>Template Code</strong>';
        reject({ message });
      }
    });
  }
  private checkRulesNotEmpty(template: VariablePayoutTemplate) {
    return new Promise((resolve, reject) => {
      if (template) {
        // if (template.serviceItemBenefits.length > 0) {
        resolve(template);
      } else {
        this.httpBlockerService.enableHttpBlocker(false);
        const message = 'Please add atleast <strong>One Rule</strong>';
        reject({ message });
      }
    });
  }
  /**
   *  not in use.
   */
  private checkUnitMapped(template: VariablePayoutTemplate) {
    return new Promise((resolve, reject) => {
      if (template.organization.length > 1) {
        resolve(template);
      } else {
        this.httpBlockerService.enableHttpBlocker(false);
        const message = 'Please map with atleast <strong>One Unit</strong>';
        reject({ message });
      }
    });
  }
  // ///////////////       Template Validation END      //////////////////// //
  private customError(errorMessage) {
    this.jhiAlertService.error('global.messages.response-msg', { msg: errorMessage });
  }
}
