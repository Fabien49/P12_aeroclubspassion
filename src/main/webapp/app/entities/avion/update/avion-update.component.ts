import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAvion, Avion } from '../avion.model';
import { AvionService } from '../service/avion.service';
import { IAeroclub } from 'app/entities/aeroclub/aeroclub.model';
import { AeroclubService } from 'app/entities/aeroclub/service/aeroclub.service';

@Component({
  selector: 'jhi-avion-update',
  templateUrl: './avion-update.component.html',
})
export class AvionUpdateComponent implements OnInit {
  isSaving = false;

  aeroclubsSharedCollection: IAeroclub[] = [];

  editForm = this.fb.group({
    id: [],
    marque: [null, [Validators.required]],
    type: [],
    moteur: [],
    puissance: [],
    place: [],
    autonomie: [],
    usage: [],
    heures: [],
    aeroclub: [],
  });

  constructor(
    protected avionService: AvionService,
    protected aeroclubService: AeroclubService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avion }) => {
      this.updateForm(avion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avion = this.createFromForm();
    if (avion.id !== undefined) {
      this.subscribeToSaveResponse(this.avionService.update(avion));
    } else {
      this.subscribeToSaveResponse(this.avionService.create(avion));
    }
  }

  trackAeroclubById(index: number, item: IAeroclub): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvion>>): void {
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

  protected updateForm(avion: IAvion): void {
    this.editForm.patchValue({
      id: avion.id,
      marque: avion.marque,
      type: avion.type,
      moteur: avion.moteur,
      puissance: avion.puissance,
      place: avion.place,
      autonomie: avion.autonomie,
      usage: avion.usage,
      heures: avion.heures,
      aeroclub: avion.aeroclub,
    });

    this.aeroclubsSharedCollection = this.aeroclubService.addAeroclubToCollectionIfMissing(this.aeroclubsSharedCollection, avion.aeroclub);
  }

  protected loadRelationshipsOptions(): void {
    this.aeroclubService
      .query()
      .pipe(map((res: HttpResponse<IAeroclub[]>) => res.body ?? []))
      .pipe(
        map((aeroclubs: IAeroclub[]) =>
          this.aeroclubService.addAeroclubToCollectionIfMissing(aeroclubs, this.editForm.get('aeroclub')!.value)
        )
      )
      .subscribe((aeroclubs: IAeroclub[]) => (this.aeroclubsSharedCollection = aeroclubs));
  }

  protected createFromForm(): IAvion {
    return {
      ...new Avion(),
      id: this.editForm.get(['id'])!.value,
      marque: this.editForm.get(['marque'])!.value,
      type: this.editForm.get(['type'])!.value,
      moteur: this.editForm.get(['moteur'])!.value,
      puissance: this.editForm.get(['puissance'])!.value,
      place: this.editForm.get(['place'])!.value,
      autonomie: this.editForm.get(['autonomie'])!.value,
      usage: this.editForm.get(['usage'])!.value,
      heures: this.editForm.get(['heures'])!.value,
      aeroclub: this.editForm.get(['aeroclub'])!.value,
    };
  }
}
