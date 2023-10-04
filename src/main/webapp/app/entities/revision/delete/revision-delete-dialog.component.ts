import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRevision } from '../revision.model';
import { RevisionService } from '../service/revision.service';

@Component({
  templateUrl: './revision-delete-dialog.component.html',
})
export class RevisionDeleteDialogComponent {
  revision?: IRevision;

  constructor(protected revisionService: RevisionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.revisionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
