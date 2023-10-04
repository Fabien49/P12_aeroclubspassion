jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AtelierService } from '../service/atelier.service';
import { IAtelier, Atelier } from '../atelier.model';
import { IAvion } from 'app/entities/avion/avion.model';
import { AvionService } from 'app/entities/avion/service/avion.service';

import { AtelierUpdateComponent } from './atelier-update.component';

describe('Atelier Management Update Component', () => {
  let comp: AtelierUpdateComponent;
  let fixture: ComponentFixture<AtelierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let atelierService: AtelierService;
  let avionService: AvionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AtelierUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AtelierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtelierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    atelierService = TestBed.inject(AtelierService);
    avionService = TestBed.inject(AvionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Avion query and add missing value', () => {
      const atelier: IAtelier = { id: 456 };
      const avion: IAvion = { id: 46152 };
      atelier.avion = avion;

      const avionCollection: IAvion[] = [{ id: 23160 }];
      jest.spyOn(avionService, 'query').mockReturnValue(of(new HttpResponse({ body: avionCollection })));
      const additionalAvions = [avion];
      const expectedCollection: IAvion[] = [...additionalAvions, ...avionCollection];
      jest.spyOn(avionService, 'addAvionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ atelier });
      comp.ngOnInit();

      expect(avionService.query).toHaveBeenCalled();
      expect(avionService.addAvionToCollectionIfMissing).toHaveBeenCalledWith(avionCollection, ...additionalAvions);
      expect(comp.avionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const atelier: IAtelier = { id: 456 };
      const avion: IAvion = { id: 54276 };
      atelier.avion = avion;

      activatedRoute.data = of({ atelier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(atelier));
      expect(comp.avionsSharedCollection).toContain(avion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Atelier>>();
      const atelier = { id: 123 };
      jest.spyOn(atelierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atelier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atelier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(atelierService.update).toHaveBeenCalledWith(atelier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Atelier>>();
      const atelier = new Atelier();
      jest.spyOn(atelierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atelier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atelier }));
      saveSubject.complete();

      // THEN
      expect(atelierService.create).toHaveBeenCalledWith(atelier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Atelier>>();
      const atelier = { id: 123 };
      jest.spyOn(atelierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atelier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(atelierService.update).toHaveBeenCalledWith(atelier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAvionById', () => {
      it('Should return tracked Avion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAvionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
