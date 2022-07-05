import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcudienteComponent } from '../list/acudiente.component';
import { AcudienteDetailComponent } from '../detail/acudiente-detail.component';
import { AcudienteUpdateComponent } from '../update/acudiente-update.component';
import { AcudienteRoutingResolveService } from './acudiente-routing-resolve.service';

const acudienteRoute: Routes = [
  {
    path: '',
    component: AcudienteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcudienteDetailComponent,
    resolve: {
      acudiente: AcudienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcudienteUpdateComponent,
    resolve: {
      acudiente: AcudienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcudienteUpdateComponent,
    resolve: {
      acudiente: AcudienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acudienteRoute)],
  exports: [RouterModule],
})
export class AcudienteRoutingModule {}
