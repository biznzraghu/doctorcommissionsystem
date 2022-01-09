export enum VisitType {
  DC = 'DC',
  ER = 'ER',
  IP = 'IP',
  OP = 'OP'
}

export enum Type {
  ALL_ITEMS = 'All Items',
  ALL_PACKAGES = 'All Packages',
  ALL_SERVICES = 'All Services',
  ITEM_CATEGORY = 'Item Category',
  ITEM_GROUP = 'Item Group',
  ITEM_NAME = 'Item Name',
  PACKAGE = 'Package',
  PACKAGE_CATEGORY = 'Package Category',
  PACKAGE_GROUP = 'Package Group',
  PACKAGE_MINUS_MATERIAL_COST = 'Package Minus Material Cost',
  PACKAGE_NAME = 'Package Name',
  SERVICE_GROUP = 'Service Group',
  SERVICE_INSIDE_PACKAGE = 'Service Inside Package',
  SERVICE_NAME = 'Service Name',
  SERVICE_TYPE = 'Service Type',
  INVOICE = 'Invoice',
  Invoice_With_Anaesthesia = 'Invoice with anaesthesia',
  Invoice_With_Surgery = 'Invoice with surgery'
}

export enum DiscountType {
  NET = 'NET',
  GROSS = 'GROSS'
}

export enum BeneficiaryType {
  ADMITTING_CONSULTANT = 'Admitting Consultant',
  NONE_AND_PERFORMING_USER = 'None And Performing User',
  ORDERING_CONSULTANT = 'Ordering Consultant',
  RENDERING_CONSULTANT = 'Rendering Consultant',
  ADMITTING = 'Admitting',
  CONSULTANT = 'Consultant',
  ORDERING = 'Ordering',
  RENDERING = 'Rendering'
}

export enum PatientCategory {
  CASH = 'Cash',
  CASH_CREDIT = 'Cash Credit',
  CREDIT = 'Credit'
}

export enum MaterialAmount {
  COST = 'Cost',
  MARGIN = 'Margin',
  SALE = 'Sale'
}
