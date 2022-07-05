import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegistroDiarioComponent } from '../list/registro-diario.component';
import { RegistroDiarioDetailComponent } from '../detail/registro-diario-detail.component';
import { RegistroDiarioUpdateComponent } from '../update/registro-diario-update.component';
import { RegistroDiarioRoutingResolveService } from './registro-diario-routing-resolve.service';

const registroDiarioRoute: Routes = [
  {
    path: '',
    component: RegistroDiarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegistroDiarioDetailComponent,
    resolve: {
      registroDiario: RegistroDiarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegistroDiarioUpdateComponent,
    resolve: {
      registroDiario: RegistroDiarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegistroDiarioUpdateComponent,
    resolve: {
      registroDiario: RegistroDiarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(registroDiarioRoute)],
  exports: [RouterModule],
})
export class RegistroDiarioRoutingModule {}
