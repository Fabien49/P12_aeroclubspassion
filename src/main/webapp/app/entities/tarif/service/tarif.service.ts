import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITarif, getTarifIdentifier } from '../tarif.model';

export type EntityResponseType = HttpResponse<ITarif>;
export type EntityArrayResponseType = HttpResponse<ITarif[]>;

@Injectable({ providedIn: 'root' })
export class TarifService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tarifs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tarif: ITarif): Observable<EntityResponseType> {
    return this.http.post<ITarif>(this.resourceUrl, tarif, { observe: 'response' });
  }

  update(tarif: ITarif): Observable<EntityResponseType> {
    return this.http.put<ITarif>(`${this.resourceUrl}/${getTarifIdentifier(tarif) as number}`, tarif, { observe: 'response' });
  }

  partialUpdate(tarif: ITarif): Observable<EntityResponseType> {
    return this.http.patch<ITarif>(`${this.resourceUrl}/${getTarifIdentifier(tarif) as number}`, tarif, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITarif>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITarif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTarifToCollectionIfMissing(tarifCollection: ITarif[], ...tarifsToCheck: (ITarif | null | undefined)[]): ITarif[] {
    const tarifs: ITarif[] = tarifsToCheck.filter(isPresent);
    if (tarifs.length > 0) {
      const tarifCollectionIdentifiers = tarifCollection.map(tarifItem => getTarifIdentifier(tarifItem)!);
      const tarifsToAdd = tarifs.filter(tarifItem => {
        const tarifIdentifier = getTarifIdentifier(tarifItem);
        if (tarifIdentifier == null || tarifCollectionIdentifiers.includes(tarifIdentifier)) {
          return false;
        }
        tarifCollectionIdentifiers.push(tarifIdentifier);
        return true;
      });
      return [...tarifsToAdd, ...tarifCollection];
    }
    return tarifCollection;
  }
}
