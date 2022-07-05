import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoIdentificacion } from 'app/entities/enumerations/tipo-identificacion.model';
import { IAcudiente, Acudiente } from '../acudiente.model';

import { AcudienteService } from './acudiente.service';

describe('Acudiente Service', () => {
  let service: AcudienteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAcudiente;
  let expectedResult: IAcudiente | IAcudiente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AcudienteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      apellido: 'AAAAAAA',
      tipoIdentifiacacion: TipoIdentificacion.CC,
      direccion: 'AAAAAAA',
      telefono: 'AAAAAAA',
      email: 'AAAAAAA',
      parentesco: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Acudiente', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Acudiente()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Acudiente', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          tipoIdentifiacacion: 'BBBBBB',
          direccion: 'BBBBBB',
          telefono: 'BBBBBB',
          email: 'BBBBBB',
          parentesco: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Acudiente', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          direccion: 'BBBBBB',
          parentesco: 'BBBBBB',
        },
        new Acudiente()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Acudiente', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          tipoIdentifiacacion: 'BBBBBB',
          direccion: 'BBBBBB',
          telefono: 'BBBBBB',
          email: 'BBBBBB',
          parentesco: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Acudiente', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAcudienteToCollectionIfMissing', () => {
      it('should add a Acudiente to an empty array', () => {
        const acudiente: IAcudiente = { id: 123 };
        expectedResult = service.addAcudienteToCollectionIfMissing([], acudiente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acudiente);
      });

      it('should not add a Acudiente to an array that contains it', () => {
        const acudiente: IAcudiente = { id: 123 };
        const acudienteCollection: IAcudiente[] = [
          {
            ...acudiente,
          },
          { id: 456 },
        ];
        expectedResult = service.addAcudienteToCollectionIfMissing(acudienteCollection, acudiente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Acudiente to an array that doesn't contain it", () => {
        const acudiente: IAcudiente = { id: 123 };
        const acudienteCollection: IAcudiente[] = [{ id: 456 }];
        expectedResult = service.addAcudienteToCollectionIfMissing(acudienteCollection, acudiente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acudiente);
      });

      it('should add only unique Acudiente to an array', () => {
        const acudienteArray: IAcudiente[] = [{ id: 123 }, { id: 456 }, { id: 61623 }];
        const acudienteCollection: IAcudiente[] = [{ id: 123 }];
        expectedResult = service.addAcudienteToCollectionIfMissing(acudienteCollection, ...acudienteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acudiente: IAcudiente = { id: 123 };
        const acudiente2: IAcudiente = { id: 456 };
        expectedResult = service.addAcudienteToCollectionIfMissing([], acudiente, acudiente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acudiente);
        expect(expectedResult).toContain(acudiente2);
      });

      it('should accept null and undefined values', () => {
        const acudiente: IAcudiente = { id: 123 };
        expectedResult = service.addAcudienteToCollectionIfMissing([], null, acudiente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acudiente);
      });

      it('should return initial array if no Acudiente is added', () => {
        const acudienteCollection: IAcudiente[] = [{ id: 123 }];
        expectedResult = service.addAcudienteToCollectionIfMissing(acudienteCollection, undefined, null);
        expect(expectedResult).toEqual(acudienteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
