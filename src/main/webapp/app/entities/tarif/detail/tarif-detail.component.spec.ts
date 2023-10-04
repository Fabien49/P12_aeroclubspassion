import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TarifDetailComponent } from './tarif-detail.component';

describe('Tarif Management Detail Component', () => {
  let comp: TarifDetailComponent;
  let fixture: ComponentFixture<TarifDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TarifDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tarif: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TarifDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TarifDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tarif on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tarif).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
