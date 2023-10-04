jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AvionService } from '../service/avion.service';
import { IAvion, Avion } from '../avion.model';
import { IAeroclub } from 'app/entities/aeroclub/aeroclub.model';
import { AeroclubService } from 'app/entities/aeroclub/service/aeroclub.service';

import { AvionUpdateComponent } from './avion-update.component';

describe('Avion Management Update Component', () => {
  let comp: AvionUpdateComponent;
  let fixture: ComponentFixture<AvionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let avionService: AvionService;
  let aeroclubService: AeroclubService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AvionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AvionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    avionService = TestBed.inject(AvionService);
    aeroclubService = TestBed.inject(AeroclubService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Aeroclub query and add missing value', () => {
      const avion: IAvion = { id: 456 };
      const aeroclub: IAeroclub = { id: 82993 };
      avion.aeroclub = aeroclub;

      const aeroclubCollection: IAeroclub[] = [{ id: 58680 }];
      jest.spyOn(aeroclubService, 'query').mockReturnValue(of(new HttpResponse({ body: aeroclubCollection })));
      const additionalAeroclubs = [aeroclub];
      const expectedCollection: IAeroclub[] = [...additionalAeroclubs, ...aeroclubCollection];
      jest.spyOn(aeroclubService, 'addAeroclubToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avion });
      comp.ngOnInit();

      expect(aeroclubService.query).toHaveBeenCalled();
      expect(aeroclubService.addAeroclubToCollectionIfMissing).toHaveBeenCalledWith(aeroclubCollection, ...additionalAeroclubs);
      expect(comp.aeroclubsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const avion: IAvion = { id: 456 };
      const aeroclub: IAeroclub = { id: 21712 };
      avion.aeroclub = aeroclub;

      activatedRoute.data = of({ avion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(avion));
      expect(comp.aeroclubsSharedCollection).toContain(aeroclub);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Avion>>();
      const avion = { id: 123 };
      jest.spyOn(avionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(avionService.update).toHaveBeenCalledWith(avion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Avion>>();
      const avion = new Avion();
      jest.spyOn(avionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avion }));
      saveSubject.complete();

      // THEN
      expect(avionService.create).toHaveBeenCalledWith(avion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Avion>>();
      const avion = { id: 123 };
      jest.spyOn(avionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(avionService.update).toHaveBeenCalledWith(avion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAeroclubById', () => {
      it('Should return tracked Aeroclub primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAeroclubById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
