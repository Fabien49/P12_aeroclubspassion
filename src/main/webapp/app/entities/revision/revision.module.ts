import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RevisionComponent } from './list/revision.component';
import { RevisionDetailComponent } from './detail/revision-detail.component';
import { RevisionUpdateComponent } from './update/revision-update.component';
import { RevisionDeleteDialogComponent } from './delete/revision-delete-dialog.component';
import { RevisionRoutingModule } from './route/revision-routing.module';

@NgModule({
  imports: [SharedModule, RevisionRoutingModule],
  declarations: [RevisionComponent, RevisionDetailComponent, RevisionUpdateComponent, RevisionDeleteDialogComponent],
  entryComponents: [RevisionDeleteDialogComponent],
})
export class RevisionModule {}
