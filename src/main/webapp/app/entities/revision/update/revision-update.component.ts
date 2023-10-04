import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRevision, Revision } from '../revision.model';
import { RevisionService } from '../service/revision.service';
import { IAvion } from 'app/entities/avion/avion.model';
import { AvionService } from 'app/entities/avion/service/avion.service';

@Component({
  selector: 'jhi-revision-update',
  templateUrl: './revision-update.component.html',
})
export class RevisionUpdateComponent implements OnInit {
  isSaving = false;

  avionsCollection: IAvion[] = [];

  editForm = this.fb.group({
    id: [],
    niveaux: [],
    pression: [],
    carroserie: [],
    date: [],
    avion: [],
  });

  constructor(
    protected revisionService: RevisionService,
    protected avionService: AvionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revision }) => {
      if (revision.id === undefined) {
        const today = dayjs().startOf('day');
        revision.date = today;
      }

      this.updateForm(revision);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const revision = this.createFromForm();
    if (revision.id !== undefined) {
      this.subscribeToSaveResponse(this.revisionService.update(revision));
    } else {
      this.subscribeToSaveResponse(this.revisionService.create(revision));
    }
  }

  trackAvionById(index: number, item: IAvion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRevision>>): void {
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

  protected updateForm(revision: IRevision): void {
    this.editForm.patchValue({
      id: revision.id,
      niveaux: revision.niveaux,
      pression: revision.pression,
      carroserie: revision.carroserie,
      date: revision.date ? revision.date.format(DATE_TIME_FORMAT) : null,
      avion: revision.avion,
    });

    this.avionsCollection = this.avionService.addAvionToCollectionIfMissing(this.avionsCollection, revision.avion);
  }

  protected loadRelationshipsOptions(): void {
    this.avionService
      .query({ filter: 'revision-is-null' })
      .pipe(map((res: HttpResponse<IAvion[]>) => res.body ?? []))
      .pipe(map((avions: IAvion[]) => this.avionService.addAvionToCollectionIfMissing(avions, this.editForm.get('avion')!.value)))
      .subscribe((avions: IAvion[]) => (this.avionsCollection = avions));
  }

  protected createFromForm(): IRevision {
    return {
      ...new Revision(),
      id: this.editForm.get(['id'])!.value,
      niveaux: this.editForm.get(['niveaux'])!.value,
      pression: this.editForm.get(['pression'])!.value,
      carroserie: this.editForm.get(['carroserie'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      avion: this.editForm.get(['avion'])!.value,
    };
  }
}
