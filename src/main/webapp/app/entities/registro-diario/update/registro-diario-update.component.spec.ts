import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RegistroDiarioService } from '../service/registro-diario.service';
import { IRegistroDiario, RegistroDiario } from '../registro-diario.model';

import { RegistroDiarioUpdateComponent } from './registro-diario-update.component';

describe('RegistroDiario Management Update Component', () => {
  let comp: RegistroDiarioUpdateComponent;
  let fixture: ComponentFixture<RegistroDiarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let registroDiarioService: RegistroDiarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RegistroDiarioUpdateComponent],
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
      .overrideTemplate(RegistroDiarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroDiarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    registroDiarioService = TestBed.inject(RegistroDiarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const registroDiario: IRegistroDiario = { id: 456 };

      activatedRoute.data = of({ registroDiario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(registroDiario));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroDiario>>();
      const registroDiario = { id: 123 };
      jest.spyOn(registroDiarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDiario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroDiario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(registroDiarioService.update).toHaveBeenCalledWith(registroDiario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroDiario>>();
      const registroDiario = new RegistroDiario();
      jest.spyOn(registroDiarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDiario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroDiario }));
      saveSubject.complete();

      // THEN
      expect(registroDiarioService.create).toHaveBeenCalledWith(registroDiario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroDiario>>();
      const registroDiario = { id: 123 };
      jest.spyOn(registroDiarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroDiario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(registroDiarioService.update).toHaveBeenCalledWith(registroDiario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
