import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TotalGastosService } from '../service/total-gastos.service';
import { ITotalGastos, TotalGastos } from '../total-gastos.model';

import { TotalGastosUpdateComponent } from './total-gastos-update.component';

describe('TotalGastos Management Update Component', () => {
  let comp: TotalGastosUpdateComponent;
  let fixture: ComponentFixture<TotalGastosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let totalGastosService: TotalGastosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TotalGastosUpdateComponent],
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
      .overrideTemplate(TotalGastosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TotalGastosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    totalGastosService = TestBed.inject(TotalGastosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const totalGastos: ITotalGastos = { id: 456 };

      activatedRoute.data = of({ totalGastos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(totalGastos));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TotalGastos>>();
      const totalGastos = { id: 123 };
      jest.spyOn(totalGastosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ totalGastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: totalGastos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(totalGastosService.update).toHaveBeenCalledWith(totalGastos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TotalGastos>>();
      const totalGastos = new TotalGastos();
      jest.spyOn(totalGastosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ totalGastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: totalGastos }));
      saveSubject.complete();

      // THEN
      expect(totalGastosService.create).toHaveBeenCalledWith(totalGastos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TotalGastos>>();
      const totalGastos = { id: 123 };
      jest.spyOn(totalGastosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ totalGastos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(totalGastosService.update).toHaveBeenCalledWith(totalGastos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
