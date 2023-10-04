import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtelierDetailComponent } from './atelier-detail.component';

describe('Atelier Management Detail Component', () => {
  let comp: AtelierDetailComponent;
  let fixture: ComponentFixture<AtelierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AtelierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ atelier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AtelierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AtelierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load atelier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.atelier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
