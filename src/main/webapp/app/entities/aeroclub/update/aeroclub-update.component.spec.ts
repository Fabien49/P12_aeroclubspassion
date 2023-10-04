jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AeroclubService } from '../service/aeroclub.service';
import { IAeroclub, Aeroclub } from '../aeroclub.model';

import { AeroclubUpdateComponent } from './aeroclub-update.component';

describe('Aeroclub Management Update Component', () => {
  let comp: AeroclubUpdateComponent;
  let fixture: ComponentFixture<AeroclubUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aeroclubService: AeroclubService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AeroclubUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AeroclubUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AeroclubUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aeroclubService = TestBed.inject(AeroclubService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aeroclub: IAeroclub = { id: 456 };

      activatedRoute.data = of({ aeroclub });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aeroclub));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aeroclub>>();
      const aeroclub = { id: 123 };
      jest.spyOn(aeroclubService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aeroclub });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aeroclub }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aeroclubService.update).toHaveBeenCalledWith(aeroclub);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aeroclub>>();
      const aeroclub = new Aeroclub();
      jest.spyOn(aeroclubService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aeroclub });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aeroclub }));
      saveSubject.complete();

      // THEN
      expect(aeroclubService.create).toHaveBeenCalledWith(aeroclub);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aeroclub>>();
      const aeroclub = { id: 123 };
      jest.spyOn(aeroclubService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aeroclub });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aeroclubService.update).toHaveBeenCalledWith(aeroclub);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
