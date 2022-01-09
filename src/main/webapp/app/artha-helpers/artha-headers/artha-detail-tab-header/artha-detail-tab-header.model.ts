import { TabListDTO } from '../list-header/list-header.model';

export class ArthaDetailTabHeaderOptions {
  constructor(
    public disableExport?: boolean,
    public disableSearch?: boolean,
    public displayTabView?: boolean,
    public displayTabViewList?: Array<TabListDTO>,
    public selectedTabIndex?: number
  ) {}
}
