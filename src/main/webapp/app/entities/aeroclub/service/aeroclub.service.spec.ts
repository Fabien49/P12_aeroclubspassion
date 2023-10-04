import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAeroclub, Aeroclub } from '../aeroclub.model';

import { AeroclubService } from './aeroclub.service';

describe('Aeroclub Service', () => {
  let service: AeroclubService;
  let httpMock: HttpTestingController;
  let elemDefault: IAeroclub;
  let expectedResult: IAeroclub | IAeroclub[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AeroclubService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      oaci: 'AAAAAAA',
      name: 'AAAAAAA',
      type: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      mail: 'AAAAAAA',
      adresse: 'AAAAAAA',
      codePostal: 'AAAAAAA',
      commune: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Aeroclub', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Aeroclub()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Aeroclub', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          oaci: 'BBBBBB',
          name: 'BBBBBB',
          type: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          mail: 'BBBBBB',
          adresse: 'BBBBBB',
          codePostal: 'BBBBBB',
          commune: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Aeroclub', () => {
      const patchObject = Object.assign(
        {
          oaci: 'BBBBBB',
          name: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          mail: 'BBBBBB',
          commune: 'BBBBBB',
        },
        new Aeroclub()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Aeroclub', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          oaci: 'BBBBBB',
          name: 'BBBBBB',
          type: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          mail: 'BBBBBB',
          adresse: 'BBBBBB',
          codePostal: 'BBBBBB',
          commune: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Aeroclub', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAeroclubToCollectionIfMissing', () => {
      it('should add a Aeroclub to an empty array', () => {
        const aeroclub: IAeroclub = { id: 123 };
        expectedResult = service.addAeroclubToCollectionIfMissing([], aeroclub);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aeroclub);
      });

      it('should not add a Aeroclub to an array that contains it', () => {
        const aeroclub: IAeroclub = { id: 123 };
        const aeroclubCollection: IAeroclub[] = [
          {
            ...aeroclub,
          },
          { id: 456 },
        ];
        expectedResult = service.addAeroclubToCollectionIfMissing(aeroclubCollection, aeroclub);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Aeroclub to an array that doesn't contain it", () => {
        const aeroclub: IAeroclub = { id: 123 };
        const aeroclubCollection: IAeroclub[] = [{ id: 456 }];
        expectedResult = service.addAeroclubToCollectionIfMissing(aeroclubCollection, aeroclub);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aeroclub);
      });

      it('should add only unique Aeroclub to an array', () => {
        const aeroclubArray: IAeroclub[] = [{ id: 123 }, { id: 456 }, { id: 37836 }];
        const aeroclubCollection: IAeroclub[] = [{ id: 123 }];
        expectedResult = service.addAeroclubToCollectionIfMissing(aeroclubCollection, ...aeroclubArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aeroclub: IAeroclub = { id: 123 };
        const aeroclub2: IAeroclub = { id: 456 };
        expectedResult = service.addAeroclubToCollectionIfMissing([], aeroclub, aeroclub2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aeroclub);
        expect(expectedResult).toContain(aeroclub2);
      });

      it('should accept null and undefined values', () => {
        const aeroclub: IAeroclub = { id: 123 };
        expectedResult = service.addAeroclubToCollectionIfMissing([], null, aeroclub, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aeroclub);
      });

      it('should return initial array if no Aeroclub is added', () => {
        const aeroclubCollection: IAeroclub[] = [{ id: 123 }];
        expectedResult = service.addAeroclubToCollectionIfMissing(aeroclubCollection, undefined, null);
        expect(expectedResult).toEqual(aeroclubCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
