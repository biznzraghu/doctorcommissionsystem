import { IUser, User } from './user.model';
export interface IPreferences {
    id?: number;
    user?: IUser;
    hospital?: IHospital;
    department?: IHospital;
    healthcareServiceCenter?: IHsc;
    validateTimeZone?: boolean;
    organization: any;
}
export declare class Preferences implements IPreferences {
    id?: number;
    user?: User;
    hospital?: Hospital;
    department?: Hospital;
    healthcareServiceCenter?: Hsc;
    validateTimeZone?: boolean;
    organization: any;
    constructor(id?: number, user?: User, hospital?: Hospital, department?: Hospital, healthcareServiceCenter?: Hsc, validateTimeZone?: boolean);
}
export interface IHospital {
    id?: number;
    code?: string;
    name?: string;
    active?: boolean;
    type?: string;
    partOf?: any;
}
export declare class Hospital implements IHospital {
    id?: number;
    code?: string;
    name?: string;
    active?: boolean;
    type?: string;
    partOf?: any;
    constructor(id?: number, code?: string, name?: string, active?: boolean, type?: string, partOf?: any);
}
export interface IHsc {
    id?: number;
    code?: string;
    name?: string;
    active?: boolean;
    serviceCategory?: string;
    capabilities?: any[];
    partOf?: Hospital;
}
export declare class Hsc {
    id?: number;
    code?: string;
    name?: string;
    active?: boolean;
    serviceCategory?: string;
    capabilities?: any[];
    partOf?: Hospital;
    constructor(id?: number, code?: string, name?: string, active?: boolean, serviceCategory?: string, capabilities?: any[], partOf?: Hospital);
}
