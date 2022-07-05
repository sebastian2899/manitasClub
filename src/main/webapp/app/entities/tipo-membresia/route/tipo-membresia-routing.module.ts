import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoMembresiaComponent } from '../list/tipo-membresia.component';
import { TipoMembresiaDetailComponent } from '../detail/tipo-membresia-detail.component';
import { TipoMembresiaUpdateComponent } from '../update/tipo-membresia-update.component';
import { TipoMembresiaRoutingResolveService } from './tipo-membresia-routing-resolve.service';

const tipoMembresiaRoute: Routes = [
  {
    path: '',
    component: TipoMembresiaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoMembresiaDetailComponent,
    resolve: {
      tipoMembresia: TipoMembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoMembresiaUpdateComponent,
    resolve: {
      tipoMembresia: TipoMembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoMembresiaUpdateComponent,
    resolve: {
      tipoMembresia: TipoMembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoMembresiaRoute)],
  exports: [RouterModule],
})
export class TipoMembresiaRoutingModule {}
