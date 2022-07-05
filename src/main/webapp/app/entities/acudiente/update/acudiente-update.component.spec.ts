import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AcudienteService } from '../service/acudiente.service';
import { IAcudiente, Acudiente } from '../acudiente.model';

import { AcudienteUpdateComponent } from './acudiente-update.component';

describe('Acudiente Management Update Component', () => {
  let comp: AcudienteUpdateComponent;
  let fixture: ComponentFixture<AcudienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acudienteService: AcudienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AcudienteUpdateComponent],
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
      .overrideTemplate(AcudienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcudienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acudienteService = TestBed.inject(AcudienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const acudiente: IAcudiente = { id: 456 };

      activatedRoute.data = of({ acudiente });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(acudiente));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Acudiente>>();
      const acudiente = { id: 123 };
      jest.spyOn(acudienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acudiente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acudiente }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(acudienteService.update).toHaveBeenCalledWith(acudiente);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Acudiente>>();
      const acudiente = new Acudiente();
      jest.spyOn(acudienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acudiente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acudiente }));
      saveSubject.complete();

      // THEN
      expect(acudienteService.create).toHaveBeenCalledWith(acudiente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Acudiente>>();
      const acudiente = { id: 123 };
      jest.spyOn(acudienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acudiente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acudienteService.update).toHaveBeenCalledWith(acudiente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
