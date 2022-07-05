import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMembresia, getMembresiaIdentifier } from '../membresia.model';

export type EntityResponseType = HttpResponse<IMembresia>;
export type EntityArrayResponseType = HttpResponse<IMembresia[]>;

@Injectable({ providedIn: 'root' })
export class MembresiaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/membresias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(membresia: IMembresia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(membresia);
    return this.http
      .post<IMembresia>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(membresia: IMembresia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(membresia);
    return this.http
      .put<IMembresia>(`${this.resourceUrl}/${getMembresiaIdentifier(membresia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(membresia: IMembresia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(membresia);
    return this.http
      .patch<IMembresia>(`${this.resourceUrl}/${getMembresiaIdentifier(membresia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMembresia>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMembresia[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMembresiaToCollectionIfMissing(
    membresiaCollection: IMembresia[],
    ...membresiasToCheck: (IMembresia | null | undefined)[]
  ): IMembresia[] {
    const membresias: IMembresia[] = membresiasToCheck.filter(isPresent);
    if (membresias.length > 0) {
      const membresiaCollectionIdentifiers = membresiaCollection.map(membresiaItem => getMembresiaIdentifier(membresiaItem)!);
      const membresiasToAdd = membresias.filter(membresiaItem => {
        const membresiaIdentifier = getMembresiaIdentifier(membresiaItem);
        if (membresiaIdentifier == null || membresiaCollectionIdentifiers.includes(membresiaIdentifier)) {
          return false;
        }
        membresiaCollectionIdentifiers.push(membresiaIdentifier);
        return true;
      });
      return [...membresiasToAdd, ...membresiaCollection];
    }
    return membresiaCollection;
  }

  protected convertDateFromClient(membresia: IMembresia): IMembresia {
    return Object.assign({}, membresia, {
      fechaCreacion: membresia.fechaCreacion?.isValid() ? membresia.fechaCreacion.toJSON() : undefined,
      fechaInicio: membresia.fechaInicio?.isValid() ? membresia.fechaInicio.toJSON() : undefined,
      fechaFin: membresia.fechaFin?.isValid() ? membresia.fechaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((membresia: IMembresia) => {
        membresia.fechaCreacion = membresia.fechaCreacion ? dayjs(membresia.fechaCreacion) : undefined;
        membresia.fechaInicio = membresia.fechaInicio ? dayjs(membresia.fechaInicio) : undefined;
        membresia.fechaFin = membresia.fechaFin ? dayjs(membresia.fechaFin) : undefined;
      });
    }
    return res;
  }
}
