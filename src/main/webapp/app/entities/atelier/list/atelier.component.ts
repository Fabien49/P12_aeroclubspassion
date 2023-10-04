import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAtelier } from '../atelier.model';
import { AtelierService } from '../service/atelier.service';
import { AtelierDeleteDialogComponent } from '../delete/atelier-delete-dialog.component';

@Component({
  selector: 'jhi-atelier',
  templateUrl: './atelier.component.html',
})
export class AtelierComponent implements OnInit {
  ateliers?: IAtelier[];
  isLoading = false;

  constructor(protected atelierService: AtelierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.atelierService.query().subscribe(
      (res: HttpResponse<IAtelier[]>) => {
        this.isLoading = false;
        this.ateliers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAtelier): number {
    return item.id!;
  }

  delete(atelier: IAtelier): void {
    const modalRef = this.modalService.open(AtelierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.atelier = atelier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
