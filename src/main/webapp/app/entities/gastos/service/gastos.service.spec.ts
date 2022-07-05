import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGastos, Gastos } from '../gastos.model';

import { GastosService } from './gastos.service';

describe('Gastos Service', () => {
  let service: GastosService;
  let httpMock: HttpTestingController;
  let elemDefault: IGastos;
  let expectedResult: IGastos | IGastos[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GastosService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      valor: 0,
      descripcion: 'AAAAAAA',
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

    it('should create a Gastos', () => {
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

      service.create(new Gastos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gastos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valor: 1,
          descripcion: 'BBBBBB',
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

    it('should partial update a Gastos', () => {
      const patchObject = Object.assign({}, new Gastos());

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

    it('should return a list of Gastos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valor: 1,
          descripcion: 'BBBBBB',
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

    it('should delete a Gastos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGastosToCollectionIfMissing', () => {
      it('should add a Gastos to an empty array', () => {
        const gastos: IGastos = { id: 123 };
        expectedResult = service.addGastosToCollectionIfMissing([], gastos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gastos);
      });

      it('should not add a Gastos to an array that contains it', () => {
        const gastos: IGastos = { id: 123 };
        const gastosCollection: IGastos[] = [
          {
            ...gastos,
          },
          { id: 456 },
        ];
        expectedResult = service.addGastosToCollectionIfMissing(gastosCollection, gastos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gastos to an array that doesn't contain it", () => {
        const gastos: IGastos = { id: 123 };
        const gastosCollection: IGastos[] = [{ id: 456 }];
        expectedResult = service.addGastosToCollectionIfMissing(gastosCollection, gastos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gastos);
      });

      it('should add only unique Gastos to an array', () => {
        const gastosArray: IGastos[] = [{ id: 123 }, { id: 456 }, { id: 64841 }];
        const gastosCollection: IGastos[] = [{ id: 123 }];
        expectedResult = service.addGastosToCollectionIfMissing(gastosCollection, ...gastosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gastos: IGastos = { id: 123 };
        const gastos2: IGastos = { id: 456 };
        expectedResult = service.addGastosToCollectionIfMissing([], gastos, gastos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gastos);
        expect(expectedResult).toContain(gastos2);
      });

      it('should accept null and undefined values', () => {
        const gastos: IGastos = { id: 123 };
        expectedResult = service.addGastosToCollectionIfMissing([], null, gastos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gastos);
      });

      it('should return initial array if no Gastos is added', () => {
        const gastosCollection: IGastos[] = [{ id: 123 }];
        expectedResult = service.addGastosToCollectionIfMissing(gastosCollection, undefined, null);
        expect(expectedResult).toEqual(gastosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
