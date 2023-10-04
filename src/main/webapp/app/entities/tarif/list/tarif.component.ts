import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITarif } from '../tarif.model';
import { TarifService } from '../service/tarif.service';
import { TarifDeleteDialogComponent } from '../delete/tarif-delete-dialog.component';

@Component({
  selector: 'jhi-tarif',
  templateUrl: './tarif.component.html',
})
export class TarifComponent implements OnInit {
  tarifs?: ITarif[];
  isLoading = false;

  constructor(protected tarifService: TarifService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tarifService.query().subscribe(
      (res: HttpResponse<ITarif[]>) => {
        this.isLoading = false;
        this.tarifs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITarif): number {
    return item.id!;
  }

  delete(tarif: ITarif): void {
    const modalRef = this.modalService.open(TarifDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tarif = tarif;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
