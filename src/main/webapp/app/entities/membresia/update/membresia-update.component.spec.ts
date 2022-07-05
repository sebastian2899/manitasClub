import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MembresiaService } from '../service/membresia.service';
import { IMembresia, Membresia } from '../membresia.model';
import { ITipoMembresia } from 'app/entities/tipo-membresia/tipo-membresia.model';
import { TipoMembresiaService } from 'app/entities/tipo-membresia/service/tipo-membresia.service';
import { INinio } from 'app/entities/ninio/ninio.model';
import { NinioService } from 'app/entities/ninio/service/ninio.service';

import { MembresiaUpdateComponent } from './membresia-update.component';

describe('Membresia Management Update Component', () => {
  let comp: MembresiaUpdateComponent;
  let fixture: ComponentFixture<MembresiaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let membresiaService: MembresiaService;
  let tipoMembresiaService: TipoMembresiaService;
  let ninioService: NinioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MembresiaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MembresiaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MembresiaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    membresiaService = TestBed.inject(MembresiaService);
    tipoMembresiaService = TestBed.inject(TipoMembresiaService);
    ninioService = TestBed.inject(NinioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call tipo query and add missing value', () => {
      const membresia: IMembresia = { id: 456 };
      const tipo: ITipoMembresia = { id: 82974 };
      membresia.tipo = tipo;

      const tipoCollection: ITipoMembresia[] = [{ id: 53215 }];
      jest.spyOn(tipoMembresiaService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoCollection })));
      const expectedCollection: ITipoMembresia[] = [tipo, ...tipoCollection];
      jest.spyOn(tipoMembresiaService, 'addTipoMembresiaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      expect(tipoMembresiaService.query).toHaveBeenCalled();
      expect(tipoMembresiaService.addTipoMembresiaToCollectionIfMissing).toHaveBeenCalledWith(tipoCollection, tipo);
      expect(comp.tiposCollection).toEqual(expectedCollection);
    });

    it('Should call ninio query and add missing value', () => {
      const membresia: IMembresia = { id: 456 };
      const ninio: INinio = { id: 42378 };
      membresia.ninio = ninio;

      const ninioCollection: INinio[] = [{ id: 68119 }];
      jest.spyOn(ninioService, 'query').mockReturnValue(of(new HttpResponse({ body: ninioCollection })));
      const expectedCollection: INinio[] = [ninio, ...ninioCollection];
      jest.spyOn(ninioService, 'addNinioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      expect(ninioService.query).toHaveBeenCalled();
      expect(ninioService.addNinioToCollectionIfMissing).toHaveBeenCalledWith(ninioCollection, ninio);
      expect(comp.niniosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const membresia: IMembresia = { id: 456 };
      const tipo: ITipoMembresia = { id: 30285 };
      membresia.tipo = tipo;
      const ninio: INinio = { id: 23599 };
      membresia.ninio = ninio;

      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(membresia));
      expect(comp.tiposCollection).toContain(tipo);
      expect(comp.niniosCollection).toContain(ninio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membresia>>();
      const membresia = { id: 123 };
      jest.spyOn(membresiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: membresia }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(membresiaService.update).toHaveBeenCalledWith(membresia);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membresia>>();
      const membresia = new Membresia();
      jest.spyOn(membresiaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: membresia }));
      saveSubject.complete();

      // THEN
      expect(membresiaService.create).toHaveBeenCalledWith(membresia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membresia>>();
      const membresia = { id: 123 };
      jest.spyOn(membresiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(membresiaService.update).toHaveBeenCalledWith(membresia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoMembresiaById', () => {
      it('Should return tracked TipoMembresia primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoMembresiaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackNinioById', () => {
      it('Should return tracked Ninio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNinioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
