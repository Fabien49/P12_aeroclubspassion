import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AtelierService } from '../service/atelier.service';

import { AtelierComponent } from './atelier.component';

describe('Atelier Management Component', () => {
  let comp: AtelierComponent;
  let fixture: ComponentFixture<AtelierComponent>;
  let service: AtelierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AtelierComponent],
    })
      .overrideTemplate(AtelierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtelierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AtelierService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.ateliers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
