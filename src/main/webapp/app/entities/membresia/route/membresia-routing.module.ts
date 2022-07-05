import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MembresiaComponent } from '../list/membresia.component';
import { MembresiaDetailComponent } from '../detail/membresia-detail.component';
import { MembresiaUpdateComponent } from '../update/membresia-update.component';
import { MembresiaRoutingResolveService } from './membresia-routing-resolve.service';

const membresiaRoute: Routes = [
  {
    path: '',
    component: MembresiaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MembresiaDetailComponent,
    resolve: {
      membresia: MembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MembresiaUpdateComponent,
    resolve: {
      membresia: MembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MembresiaUpdateComponent,
    resolve: {
      membresia: MembresiaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(membresiaRoute)],
  exports: [RouterModule],
})
export class MembresiaRoutingModule {}
