import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAeroclub, Aeroclub } from '../aeroclub.model';
import { AeroclubService } from '../service/aeroclub.service';

@Component({
  selector: 'jhi-aeroclub-update',
  templateUrl: './aeroclub-update.component.html',
})
export class AeroclubUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    oaci: [null, [Validators.required]],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    mail: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    codePostal: [null, [Validators.required]],
    commune: [null, [Validators.required]],
  });

  constructor(protected aeroclubService: AeroclubService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aeroclub }) => {
      this.updateForm(aeroclub);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aeroclub = this.createFromForm();
    if (aeroclub.id !== undefined) {
      this.subscribeToSaveResponse(this.aeroclubService.update(aeroclub));
    } else {
      this.subscribeToSaveResponse(this.aeroclubService.create(aeroclub));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAeroclub>>): void {
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

  protected updateForm(aeroclub: IAeroclub): void {
    this.editForm.patchValue({
      id: aeroclub.id,
      oaci: aeroclub.oaci,
      name: aeroclub.name,
      type: aeroclub.type,
      phoneNumber: aeroclub.phoneNumber,
      mail: aeroclub.mail,
      adresse: aeroclub.adresse,
      codePostal: aeroclub.codePostal,
      commune: aeroclub.commune,
    });
  }

  protected createFromForm(): IAeroclub {
    return {
      ...new Aeroclub(),
      id: this.editForm.get(['id'])!.value,
      oaci: this.editForm.get(['oaci'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      commune: this.editForm.get(['commune'])!.value,
    };
  }
}
