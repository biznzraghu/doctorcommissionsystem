export class IndexUtil {
  constructor(public master?: string, public entity?: string, public isIndexing?: boolean, public contain_index?: boolean) {
    this.isIndexing = false;
  }
}
