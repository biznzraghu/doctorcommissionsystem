import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { UtilsHelperService } from 'app/artha-helpers';
import { createRequestOption } from 'app/shared/util/request-util';
import { Subject, Observable } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { Feature } from './models/feature.model';
import { ModuleConfig } from './models/module';
import { Role } from './models/role.model';
import { Privilege } from './models/privilege.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private modulesURL = `${SERVER_API_URL}api/modules`;
  private featuresURL = `${SERVER_API_URL}api/_search/features`;
  private roleURL = `${SERVER_API_URL}/api/roles`;
  private roleSearchURL = `${SERVER_API_URL}/api/_search/roles`;
  private privilegeURL = `${SERVER_API_URL}/api/_search/privileges`;

  private changeModuleSubject = new Subject<any>();
  changeModuleAction$ = this.changeModuleSubject.asObservable();

  private moduleQuery: HttpParams;

  public modules$ = this.http
    .get<ModuleConfig[]>(this.modulesURL, { params: this.moduleQuery })
    .pipe(
      map((data: ModuleConfig[]) => data.filter(_data => _data.id !== 1)),
      tap((data: ModuleConfig[]) => {
        const [module] = data;
        this.changeModule(module.id);
      })
    );

  public modulesWithFeatures$ = this.changeModuleAction$.pipe(
    switchMap((req: any) => {
      const options = createRequestOption(req);
      return this.http
        .get<Feature[]>(this.featuresURL, { params: options })
        .pipe(
          map(data =>
            data
              .filter(({ type }) => type !== 'Internal')
              .sort(({ type: type1 }, { type: type2 }) => {
                if (type1 < type2) return -1;
                if (type2 > type2) return 1;
                return 0;
              })
          ),
          map(_data => this.utils.groupBy(_data, 'type'))
        );
    })
  );

  constructor(private http: HttpClient, private utils: UtilsHelperService) {
    this.moduleQuery = createRequestOption({ query: 'active:true *', size: 1000, sort: ['displayOrder.sort,asc'] });
  }

  changeModule(id: number) {
    const req = {
      query: 'active:true module.id:' + id,
      size: 1000,
      sort: ['displayOrder.sort,asc']
    };
    this.changeModuleSubject.next(req);
  }

  findRole(id: number): Observable<Role> {
    return this.http.get<Role>(`${this.roleURL}/${id}`);
  }

  searchPrivileges(req): Observable<Privilege[]> {
    const options = createRequestOption(req);
    return this.http.get<Privilege[]>(this.privilegeURL, { params: options });
  }

  createNewRole(role: Role): Observable<Role> {
    return this.http.post<Role>(this.roleURL, role);
  }

  updateRole(role: Role): Observable<Role> {
    return this.http.put<Role>(this.roleURL, role);
  }

  searchRoles(req): Observable<Role[]> {
    const options = createRequestOption(req);
    return this.http.get<Role[]>(this.roleSearchURL, { params: options });
  }
}
