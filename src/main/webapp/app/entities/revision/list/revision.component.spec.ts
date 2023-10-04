import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RevisionService } from '../service/revision.service';

import { RevisionComponent } from './revision.component';

describe('Revision Management Component', () => {
  let comp: RevisionComponent;
  let fixture: ComponentFixture<RevisionComponent>;
  let service: RevisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RevisionComponent],
    })
      .overrideTemplate(RevisionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RevisionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RevisionService);

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
    expect(comp.revisions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
