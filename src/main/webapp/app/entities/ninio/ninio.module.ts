import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NinioComponent } from './list/ninio.component';
import { NinioDetailComponent } from './detail/ninio-detail.component';
import { NinioUpdateComponent } from './update/ninio-update.component';
import { NinioDeleteDialogComponent } from './delete/ninio-delete-dialog.component';
import { NinioRoutingModule } from './route/ninio-routing.module';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  imports: [SharedModule, NinioRoutingModule, MatIconModule, MatAutocompleteModule, MatFormFieldModule],
  declarations: [NinioComponent, NinioDetailComponent, NinioUpdateComponent, NinioDeleteDialogComponent],
  entryComponents: [NinioDeleteDialogComponent],
})
export class NinioModule {}
