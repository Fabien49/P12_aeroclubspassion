import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReservation, Reservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';
import { IAvion } from 'app/entities/avion/avion.model';
import { AvionService } from 'app/entities/avion/service/avion.service';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html',
})
export class ReservationUpdateComponent implements OnInit {
  isSaving = false;

  avionsCollection: IAvion[] = [];

  editForm = this.fb.group({
    id: [],
    dateEmprunt: [],
    dateRetour: [],
    avion: [],
  });

  constructor(
    protected reservationService: ReservationService,
    protected avionService: AvionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      if (reservation.id === undefined) {
        const today = dayjs().startOf('day');
        reservation.dateEmprunt = today;
        reservation.dateRetour = today;
      }

      this.updateForm(reservation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reservation = this.createFromForm();
    if (reservation.id !== undefined) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  trackAvionById(index: number, item: IAvion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>): void {
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

  protected updateForm(reservation: IReservation): void {
    this.editForm.patchValue({
      id: reservation.id,
      dateEmprunt: reservation.dateEmprunt ? reservation.dateEmprunt.format(DATE_TIME_FORMAT) : null,
      dateRetour: reservation.dateRetour ? reservation.dateRetour.format(DATE_TIME_FORMAT) : null,
      avion: reservation.avion,
    });

    this.avionsCollection = this.avionService.addAvionToCollectionIfMissing(this.avionsCollection, reservation.avion);
  }

  protected loadRelationshipsOptions(): void {
    this.avionService
      .query({ filter: 'reservation-is-null' })
      .pipe(map((res: HttpResponse<IAvion[]>) => res.body ?? []))
      .pipe(map((avions: IAvion[]) => this.avionService.addAvionToCollectionIfMissing(avions, this.editForm.get('avion')!.value)))
      .subscribe((avions: IAvion[]) => (this.avionsCollection = avions));
  }

  protected createFromForm(): IReservation {
    return {
      ...new Reservation(),
      id: this.editForm.get(['id'])!.value,
      dateEmprunt: this.editForm.get(['dateEmprunt'])!.value
        ? dayjs(this.editForm.get(['dateEmprunt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateRetour: this.editForm.get(['dateRetour'])!.value ? dayjs(this.editForm.get(['dateRetour'])!.value, DATE_TIME_FORMAT) : undefined,
      avion: this.editForm.get(['avion'])!.value,
    };
  }
}
