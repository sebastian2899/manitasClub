import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GastosComponent } from '../list/gastos.component';
import { GastosDetailComponent } from '../detail/gastos-detail.component';
import { GastosUpdateComponent } from '../update/gastos-update.component';
import { GastosRoutingResolveService } from './gastos-routing-resolve.service';

const gastosRoute: Routes = [
  {
    path: '',
    component: GastosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GastosDetailComponent,
    resolve: {
      gastos: GastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GastosUpdateComponent,
    resolve: {
      gastos: GastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GastosUpdateComponent,
    resolve: {
      gastos: GastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gastosRoute)],
  exports: [RouterModule],
})
export class GastosRoutingModule {}
