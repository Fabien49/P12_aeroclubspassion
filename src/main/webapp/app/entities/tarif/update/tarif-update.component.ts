import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITarif, Tarif } from '../tarif.model';
import { TarifService } from '../service/tarif.service';
import { IAeroclub } from 'app/entities/aeroclub/aeroclub.model';
import { AeroclubService } from 'app/entities/aeroclub/service/aeroclub.service';

@Component({
  selector: 'jhi-tarif-update',
  templateUrl: './tarif-update.component.html',
})
export class TarifUpdateComponent implements OnInit {
  isSaving = false;

  aeroclubsCollection: IAeroclub[] = [];

  editForm = this.fb.group({
    id: [],
    taxeAtterrissage: [],
    taxeParking: [],
    carburant: [],
    aeroclub: [],
  });

  constructor(
    protected tarifService: TarifService,
    protected aeroclubService: AeroclubService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarif }) => {
      this.updateForm(tarif);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tarif = this.createFromForm();
    if (tarif.id !== undefined) {
      this.subscribeToSaveResponse(this.tarifService.update(tarif));
    } else {
      this.subscribeToSaveResponse(this.tarifService.create(tarif));
    }
  }

  trackAeroclubById(index: number, item: IAeroclub): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITarif>>): void {
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

  protected updateForm(tarif: ITarif): void {
    this.editForm.patchValue({
      id: tarif.id,
      taxeAtterrissage: tarif.taxeAtterrissage,
      taxeParking: tarif.taxeParking,
      carburant: tarif.carburant,
      aeroclub: tarif.aeroclub,
    });

    this.aeroclubsCollection = this.aeroclubService.addAeroclubToCollectionIfMissing(this.aeroclubsCollection, tarif.aeroclub);
  }

  protected loadRelationshipsOptions(): void {
    this.aeroclubService
      .query({ filter: 'tarif-is-null' })
      .pipe(map((res: HttpResponse<IAeroclub[]>) => res.body ?? []))
      .pipe(
        map((aeroclubs: IAeroclub[]) =>
          this.aeroclubService.addAeroclubToCollectionIfMissing(aeroclubs, this.editForm.get('aeroclub')!.value)
        )
      )
      .subscribe((aeroclubs: IAeroclub[]) => (this.aeroclubsCollection = aeroclubs));
  }

  protected createFromForm(): ITarif {
    return {
      ...new Tarif(),
      id: this.editForm.get(['id'])!.value,
      taxeAtterrissage: this.editForm.get(['taxeAtterrissage'])!.value,
      taxeParking: this.editForm.get(['taxeParking'])!.value,
      carburant: this.editForm.get(['carburant'])!.value,
      aeroclub: this.editForm.get(['aeroclub'])!.value,
    };
  }
}
