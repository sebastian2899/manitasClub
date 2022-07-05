import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRegistroDiario, RegistroDiario } from '../registro-diario.model';

import { RegistroDiarioService } from './registro-diario.service';

describe('RegistroDiario Service', () => {
  let service: RegistroDiarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IRegistroDiario;
  let expectedResult: IRegistroDiario | IRegistroDiario[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegistroDiarioService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombreNinio: 'AAAAAAA',
      nombreAcudiente: 'AAAAAAA',
      telefonoAcudiente: 'AAAAAAA',
      valor: 0,
      fechaIngreso: currentDate,
      horaEntrada: 'AAAAAAA',
      horaSalida: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RegistroDiario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaIngreso: currentDate,
        },
        returnedFromService
      );

      service.create(new RegistroDiario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RegistroDiario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreNinio: 'BBBBBB',
          nombreAcudiente: 'BBBBBB',
          telefonoAcudiente: 'BBBBBB',
          valor: 1,
          fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
          horaEntrada: 'BBBBBB',
          horaSalida: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaIngreso: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RegistroDiario', () => {
      const patchObject = Object.assign(
        {
          nombreNinio: 'BBBBBB',
          telefonoAcudiente: 'BBBBBB',
          fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
          horaSalida: 'BBBBBB',
        },
        new RegistroDiario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaIngreso: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RegistroDiario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreNinio: 'BBBBBB',
          nombreAcudiente: 'BBBBBB',
          telefonoAcudiente: 'BBBBBB',
          valor: 1,
          fechaIngreso: currentDate.format(DATE_TIME_FORMAT),
          horaEntrada: 'BBBBBB',
          horaSalida: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaIngreso: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RegistroDiario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRegistroDiarioToCollectionIfMissing', () => {
      it('should add a RegistroDiario to an empty array', () => {
        const registroDiario: IRegistroDiario = { id: 123 };
        expectedResult = service.addRegistroDiarioToCollectionIfMissing([], registroDiario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroDiario);
      });

      it('should not add a RegistroDiario to an array that contains it', () => {
        const registroDiario: IRegistroDiario = { id: 123 };
        const registroDiarioCollection: IRegistroDiario[] = [
          {
            ...registroDiario,
          },
          { id: 456 },
        ];
        expectedResult = service.addRegistroDiarioToCollectionIfMissing(registroDiarioCollection, registroDiario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RegistroDiario to an array that doesn't contain it", () => {
        const registroDiario: IRegistroDiario = { id: 123 };
        const registroDiarioCollection: IRegistroDiario[] = [{ id: 456 }];
        expectedResult = service.addRegistroDiarioToCollectionIfMissing(registroDiarioCollection, registroDiario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroDiario);
      });

      it('should add only unique RegistroDiario to an array', () => {
        const registroDiarioArray: IRegistroDiario[] = [{ id: 123 }, { id: 456 }, { id: 84982 }];
        const registroDiarioCollection: IRegistroDiario[] = [{ id: 123 }];
        expectedResult = service.addRegistroDiarioToCollectionIfMissing(registroDiarioCollection, ...registroDiarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const registroDiario: IRegistroDiario = { id: 123 };
        const registroDiario2: IRegistroDiario = { id: 456 };
        expectedResult = service.addRegistroDiarioToCollectionIfMissing([], registroDiario, registroDiario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroDiario);
        expect(expectedResult).toContain(registroDiario2);
      });

      it('should accept null and undefined values', () => {
        const registroDiario: IRegistroDiario = { id: 123 };
        expectedResult = service.addRegistroDiarioToCollectionIfMissing([], null, registroDiario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroDiario);
      });

      it('should return initial array if no RegistroDiario is added', () => {
        const registroDiarioCollection: IRegistroDiario[] = [{ id: 123 }];
        expectedResult = service.addRegistroDiarioToCollectionIfMissing(registroDiarioCollection, undefined, null);
        expect(expectedResult).toEqual(registroDiarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
