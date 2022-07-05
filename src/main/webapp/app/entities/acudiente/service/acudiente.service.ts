import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcudiente, getAcudienteIdentifier } from '../acudiente.model';

export type EntityResponseType = HttpResponse<IAcudiente>;
export type EntityArrayResponseType = HttpResponse<IAcudiente[]>;

@Injectable({ providedIn: 'root' })
export class AcudienteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acudientes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acudiente: IAcudiente): Observable<EntityResponseType> {
    return this.http.post<IAcudiente>(this.resourceUrl, acudiente, { observe: 'response' });
  }

  update(acudiente: IAcudiente): Observable<EntityResponseType> {
    return this.http.put<IAcudiente>(`${this.resourceUrl}/${getAcudienteIdentifier(acudiente) as number}`, acudiente, {
      observe: 'response',
    });
  }

  partialUpdate(acudiente: IAcudiente): Observable<EntityResponseType> {
    return this.http.patch<IAcudiente>(`${this.resourceUrl}/${getAcudienteIdentifier(acudiente) as number}`, acudiente, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcudiente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcudiente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAcudienteToCollectionIfMissing(
    acudienteCollection: IAcudiente[],
    ...acudientesToCheck: (IAcudiente | null | undefined)[]
  ): IAcudiente[] {
    const acudientes: IAcudiente[] = acudientesToCheck.filter(isPresent);
    if (acudientes.length > 0) {
      const acudienteCollectionIdentifiers = acudienteCollection.map(acudienteItem => getAcudienteIdentifier(acudienteItem)!);
      const acudientesToAdd = acudientes.filter(acudienteItem => {
        const acudienteIdentifier = getAcudienteIdentifier(acudienteItem);
        if (acudienteIdentifier == null || acudienteCollectionIdentifiers.includes(acudienteIdentifier)) {
          return false;
        }
        acudienteCollectionIdentifiers.push(acudienteIdentifier);
        return true;
      });
      return [...acudientesToAdd, ...acudienteCollection];
    }
    return acudienteCollection;
  }
}
