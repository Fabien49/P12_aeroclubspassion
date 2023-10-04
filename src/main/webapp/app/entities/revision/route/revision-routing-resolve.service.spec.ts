jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRevision, Revision } from '../revision.model';
import { RevisionService } from '../service/revision.service';

import { RevisionRoutingResolveService } from './revision-routing-resolve.service';

describe('Revision routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RevisionRoutingResolveService;
  let service: RevisionService;
  let resultRevision: IRevision | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RevisionRoutingResolveService);
    service = TestBed.inject(RevisionService);
    resultRevision = undefined;
  });

  describe('resolve', () => {
    it('should return IRevision returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRevision).toEqual({ id: 123 });
    });

    it('should return new IRevision if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevision = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRevision).toEqual(new Revision());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Revision })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRevision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRevision).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
