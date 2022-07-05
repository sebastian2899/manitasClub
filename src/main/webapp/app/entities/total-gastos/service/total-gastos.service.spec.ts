import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoCaja } from 'app/entities/enumerations/estado-caja.model';
import { ITotalGastos, TotalGastos } from '../total-gastos.model';

import { TotalGastosService } from './total-gastos.service';

describe('TotalGastos Service', () => {
  let service: TotalGastosService;
  let httpMock: HttpTestingController;
  let elemDefault: ITotalGastos;
  let expectedResult: ITotalGastos | ITotalGastos[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TotalGastosService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      valorInicial: 0,
      valorTotalGastos: 0,
      diferencia: 0,
      estado: EstadoCaja.DEUDA,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TotalGastos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.create(new TotalGastos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TotalGastos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorInicial: 1,
          valorTotalGastos: 1,
          diferencia: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TotalGastos', () => {
      const patchObject = Object.assign(
        {
          valorTotalGastos: 1,
        },
        new TotalGastos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TotalGastos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorInicial: 1,
          valorTotalGastos: 1,
          diferencia: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TotalGastos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTotalGastosToCollectionIfMissing', () => {
      it('should add a TotalGastos to an empty array', () => {
        const totalGastos: ITotalGastos = { id: 123 };
        expectedResult = service.addTotalGastosToCollectionIfMissing([], totalGastos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(totalGastos);
      });

      it('should not add a TotalGastos to an array that contains it', () => {
        const totalGastos: ITotalGastos = { id: 123 };
        const totalGastosCollection: ITotalGastos[] = [
          {
            ...totalGastos,
          },
          { id: 456 },
        ];
        expectedResult = service.addTotalGastosToCollectionIfMissing(totalGastosCollection, totalGastos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TotalGastos to an array that doesn't contain it", () => {
        const totalGastos: ITotalGastos = { id: 123 };
        const totalGastosCollection: ITotalGastos[] = [{ id: 456 }];
        expectedResult = service.addTotalGastosToCollectionIfMissing(totalGastosCollection, totalGastos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(totalGastos);
      });

      it('should add only unique TotalGastos to an array', () => {
        const totalGastosArray: ITotalGastos[] = [{ id: 123 }, { id: 456 }, { id: 16502 }];
        const totalGastosCollection: ITotalGastos[] = [{ id: 123 }];
        expectedResult = service.addTotalGastosToCollectionIfMissing(totalGastosCollection, ...totalGastosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const totalGastos: ITotalGastos = { id: 123 };
        const totalGastos2: ITotalGastos = { id: 456 };
        expectedResult = service.addTotalGastosToCollectionIfMissing([], totalGastos, totalGastos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(totalGastos);
        expect(expectedResult).toContain(totalGastos2);
      });

      it('should accept null and undefined values', () => {
        const totalGastos: ITotalGastos = { id: 123 };
        expectedResult = service.addTotalGastosToCollectionIfMissing([], null, totalGastos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(totalGastos);
      });

      it('should return initial array if no TotalGastos is added', () => {
        const totalGastosCollection: ITotalGastos[] = [{ id: 123 }];
        expectedResult = service.addTotalGastosToCollectionIfMissing(totalGastosCollection, undefined, null);
        expect(expectedResult).toEqual(totalGastosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
