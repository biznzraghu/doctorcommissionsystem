import { ValueSet } from './value-set.model';

export class Organization {
    constructor(
        public active?: boolean,
        public clinical?: boolean,
        public code?: string,
        public description?: string,
        public id?: number,
        public licenseNumber?: string,
        public name?: string,
        public partOf?: Organization,
        public startedOn?: string,
        public type?: OrganizationType,
        public website?: string
    ) {

    }
}

export class OrganizationType {
    constructor(
        public active?: boolean,
        public code?: string,
        public comments?: string,
        public definition?: string,
        public display?: string,
        public displayOrder?: number,
        public id?: number,
        public level?: string,
        public sourc?: string,
        public valueSet?: ValueSet
    ) {

    }
}