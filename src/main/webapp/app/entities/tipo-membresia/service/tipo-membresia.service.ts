import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoMembresia, getTipoMembresiaIdentifier } from '../tipo-membresia.model';

export type EntityResponseType = HttpResponse<ITipoMembresia>;
export type EntityArrayResponseType = HttpResponse<ITipoMembresia[]>;

@Injectable({ providedIn: 'root' })
export class TipoMembresiaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-membresias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoMembresia: ITipoMembresia): Observable<EntityResponseType> {
    return this.http.post<ITipoMembresia>(this.resourceUrl, tipoMembresia, { observe: 'response' });
  }

  update(tipoMembresia: ITipoMembresia): Observable<EntityResponseType> {
    return this.http.put<ITipoMembresia>(`${this.resourceUrl}/${getTipoMembresiaIdentifier(tipoMembresia) as number}`, tipoMembresia, {
      observe: 'response',
    });
  }

  partialUpdate(tipoMembresia: ITipoMembresia): Observable<EntityResponseType> {
    return this.http.patch<ITipoMembresia>(`${this.resourceUrl}/${getTipoMembresiaIdentifier(tipoMembresia) as number}`, tipoMembresia, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoMembresia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoMembresia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoMembresiaToCollectionIfMissing(
    tipoMembresiaCollection: ITipoMembresia[],
    ...tipoMembresiasToCheck: (ITipoMembresia | null | undefined)[]
  ): ITipoMembresia[] {
    const tipoMembresias: ITipoMembresia[] = tipoMembresiasToCheck.filter(isPresent);
    if (tipoMembresias.length > 0) {
      const tipoMembresiaCollectionIdentifiers = tipoMembresiaCollection.map(
        tipoMembresiaItem => getTipoMembresiaIdentifier(tipoMembresiaItem)!
      );
      const tipoMembresiasToAdd = tipoMembresias.filter(tipoMembresiaItem => {
        const tipoMembresiaIdentifier = getTipoMembresiaIdentifier(tipoMembresiaItem);
        if (tipoMembresiaIdentifier == null || tipoMembresiaCollectionIdentifiers.includes(tipoMembresiaIdentifier)) {
          return false;
        }
        tipoMembresiaCollectionIdentifiers.push(tipoMembresiaIdentifier);
        return true;
      });
      return [...tipoMembresiasToAdd, ...tipoMembresiaCollection];
    }
    return tipoMembresiaCollection;
  }
}
