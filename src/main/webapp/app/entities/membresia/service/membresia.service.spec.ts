import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoMembresia } from 'app/entities/enumerations/estado-membresia.model';
import { IMembresia, Membresia } from '../membresia.model';

import { MembresiaService } from './membresia.service';

describe('Membresia Service', () => {
  let service: MembresiaService;
  let httpMock: HttpTestingController;
  let elemDefault: IMembresia;
  let expectedResult: IMembresia | IMembresia[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MembresiaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      fechaInicio: currentDate,
      fechaFin: currentDate,
      cantidad: 0,
      estado: EstadoMembresia.VENCIDA,
      descripcion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Membresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Membresia()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Membresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          cantidad: 1,
          estado: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Membresia', () => {
      const patchObject = Object.assign(
        {
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          cantidad: 1,
          estado: 'BBBBBB',
        },
        new Membresia()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Membresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          cantidad: 1,
          estado: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Membresia', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMembresiaToCollectionIfMissing', () => {
      it('should add a Membresia to an empty array', () => {
        const membresia: IMembresia = { id: 123 };
        expectedResult = service.addMembresiaToCollectionIfMissing([], membresia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(membresia);
      });

      it('should not add a Membresia to an array that contains it', () => {
        const membresia: IMembresia = { id: 123 };
        const membresiaCollection: IMembresia[] = [
          {
            ...membresia,
          },
          { id: 456 },
        ];
        expectedResult = service.addMembresiaToCollectionIfMissing(membresiaCollection, membresia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Membresia to an array that doesn't contain it", () => {
        const membresia: IMembresia = { id: 123 };
        const membresiaCollection: IMembresia[] = [{ id: 456 }];
        expectedResult = service.addMembresiaToCollectionIfMissing(membresiaCollection, membresia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(membresia);
      });

      it('should add only unique Membresia to an array', () => {
        const membresiaArray: IMembresia[] = [{ id: 123 }, { id: 456 }, { id: 74118 }];
        const membresiaCollection: IMembresia[] = [{ id: 123 }];
        expectedResult = service.addMembresiaToCollectionIfMissing(membresiaCollection, ...membresiaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const membresia: IMembresia = { id: 123 };
        const membresia2: IMembresia = { id: 456 };
        expectedResult = service.addMembresiaToCollectionIfMissing([], membresia, membresia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(membresia);
        expect(expectedResult).toContain(membresia2);
      });

      it('should accept null and undefined values', () => {
        const membresia: IMembresia = { id: 123 };
        expectedResult = service.addMembresiaToCollectionIfMissing([], null, membresia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(membresia);
      });

      it('should return initial array if no Membresia is added', () => {
        const membresiaCollection: IMembresia[] = [{ id: 123 }];
        expectedResult = service.addMembresiaToCollectionIfMissing(membresiaCollection, undefined, null);
        expect(expectedResult).toEqual(membresiaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
