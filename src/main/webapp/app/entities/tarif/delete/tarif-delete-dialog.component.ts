import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITarif } from '../tarif.model';
import { TarifService } from '../service/tarif.service';

@Component({
  templateUrl: './tarif-delete-dialog.component.html',
})
export class TarifDeleteDialogComponent {
  tarif?: ITarif;

  constructor(protected tarifService: TarifService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tarifService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
