import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ninio',
        data: { pageTitle: 'manitasclubApp.ninio.home.title' },
        loadChildren: () => import('./ninio/ninio.module').then(m => m.NinioModule),
      },
      {
        path: 'acudiente',
        data: { pageTitle: 'manitasclubApp.acudiente.home.title' },
        loadChildren: () => import('./acudiente/acudiente.module').then(m => m.AcudienteModule),
      },
      {
        path: 'tipo-membresia',
        data: { pageTitle: 'manitasclubApp.tipoMembresia.home.title' },
        loadChildren: () => import('./tipo-membresia/tipo-membresia.module').then(m => m.TipoMembresiaModule),
      },
      {
        path: 'membresia',
        data: { pageTitle: 'manitasclubApp.membresia.home.title' },
        loadChildren: () => import('./membresia/membresia.module').then(m => m.MembresiaModule),
      },
      {
        path: 'registro-diario',
        data: { pageTitle: 'manitasclubApp.registroDiario.home.title' },
        loadChildren: () => import('./registro-diario/registro-diario.module').then(m => m.RegistroDiarioModule),
      },
      {
        path: 'caja',
        data: { pageTitle: 'manitasclubApp.caja.home.title' },
        loadChildren: () => import('./caja/caja.module').then(m => m.CajaModule),
      },
      {
        path: 'gastos',
        data: { pageTitle: 'manitasclubApp.gastos.home.title' },
        loadChildren: () => import('./gastos/gastos.module').then(m => m.GastosModule),
      },
      {
        path: 'total-gastos',
        data: { pageTitle: 'manitasclubApp.totalGastos.home.title' },
        loadChildren: () => import('./total-gastos/total-gastos.module').then(m => m.TotalGastosModule),
      },
      {
        path: 'abono',
        data: { pageTitle: 'manitasclubApp.abono.home.title' },
        loadChildren: () => import('./abono/abono.module').then(m => m.AbonoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
