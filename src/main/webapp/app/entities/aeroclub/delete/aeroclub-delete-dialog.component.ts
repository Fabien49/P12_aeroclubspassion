import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAeroclub } from '../aeroclub.model';
import { AeroclubService } from '../service/aeroclub.service';

@Component({
  templateUrl: './aeroclub-delete-dialog.component.html',
})
export class AeroclubDeleteDialogComponent {
  aeroclub?: IAeroclub;

  constructor(protected aeroclubService: AeroclubService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aeroclubService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
