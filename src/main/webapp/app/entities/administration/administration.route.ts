import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IndexUtil } from '../artha-models/index-util.model';
import { User } from '../artha-models/user.model';
import { ValueSet } from '../artha-models/value-set.model';
import { UserService } from './administration-services/user.service';
import { ValueSetService } from './administration-services/value-set.service';
import { DepartmentsComponent } from './departments/departments.component';
import { GroupDetailComponent, GroupDialogComponent, GroupListComponent } from './group/group.index';
// import { GroupDataResolver, GroupService } from './group/group.service';
import { IndexUtilDialogComponent } from './index-util/index-util-dialog.component';
import { UnitsComponent } from './units/units.component';
import { UserDetailComponent } from './users/user-detail/user-detail.component';
import { UsersComponent } from './users/users.component';
import { ValueSetCreateComponent } from './value-set/value-set-create/value-set-create.component';
import { ValueSetDetailComponent } from './value-set/value-set-detail/value-set-detail.component';
import { ValueSetComponent } from './value-set/value-set.component';
import * as RoleConstant from './role/role.constant';
import { UnitService } from './administration-services/unit.service';
import { GroupService }  from './group/group.service';
import { UnitDetailComponent } from './units/unit-detail.component';
import { UnitDialogComponent } from './units/unit-dialog/unit-dialog.component';
import { AdministrationGroup } from 'app/entities/artha-models/group.model';
@Injectable({
  providedIn: 'root'
})
export class ValueSetDataResolver implements Resolve<any> {
  constructor(private valueSetService: ValueSetService) {}
  // eslint-disable-next-line no-unused-vars
  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      return this.valueSetService.find(route.params['id']);
    } else {
      return Observable.of(new ValueSet());
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class UserResolver implements Resolve<any> {
  constructor(private userService: UserService) {}
  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    if (route.params['id']) {
      return this.userService
        .search({
          query: `id: ${route.params['id']}`,
          page: 0,
          size: 10
        })
        .pipe(map(data => data.body));
    }
    return Observable.of(new User());
  }
}

@Injectable({
  providedIn: 'root'
})
export class UnitResolver implements Resolve<any> {
  constructor(private unitService: UnitService) {}
  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    if (route.params['id']) {
      return this.unitService.find(route.params['id']).pipe(map(data => data.body));
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class IndexUtilDataResolver implements Resolve<any> {
  constructor() {}
  resolve() {
    return Observable.of(new IndexUtil());
  }
}

@Injectable({ providedIn: 'root' })
export class GroupDataResolver implements Resolve<any> {
  constructor(private groupService: GroupService) {}

  resolve(route: ActivatedRouteSnapshot) {
    if (route.params['id']) {
      return this.groupService.find(route.params['id']);
    } else {
      return Observable.of(new AdministrationGroup());
    }
  }
}



export const administrationRoute: Routes = [
  {
    path: 'value-set',
    component: ValueSetComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.valueSet.home.title',
      module: 'administrative',
      menu: 'valueSet'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'value-set-new',
    component: ValueSetCreateComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.valueSet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'value-set/:id/edit',
    component: ValueSetCreateComponent,
    resolve: {
      valueSet: ValueSetDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.valueSet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'value-set/:id/detail',
    component: ValueSetDetailComponent,
    resolve: {
      valueSet: ValueSetDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.valueSet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'units',
    component: UnitsComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'unit/:id/detail',
    component: UnitDetailComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    resolve: {
      unit: UnitResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'unit-edit/:id',
    component: UnitDialogComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    resolve: {
      unit: UnitResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'unit-dept-edit/:id',
    component: UnitDialogComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'unit-dept/:id',
    component: UnitDetailComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'unit-new',
    component: UnitDialogComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.unit.home.title',
      module: 'administrative',
      menu: 'units'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'departments',
    component: DepartmentsComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'arthaApp.department.home.title',
      module: 'administrative',
      menu: 'departments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'users',
    component: UsersComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'userManagement.home.title',
      module: 'administrative',
      menu: 'users'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'user/:id',
    component: UserDetailComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'userManagement.home.title',
      hideSideMenu: true
    },
    resolve: {
      user: UserResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'index-util',
    component: IndexUtilDialogComponent,
    resolve: {
      indexUtil: IndexUtilDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'indexUtil.home.title',
      module: 'administrative',
      menu: 'index-util'
    }
  },
  {
    path: 'group',
    component: GroupListComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'global.menu.entities.group',
      module: 'administrative',
      menu: 'group'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'group/new',
    component: GroupDialogComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'global.menu.entities.group',
      module: 'administrative',
      menu: 'group'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'group/:id/edit',
    component: GroupDialogComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'global.menu.entities.group',
      module: 'administrative',
      menu: 'group'
    },
    resolve: {
      group: GroupDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    // path: 'group/detail/:id',
    path: 'group/:id',
    component: GroupDetailComponent,
    data: {
     // authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'global.menu.entities.group',
      module: 'administrative',
      menu: 'group'
    },
    resolve: {
      group: GroupDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  // role routes
  ...RoleConstant.ROLE_ROUTES
];
