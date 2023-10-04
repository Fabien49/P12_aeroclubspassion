jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITarif, Tarif } from '../tarif.model';
import { TarifService } from '../service/tarif.service';

import { TarifRoutingResolveService } from './tarif-routing-resolve.service';

describe('Tarif routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TarifRoutingResolveService;
  let service: TarifService;
  let resultTarif: ITarif | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TarifRoutingResolveService);
    service = TestBed.inject(TarifService);
    resultTarif = undefined;
  });

  describe('resolve', () => {
    it('should return ITarif returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTarif = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTarif).toEqual({ id: 123 });
    });

    it('should return new ITarif if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTarif = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTarif).toEqual(new Tarif());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tarif })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTarif = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTarif).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
