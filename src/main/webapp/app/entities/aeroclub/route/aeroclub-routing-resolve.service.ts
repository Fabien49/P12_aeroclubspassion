import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAeroclub, Aeroclub } from '../aeroclub.model';
import { AeroclubService } from '../service/aeroclub.service';

@Injectable({ providedIn: 'root' })
export class AeroclubRoutingResolveService implements Resolve<IAeroclub> {
  constructor(protected service: AeroclubService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAeroclub> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aeroclub: HttpResponse<Aeroclub>) => {
          if (aeroclub.body) {
            return of(aeroclub.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Aeroclub());
  }
}
