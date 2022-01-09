import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { UtilsHelperService } from 'app/artha-helpers';
import { Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Role } from '../models/role.model';
import { RoleService } from '../role.service';

@Injectable({
  providedIn: 'root'
})
@Injectable()
export class RoleDataResolver implements Resolve<any> {
  constructor(private roleService: RoleService, private utils: UtilsHelperService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<Role> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.roleService.findRole(id).pipe(
        flatMap((res: Role) => {
          if (!this.utils.isEmpty(res)) {
            return of(res);
          } else {
            return of(new Role());
          }
        })
      );
    }
    return of(new Role());
  }
}
