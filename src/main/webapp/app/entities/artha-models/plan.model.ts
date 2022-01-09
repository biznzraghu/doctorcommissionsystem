import { Organization } from './organization.model';
import { ValueSet } from './value-set.model';

export class Plan {
    constructor(
        public active?: boolean,
        public applicableEndDate?: string,
        public applicableStartDate?: string,
        public code?: string,
        public contractEndDate?: string,
        public contractStartDate?: string,
        public createdBy?: string,
        public createdDate?: string,
        public exceptionSponsor?: ExceptionSponsor,
        public id?: number,
        public ipAuthorization?: boolean,
        public modifiedBy?: string,
        public modifiedDate?: string,
        public name?: string,
        public opAuthorization?: boolean,
        public partOf?: Plan,
        public planTemplate?: Plan,
        public sponsor?: Organization,
        public sponsorPayTax?: boolean,
        public template?: boolean
    ) {

    }
}

export class ExceptionSponsor {
    constructor(
        public applicable?: boolean,
        public id?: number,
        public plans?: Array<Plan>,
        public sponsorType?: Array<SponsorType>
    ) {

    }
}

export class SponsorType {
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