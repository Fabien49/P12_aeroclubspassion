import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAtelier, getAtelierIdentifier } from '../atelier.model';

export type EntityResponseType = HttpResponse<IAtelier>;
export type EntityArrayResponseType = HttpResponse<IAtelier[]>;

@Injectable({ providedIn: 'root' })
export class AtelierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ateliers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(atelier: IAtelier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atelier);
    return this.http
      .post<IAtelier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(atelier: IAtelier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atelier);
    return this.http
      .put<IAtelier>(`${this.resourceUrl}/${getAtelierIdentifier(atelier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(atelier: IAtelier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(atelier);
    return this.http
      .patch<IAtelier>(`${this.resourceUrl}/${getAtelierIdentifier(atelier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAtelier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAtelier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAtelierToCollectionIfMissing(atelierCollection: IAtelier[], ...ateliersToCheck: (IAtelier | null | undefined)[]): IAtelier[] {
    const ateliers: IAtelier[] = ateliersToCheck.filter(isPresent);
    if (ateliers.length > 0) {
      const atelierCollectionIdentifiers = atelierCollection.map(atelierItem => getAtelierIdentifier(atelierItem)!);
      const ateliersToAdd = ateliers.filter(atelierItem => {
        const atelierIdentifier = getAtelierIdentifier(atelierItem);
        if (atelierIdentifier == null || atelierCollectionIdentifiers.includes(atelierIdentifier)) {
          return false;
        }
        atelierCollectionIdentifiers.push(atelierIdentifier);
        return true;
      });
      return [...ateliersToAdd, ...atelierCollection];
    }
    return atelierCollection;
  }

  protected convertDateFromClient(atelier: IAtelier): IAtelier {
    return Object.assign({}, atelier, {
      date: atelier.date?.isValid() ? atelier.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((atelier: IAtelier) => {
        atelier.date = atelier.date ? dayjs(atelier.date) : undefined;
      });
    }
    return res;
  }
}
