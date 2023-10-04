import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RevisionDetailComponent } from './revision-detail.component';

describe('Revision Management Detail Component', () => {
  let comp: RevisionDetailComponent;
  let fixture: ComponentFixture<RevisionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RevisionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ revision: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RevisionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RevisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load revision on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.revision).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
