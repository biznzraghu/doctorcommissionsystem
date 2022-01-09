
import { ValueSet } from './value-set.model';
import { Group } from './group.model';

export class ServiceMaster {
    constructor(
        public active?: boolean,
        public autoProcess?: boolean,
        public category?: ServiceCategory,
        public code?: string,
        public consentRequired?: boolean,
        public id?: number,
        public individuallyOrderable?: boolean,
        public insuranceExempted?: boolean,
        public minReOrderDuration?: string,
        public name?: string,
        public profileService?: boolean,
        public serviceDuration?: string,
        public serviceGroup?: Group,
        public serviceType?: ServiceType,
        public shortName?: string
    ) {

    }
}

export class ServiceCategory {
    constructor(
        public active?: boolean,
        public code?: string,
        public comments?: string,
        public definition?: string,
        public display?: string,
        public displayOrder?: number,
        public id?: number,
        public level?: string,
        public source?: string,
        public valueSet?: ValueSet
    ) {

    }
}

export class ServiceType {
    constructor(
        public active?: boolean,
        public code?: string,
        public comments?: string,
        public definition?: string,
        public display?: string,
        public displayOrder?: number,
        public id?: number,
        public level?: string,
        public source?: string,
        public valueSet?: ValueSet
    ) {

    }
}