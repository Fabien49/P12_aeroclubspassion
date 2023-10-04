import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AeroclubDetailComponent } from './aeroclub-detail.component';

describe('Aeroclub Management Detail Component', () => {
  let comp: AeroclubDetailComponent;
  let fixture: ComponentFixture<AeroclubDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AeroclubDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aeroclub: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AeroclubDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AeroclubDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aeroclub on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aeroclub).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
