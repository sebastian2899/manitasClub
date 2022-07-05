import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoMembresia, TipoMembresia } from '../tipo-membresia.model';

import { TipoMembresiaService } from './tipo-membresia.service';

describe('TipoMembresia Service', () => {
  let service: TipoMembresiaService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoMembresia;
  let expectedResult: ITipoMembresia | ITipoMembresia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoMembresiaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombreMembresia: 'AAAAAAA',
      valorMembresia: 0,
      descripcion: 'AAAAAAA',
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

    it('should create a TipoMembresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoMembresia()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoMembresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreMembresia: 'BBBBBB',
          valorMembresia: 1,
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoMembresia', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
        },
        new TipoMembresia()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoMembresia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreMembresia: 'BBBBBB',
          valorMembresia: 1,
          descripcion: 'BBBBBB',
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

    it('should delete a TipoMembresia', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoMembresiaToCollectionIfMissing', () => {
      it('should add a TipoMembresia to an empty array', () => {
        const tipoMembresia: ITipoMembresia = { id: 123 };
        expectedResult = service.addTipoMembresiaToCollectionIfMissing([], tipoMembresia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoMembresia);
      });

      it('should not add a TipoMembresia to an array that contains it', () => {
        const tipoMembresia: ITipoMembresia = { id: 123 };
        const tipoMembresiaCollection: ITipoMembresia[] = [
          {
            ...tipoMembresia,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoMembresiaToCollectionIfMissing(tipoMembresiaCollection, tipoMembresia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoMembresia to an array that doesn't contain it", () => {
        const tipoMembresia: ITipoMembresia = { id: 123 };
        const tipoMembresiaCollection: ITipoMembresia[] = [{ id: 456 }];
        expectedResult = service.addTipoMembresiaToCollectionIfMissing(tipoMembresiaCollection, tipoMembresia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoMembresia);
      });

      it('should add only unique TipoMembresia to an array', () => {
        const tipoMembresiaArray: ITipoMembresia[] = [{ id: 123 }, { id: 456 }, { id: 35535 }];
        const tipoMembresiaCollection: ITipoMembresia[] = [{ id: 123 }];
        expectedResult = service.addTipoMembresiaToCollectionIfMissing(tipoMembresiaCollection, ...tipoMembresiaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoMembresia: ITipoMembresia = { id: 123 };
        const tipoMembresia2: ITipoMembresia = { id: 456 };
        expectedResult = service.addTipoMembresiaToCollectionIfMissing([], tipoMembresia, tipoMembresia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoMembresia);
        expect(expectedResult).toContain(tipoMembresia2);
      });

      it('should accept null and undefined values', () => {
        const tipoMembresia: ITipoMembresia = { id: 123 };
        expectedResult = service.addTipoMembresiaToCollectionIfMissing([], null, tipoMembresia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoMembresia);
      });

      it('should return initial array if no TipoMembresia is added', () => {
        const tipoMembresiaCollection: ITipoMembresia[] = [{ id: 123 }];
        expectedResult = service.addTipoMembresiaToCollectionIfMissing(tipoMembresiaCollection, undefined, null);
        expect(expectedResult).toEqual(tipoMembresiaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
