import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRevision, Revision } from '../revision.model';
import { RevisionService } from '../service/revision.service';

@Injectable({ providedIn: 'root' })
export class RevisionRoutingResolveService implements Resolve<IRevision> {
  constructor(protected service: RevisionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRevision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((revision: HttpResponse<Revision>) => {
          if (revision.body) {
            return of(revision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Revision());
  }
}
