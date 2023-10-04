import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AeroclubComponent } from './list/aeroclub.component';
import { AeroclubDetailComponent } from './detail/aeroclub-detail.component';
import { AeroclubUpdateComponent } from './update/aeroclub-update.component';
import { AeroclubDeleteDialogComponent } from './delete/aeroclub-delete-dialog.component';
import { AeroclubRoutingModule } from './route/aeroclub-routing.module';

@NgModule({
  imports: [SharedModule, AeroclubRoutingModule],
  declarations: [AeroclubComponent, AeroclubDetailComponent, AeroclubUpdateComponent, AeroclubDeleteDialogComponent],
  entryComponents: [AeroclubDeleteDialogComponent],
})
export class AeroclubModule {}
