import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRevision, Revision } from '../revision.model';

import { RevisionService } from './revision.service';

describe('Revision Service', () => {
  let service: RevisionService;
  let httpMock: HttpTestingController;
  let elemDefault: IRevision;
  let expectedResult: IRevision | IRevision[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RevisionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      niveaux: false,
      pression: false,
      carroserie: false,
      date: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Revision', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Revision()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Revision', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          niveaux: true,
          pression: true,
          carroserie: true,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Revision', () => {
      const patchObject = Object.assign(
        {
          pression: true,
        },
        new Revision()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Revision', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          niveaux: true,
          pression: true,
          carroserie: true,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Revision', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRevisionToCollectionIfMissing', () => {
      it('should add a Revision to an empty array', () => {
        const revision: IRevision = { id: 123 };
        expectedResult = service.addRevisionToCollectionIfMissing([], revision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(revision);
      });

      it('should not add a Revision to an array that contains it', () => {
        const revision: IRevision = { id: 123 };
        const revisionCollection: IRevision[] = [
          {
            ...revision,
          },
          { id: 456 },
        ];
        expectedResult = service.addRevisionToCollectionIfMissing(revisionCollection, revision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Revision to an array that doesn't contain it", () => {
        const revision: IRevision = { id: 123 };
        const revisionCollection: IRevision[] = [{ id: 456 }];
        expectedResult = service.addRevisionToCollectionIfMissing(revisionCollection, revision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(revision);
      });

      it('should add only unique Revision to an array', () => {
        const revisionArray: IRevision[] = [{ id: 123 }, { id: 456 }, { id: 21575 }];
        const revisionCollection: IRevision[] = [{ id: 123 }];
        expectedResult = service.addRevisionToCollectionIfMissing(revisionCollection, ...revisionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const revision: IRevision = { id: 123 };
        const revision2: IRevision = { id: 456 };
        expectedResult = service.addRevisionToCollectionIfMissing([], revision, revision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(revision);
        expect(expectedResult).toContain(revision2);
      });

      it('should accept null and undefined values', () => {
        const revision: IRevision = { id: 123 };
        expectedResult = service.addRevisionToCollectionIfMissing([], null, revision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(revision);
      });

      it('should return initial array if no Revision is added', () => {
        const revisionCollection: IRevision[] = [{ id: 123 }];
        expectedResult = service.addRevisionToCollectionIfMissing(revisionCollection, undefined, null);
        expect(expectedResult).toEqual(revisionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
