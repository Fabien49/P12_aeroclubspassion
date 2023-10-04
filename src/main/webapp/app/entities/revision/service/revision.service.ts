import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRevision, getRevisionIdentifier } from '../revision.model';

export type EntityResponseType = HttpResponse<IRevision>;
export type EntityArrayResponseType = HttpResponse<IRevision[]>;

@Injectable({ providedIn: 'root' })
export class RevisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/revisions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(revision: IRevision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(revision);
    return this.http
      .post<IRevision>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(revision: IRevision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(revision);
    return this.http
      .put<IRevision>(`${this.resourceUrl}/${getRevisionIdentifier(revision) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(revision: IRevision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(revision);
    return this.http
      .patch<IRevision>(`${this.resourceUrl}/${getRevisionIdentifier(revision) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRevision>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRevision[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRevisionToCollectionIfMissing(revisionCollection: IRevision[], ...revisionsToCheck: (IRevision | null | undefined)[]): IRevision[] {
    const revisions: IRevision[] = revisionsToCheck.filter(isPresent);
    if (revisions.length > 0) {
      const revisionCollectionIdentifiers = revisionCollection.map(revisionItem => getRevisionIdentifier(revisionItem)!);
      const revisionsToAdd = revisions.filter(revisionItem => {
        const revisionIdentifier = getRevisionIdentifier(revisionItem);
        if (revisionIdentifier == null || revisionCollectionIdentifiers.includes(revisionIdentifier)) {
          return false;
        }
        revisionCollectionIdentifiers.push(revisionIdentifier);
        return true;
      });
      return [...revisionsToAdd, ...revisionCollection];
    }
    return revisionCollection;
  }

  protected convertDateFromClient(revision: IRevision): IRevision {
    return Object.assign({}, revision, {
      date: revision.date?.isValid() ? revision.date.toJSON() : undefined,
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
      res.body.forEach((revision: IRevision) => {
        revision.date = revision.date ? dayjs(revision.date) : undefined;
      });
    }
    return res;
  }
}
