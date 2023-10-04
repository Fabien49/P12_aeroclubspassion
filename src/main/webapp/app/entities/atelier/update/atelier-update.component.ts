import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAtelier, Atelier } from '../atelier.model';
import { AtelierService } from '../service/atelier.service';
import { IAvion } from 'app/entities/avion/avion.model';
import { AvionService } from 'app/entities/avion/service/avion.service';

@Component({
  selector: 'jhi-atelier-update',
  templateUrl: './atelier-update.component.html',
})
export class AtelierUpdateComponent implements OnInit {
  isSaving = false;

  avionsSharedCollection: IAvion[] = [];

  editForm = this.fb.group({
    id: [],
    compteurChgtMoteur: [],
    compteurCarrosserie: [],
    compteurHelisse: [],
    date: [],
    avion: [],
  });

  constructor(
    protected atelierService: AtelierService,
    protected avionService: AvionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atelier }) => {
      if (atelier.id === undefined) {
        const today = dayjs().startOf('day');
        atelier.date = today;
      }

      this.updateForm(atelier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const atelier = this.createFromForm();
    if (atelier.id !== undefined) {
      this.subscribeToSaveResponse(this.atelierService.update(atelier));
    } else {
      this.subscribeToSaveResponse(this.atelierService.create(atelier));
    }
  }

  trackAvionById(index: number, item: IAvion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtelier>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(atelier: IAtelier): void {
    this.editForm.patchValue({
      id: atelier.id,
      compteurChgtMoteur: atelier.compteurChgtMoteur,
      compteurCarrosserie: atelier.compteurCarrosserie,
      compteurHelisse: atelier.compteurHelisse,
      date: atelier.date ? atelier.date.format(DATE_TIME_FORMAT) : null,
      avion: atelier.avion,
    });

    this.avionsSharedCollection = this.avionService.addAvionToCollectionIfMissing(this.avionsSharedCollection, atelier.avion);
  }

  protected loadRelationshipsOptions(): void {
    this.avionService
      .query()
      .pipe(map((res: HttpResponse<IAvion[]>) => res.body ?? []))
      .pipe(map((avions: IAvion[]) => this.avionService.addAvionToCollectionIfMissing(avions, this.editForm.get('avion')!.value)))
      .subscribe((avions: IAvion[]) => (this.avionsSharedCollection = avions));
  }

  protected createFromForm(): IAtelier {
    return {
      ...new Atelier(),
      id: this.editForm.get(['id'])!.value,
      compteurChgtMoteur: this.editForm.get(['compteurChgtMoteur'])!.value,
      compteurCarrosserie: this.editForm.get(['compteurCarrosserie'])!.value,
      compteurHelisse: this.editForm.get(['compteurHelisse'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      avion: this.editForm.get(['avion'])!.value,
    };
  }
}
