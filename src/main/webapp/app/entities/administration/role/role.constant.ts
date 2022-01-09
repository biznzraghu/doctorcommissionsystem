import {
  RoleComponent,
  NewRoleComponent,
  AssignedPrivilegesComponent,
  ModuleFeaturesModalComponent,
  RoleDetailComponent,
  RolePrivilegesSearchComponent
} from '..';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { RoleDataResolver } from './resolver/role-data.resolver';
import { AssignedRolesComponent } from './assigned-roles/assigned-roles.component';

export const ROLES_COMPONENTS = [
  RoleComponent,
  NewRoleComponent,
  AssignedPrivilegesComponent,
  ModuleFeaturesModalComponent,
  RoleDetailComponent,
  AssignedRolesComponent,
  RolePrivilegesSearchComponent
];

export const ROLES_ENTRY_COMPONENTS = [ModuleFeaturesModalComponent, RolePrivilegesSearchComponent];

export const ROLE_ROUTES = [
  {
    path: 'role',
    component: RoleComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'gatewayApp.role.home.title',
      module: 'administrative',
      menu: 'role'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new-role',
    component: NewRoleComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'gatewayApp.role.home.title',
      hideSideMenu: true
    },
    resolve: {
      role: RoleDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'role/:id',
    component: RoleDetailComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'gatewayApp.role.home.title',
      module: 'administration',
      menu: 'role'
    },
    resolve: {
      role: RoleDataResolver
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'role/:id/edit',
    component: NewRoleComponent,
    resolve: {
      role: RoleDataResolver
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'gatewayApp.role.home.title',
      module: 'administration',
      menu: 'role'
    },
    canActivate: [UserRouteAccessService]
  }
];
