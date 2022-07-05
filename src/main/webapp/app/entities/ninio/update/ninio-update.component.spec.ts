import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NinioService } from '../service/ninio.service';
import { INinio, Ninio } from '../ninio.model';
import { IAcudiente } from 'app/entities/acudiente/acudiente.model';
import { AcudienteService } from 'app/entities/acudiente/service/acudiente.service';

import { NinioUpdateComponent } from './ninio-update.component';

describe('Ninio Management Update Component', () => {
  let comp: NinioUpdateComponent;
  let fixture: ComponentFixture<NinioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ninioService: NinioService;
  let acudienteService: AcudienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NinioUpdateComponent],
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
      .overrideTemplate(NinioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NinioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ninioService = TestBed.inject(NinioService);
    acudienteService = TestBed.inject(AcudienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call acudiente query and add missing value', () => {
      const ninio: INinio = { id: 456 };
      const acudiente: IAcudiente = { id: 82917 };
      ninio.acudiente = acudiente;

      const acudienteCollection: IAcudiente[] = [{ id: 76825 }];
      jest.spyOn(acudienteService, 'query').mockReturnValue(of(new HttpResponse({ body: acudienteCollection })));
      const expectedCollection: IAcudiente[] = [acudiente, ...acudienteCollection];
      jest.spyOn(acudienteService, 'addAcudienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ninio });
      comp.ngOnInit();

      expect(acudienteService.query).toHaveBeenCalled();
      expect(acudienteService.addAcudienteToCollectionIfMissing).toHaveBeenCalledWith(acudienteCollection, acudiente);
      expect(comp.acudientesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ninio: INinio = { id: 456 };
      const acudiente: IAcudiente = { id: 95159 };
      ninio.acudiente = acudiente;

      activatedRoute.data = of({ ninio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ninio));
      expect(comp.acudientesCollection).toContain(acudiente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ninio>>();
      const ninio = { id: 123 };
      jest.spyOn(ninioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ninio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ninio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ninioService.update).toHaveBeenCalledWith(ninio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ninio>>();
      const ninio = new Ninio();
      jest.spyOn(ninioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ninio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ninio }));
      saveSubject.complete();

      // THEN
      expect(ninioService.create).toHaveBeenCalledWith(ninio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ninio>>();
      const ninio = { id: 123 };
      jest.spyOn(ninioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ninio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ninioService.update).toHaveBeenCalledWith(ninio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAcudienteById', () => {
      it('Should return tracked Acudiente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAcudienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
