import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITarif, Tarif } from '../tarif.model';

import { TarifService } from './tarif.service';

describe('Tarif Service', () => {
  let service: TarifService;
  let httpMock: HttpTestingController;
  let elemDefault: ITarif;
  let expectedResult: ITarif | ITarif[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TarifService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      taxeAtterrissage: 0,
      taxeParking: 0,
      carburant: 0,
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

    it('should create a Tarif', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tarif()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tarif', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          taxeAtterrissage: 1,
          taxeParking: 1,
          carburant: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tarif', () => {
      const patchObject = Object.assign(
        {
          taxeAtterrissage: 1,
          taxeParking: 1,
          carburant: 1,
        },
        new Tarif()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tarif', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          taxeAtterrissage: 1,
          taxeParking: 1,
          carburant: 1,
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

    it('should delete a Tarif', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTarifToCollectionIfMissing', () => {
      it('should add a Tarif to an empty array', () => {
        const tarif: ITarif = { id: 123 };
        expectedResult = service.addTarifToCollectionIfMissing([], tarif);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tarif);
      });

      it('should not add a Tarif to an array that contains it', () => {
        const tarif: ITarif = { id: 123 };
        const tarifCollection: ITarif[] = [
          {
            ...tarif,
          },
          { id: 456 },
        ];
        expectedResult = service.addTarifToCollectionIfMissing(tarifCollection, tarif);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tarif to an array that doesn't contain it", () => {
        const tarif: ITarif = { id: 123 };
        const tarifCollection: ITarif[] = [{ id: 456 }];
        expectedResult = service.addTarifToCollectionIfMissing(tarifCollection, tarif);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tarif);
      });

      it('should add only unique Tarif to an array', () => {
        const tarifArray: ITarif[] = [{ id: 123 }, { id: 456 }, { id: 16742 }];
        const tarifCollection: ITarif[] = [{ id: 123 }];
        expectedResult = service.addTarifToCollectionIfMissing(tarifCollection, ...tarifArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tarif: ITarif = { id: 123 };
        const tarif2: ITarif = { id: 456 };
        expectedResult = service.addTarifToCollectionIfMissing([], tarif, tarif2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tarif);
        expect(expectedResult).toContain(tarif2);
      });

      it('should accept null and undefined values', () => {
        const tarif: ITarif = { id: 123 };
        expectedResult = service.addTarifToCollectionIfMissing([], null, tarif, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tarif);
      });

      it('should return initial array if no Tarif is added', () => {
        const tarifCollection: ITarif[] = [{ id: 123 }];
        expectedResult = service.addTarifToCollectionIfMissing(tarifCollection, undefined, null);
        expect(expectedResult).toEqual(tarifCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
