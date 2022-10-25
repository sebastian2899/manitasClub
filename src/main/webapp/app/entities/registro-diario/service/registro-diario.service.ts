import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegistroDiario, getRegistroDiarioIdentifier } from '../registro-diario.model';

export type EntityResponseType = HttpResponse<IRegistroDiario>;
export type EntityArrayResponseType = HttpResponse<IRegistroDiario[]>;
export type NumberResponseType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class RegistroDiarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/registro-diarios');
  protected ValuePerDatesUrl = this.applicationConfigService.getEndpointFor('api/registro-diarios/consultarValorMes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(registroDiario: IRegistroDiario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDiario);
    return this.http
      .post<IRegistroDiario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  consultarValorMes(fechaInicio: string, fechaFin: string): Observable<NumberResponseType> {
    return this.http.get<number>(`${this.ValuePerDatesUrl}/${fechaInicio}/${fechaFin}}`, { observe: 'response' });
  }

  update(registroDiario: IRegistroDiario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDiario);
    return this.http
      .put<IRegistroDiario>(`${this.resourceUrl}/${getRegistroDiarioIdentifier(registroDiario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(registroDiario: IRegistroDiario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroDiario);
    return this.http
      .patch<IRegistroDiario>(`${this.resourceUrl}/${getRegistroDiarioIdentifier(registroDiario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegistroDiario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegistroDiario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRegistroDiarioToCollectionIfMissing(
    registroDiarioCollection: IRegistroDiario[],
    ...registroDiariosToCheck: (IRegistroDiario | null | undefined)[]
  ): IRegistroDiario[] {
    const registroDiarios: IRegistroDiario[] = registroDiariosToCheck.filter(isPresent);
    if (registroDiarios.length > 0) {
      const registroDiarioCollectionIdentifiers = registroDiarioCollection.map(
        registroDiarioItem => getRegistroDiarioIdentifier(registroDiarioItem)!
      );
      const registroDiariosToAdd = registroDiarios.filter(registroDiarioItem => {
        const registroDiarioIdentifier = getRegistroDiarioIdentifier(registroDiarioItem);
        if (registroDiarioIdentifier == null || registroDiarioCollectionIdentifiers.includes(registroDiarioIdentifier)) {
          return false;
        }
        registroDiarioCollectionIdentifiers.push(registroDiarioIdentifier);
        return true;
      });
      return [...registroDiariosToAdd, ...registroDiarioCollection];
    }
    return registroDiarioCollection;
  }

  protected convertDateFromClient(registroDiario: IRegistroDiario): IRegistroDiario {
    return Object.assign({}, registroDiario, {
      fechaIngreso: registroDiario.fechaIngreso?.isValid() ? registroDiario.fechaIngreso.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaIngreso = res.body.fechaIngreso ? dayjs(res.body.fechaIngreso) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((registroDiario: IRegistroDiario) => {
        registroDiario.fechaIngreso = registroDiario.fechaIngreso ? dayjs(registroDiario.fechaIngreso) : undefined;
      });
    }
    return res;
  }
}
