import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TarifService } from '../service/tarif.service';

import { TarifComponent } from './tarif.component';

describe('Tarif Management Component', () => {
  let comp: TarifComponent;
  let fixture: ComponentFixture<TarifComponent>;
  let service: TarifService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TarifComponent],
    })
      .overrideTemplate(TarifComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TarifComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TarifService);

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
    expect(comp.tarifs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
