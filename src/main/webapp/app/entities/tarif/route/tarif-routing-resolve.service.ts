import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITarif, Tarif } from '../tarif.model';
import { TarifService } from '../service/tarif.service';

@Injectable({ providedIn: 'root' })
export class TarifRoutingResolveService implements Resolve<ITarif> {
  constructor(protected service: TarifService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITarif> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tarif: HttpResponse<Tarif>) => {
          if (tarif.body) {
            return of(tarif.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tarif());
  }
}
