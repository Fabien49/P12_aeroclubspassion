import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAeroclub, getAeroclubIdentifier } from '../aeroclub.model';

export type EntityResponseType = HttpResponse<IAeroclub>;
export type EntityArrayResponseType = HttpResponse<IAeroclub[]>;

@Injectable({ providedIn: 'root' })
export class AeroclubService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aeroclubs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aeroclub: IAeroclub): Observable<EntityResponseType> {
    return this.http.post<IAeroclub>(this.resourceUrl, aeroclub, { observe: 'response' });
  }

  update(aeroclub: IAeroclub): Observable<EntityResponseType> {
    return this.http.put<IAeroclub>(`${this.resourceUrl}/${getAeroclubIdentifier(aeroclub) as number}`, aeroclub, { observe: 'response' });
  }

  partialUpdate(aeroclub: IAeroclub): Observable<EntityResponseType> {
    return this.http.patch<IAeroclub>(`${this.resourceUrl}/${getAeroclubIdentifier(aeroclub) as number}`, aeroclub, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAeroclub>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAeroclub[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAeroclubToCollectionIfMissing(aeroclubCollection: IAeroclub[], ...aeroclubsToCheck: (IAeroclub | null | undefined)[]): IAeroclub[] {
    const aeroclubs: IAeroclub[] = aeroclubsToCheck.filter(isPresent);
    if (aeroclubs.length > 0) {
      const aeroclubCollectionIdentifiers = aeroclubCollection.map(aeroclubItem => getAeroclubIdentifier(aeroclubItem)!);
      const aeroclubsToAdd = aeroclubs.filter(aeroclubItem => {
        const aeroclubIdentifier = getAeroclubIdentifier(aeroclubItem);
        if (aeroclubIdentifier == null || aeroclubCollectionIdentifiers.includes(aeroclubIdentifier)) {
          return false;
        }
        aeroclubCollectionIdentifiers.push(aeroclubIdentifier);
        return true;
      });
      return [...aeroclubsToAdd, ...aeroclubCollection];
    }
    return aeroclubCollection;
  }
}
