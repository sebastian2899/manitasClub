import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GastosService } from '../service/gastos.service';
import { IGastos, Gastos } from '../gastos.model';

import { GastosUpdateComponent } from './gastos-update.component';

describe('Gastos Management Update Component', () => {
  let comp: GastosUpdateComponent;
  let fixture: ComponentFixture<GastosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gastosService: GastosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GastosUpdateComponent],
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
      .overrideTemplate(GastosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GastosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gastosService = TestBed.inject(GastosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gastos: IGastos = { id: 456 };

      activatedRoute.data = of({ gastos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gastos));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gastos>>();
      const gastos = { id: 123 };
      jest.spyOn(gastosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gastos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(gastosService.update).toHaveBeenCalledWith(gastos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gastos>>();
      const gastos = new Gastos();
      jest.spyOn(gastosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gastos }));
      saveSubject.complete();

      // THEN
      expect(gastosService.create).toHaveBeenCalledWith(gastos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gastos>>();
      const gastos = { id: 123 };
      jest.spyOn(gastosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gastosService.update).toHaveBeenCalledWith(gastos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
