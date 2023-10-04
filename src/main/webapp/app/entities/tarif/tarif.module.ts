import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TarifComponent } from './list/tarif.component';
import { TarifDetailComponent } from './detail/tarif-detail.component';
import { TarifUpdateComponent } from './update/tarif-update.component';
import { TarifDeleteDialogComponent } from './delete/tarif-delete-dialog.component';
import { TarifRoutingModule } from './route/tarif-routing.module';

@NgModule({
  imports: [SharedModule, TarifRoutingModule],
  declarations: [TarifComponent, TarifDetailComponent, TarifUpdateComponent, TarifDeleteDialogComponent],
  entryComponents: [TarifDeleteDialogComponent],
})
export class TarifModule {}
