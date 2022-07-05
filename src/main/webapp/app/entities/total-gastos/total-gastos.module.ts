import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TotalGastosComponent } from './list/total-gastos.component';
import { TotalGastosDetailComponent } from './detail/total-gastos-detail.component';
import { TotalGastosUpdateComponent } from './update/total-gastos-update.component';
import { TotalGastosDeleteDialogComponent } from './delete/total-gastos-delete-dialog.component';
import { TotalGastosRoutingModule } from './route/total-gastos-routing.module';

@NgModule({
  imports: [SharedModule, TotalGastosRoutingModule],
  declarations: [TotalGastosComponent, TotalGastosDetailComponent, TotalGastosUpdateComponent, TotalGastosDeleteDialogComponent],
  entryComponents: [TotalGastosDeleteDialogComponent],
})
export class TotalGastosModule {}
