import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGastos, getGastosIdentifier } from '../gastos.model';

export type EntityResponseType = HttpResponse<IGastos>;
export type EntityArrayResponseType = HttpResponse<IGastos[]>;

@Injectable({ providedIn: 'root' })
export class GastosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gastos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gastos: IGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gastos);
    return this.http
      .post<IGastos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(gastos: IGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gastos);
    return this.http
      .put<IGastos>(`${this.resourceUrl}/${getGastosIdentifier(gastos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(gastos: IGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gastos);
    return this.http
      .patch<IGastos>(`${this.resourceUrl}/${getGastosIdentifier(gastos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGastos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGastos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGastosToCollectionIfMissing(gastosCollection: IGastos[], ...gastosToCheck: (IGastos | null | undefined)[]): IGastos[] {
    const gastos: IGastos[] = gastosToCheck.filter(isPresent);
    if (gastos.length > 0) {
      const gastosCollectionIdentifiers = gastosCollection.map(gastosItem => getGastosIdentifier(gastosItem)!);
      const gastosToAdd = gastos.filter(gastosItem => {
        const gastosIdentifier = getGastosIdentifier(gastosItem);
        if (gastosIdentifier == null || gastosCollectionIdentifiers.includes(gastosIdentifier)) {
          return false;
        }
        gastosCollectionIdentifiers.push(gastosIdentifier);
        return true;
      });
      return [...gastosToAdd, ...gastosCollection];
    }
    return gastosCollection;
  }

  protected convertDateFromClient(gastos: IGastos): IGastos {
    return Object.assign({}, gastos, {
      fechaCreacion: gastos.fechaCreacion?.isValid() ? gastos.fechaCreacion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((gastos: IGastos) => {
        gastos.fechaCreacion = gastos.fechaCreacion ? dayjs(gastos.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
