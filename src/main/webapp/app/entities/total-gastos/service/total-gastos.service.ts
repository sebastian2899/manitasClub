import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITotalGastos, getTotalGastosIdentifier } from '../total-gastos.model';

export type EntityResponseType = HttpResponse<ITotalGastos>;
export type EntityArrayResponseType = HttpResponse<ITotalGastos[]>;

@Injectable({ providedIn: 'root' })
export class TotalGastosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/total-gastos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(totalGastos: ITotalGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(totalGastos);
    return this.http
      .post<ITotalGastos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(totalGastos: ITotalGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(totalGastos);
    return this.http
      .put<ITotalGastos>(`${this.resourceUrl}/${getTotalGastosIdentifier(totalGastos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(totalGastos: ITotalGastos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(totalGastos);
    return this.http
      .patch<ITotalGastos>(`${this.resourceUrl}/${getTotalGastosIdentifier(totalGastos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITotalGastos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITotalGastos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTotalGastosToCollectionIfMissing(
    totalGastosCollection: ITotalGastos[],
    ...totalGastosToCheck: (ITotalGastos | null | undefined)[]
  ): ITotalGastos[] {
    const totalGastos: ITotalGastos[] = totalGastosToCheck.filter(isPresent);
    if (totalGastos.length > 0) {
      const totalGastosCollectionIdentifiers = totalGastosCollection.map(totalGastosItem => getTotalGastosIdentifier(totalGastosItem)!);
      const totalGastosToAdd = totalGastos.filter(totalGastosItem => {
        const totalGastosIdentifier = getTotalGastosIdentifier(totalGastosItem);
        if (totalGastosIdentifier == null || totalGastosCollectionIdentifiers.includes(totalGastosIdentifier)) {
          return false;
        }
        totalGastosCollectionIdentifiers.push(totalGastosIdentifier);
        return true;
      });
      return [...totalGastosToAdd, ...totalGastosCollection];
    }
    return totalGastosCollection;
  }

  protected convertDateFromClient(totalGastos: ITotalGastos): ITotalGastos {
    return Object.assign({}, totalGastos, {
      fechaCreacion: totalGastos.fechaCreacion?.isValid() ? totalGastos.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((totalGastos: ITotalGastos) => {
        totalGastos.fechaCreacion = totalGastos.fechaCreacion ? dayjs(totalGastos.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
