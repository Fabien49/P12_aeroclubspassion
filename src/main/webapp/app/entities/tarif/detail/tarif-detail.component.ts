import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITarif } from '../tarif.model';

@Component({
  selector: 'jhi-tarif-detail',
  templateUrl: './tarif-detail.component.html',
})
export class TarifDetailComponent implements OnInit {
  tarif: ITarif | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarif }) => {
      this.tarif = tarif;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
