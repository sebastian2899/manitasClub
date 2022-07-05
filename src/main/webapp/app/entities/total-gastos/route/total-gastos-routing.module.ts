import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TotalGastosComponent } from '../list/total-gastos.component';
import { TotalGastosDetailComponent } from '../detail/total-gastos-detail.component';
import { TotalGastosUpdateComponent } from '../update/total-gastos-update.component';
import { TotalGastosRoutingResolveService } from './total-gastos-routing-resolve.service';

const totalGastosRoute: Routes = [
  {
    path: '',
    component: TotalGastosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TotalGastosDetailComponent,
    resolve: {
      totalGastos: TotalGastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TotalGastosUpdateComponent,
    resolve: {
      totalGastos: TotalGastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TotalGastosUpdateComponent,
    resolve: {
      totalGastos: TotalGastosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(totalGastosRoute)],
  exports: [RouterModule],
})
export class TotalGastosRoutingModule {}
