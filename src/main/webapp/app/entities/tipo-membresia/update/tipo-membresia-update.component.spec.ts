import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoMembresiaService } from '../service/tipo-membresia.service';
import { ITipoMembresia, TipoMembresia } from '../tipo-membresia.model';

import { TipoMembresiaUpdateComponent } from './tipo-membresia-update.component';

describe('TipoMembresia Management Update Component', () => {
  let comp: TipoMembresiaUpdateComponent;
  let fixture: ComponentFixture<TipoMembresiaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoMembresiaService: TipoMembresiaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoMembresiaUpdateComponent],
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
      .overrideTemplate(TipoMembresiaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoMembresiaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoMembresiaService = TestBed.inject(TipoMembresiaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoMembresia: ITipoMembresia = { id: 456 };

      activatedRoute.data = of({ tipoMembresia });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoMembresia));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoMembresia>>();
      const tipoMembresia = { id: 123 };
      jest.spyOn(tipoMembresiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoMembresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoMembresia }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoMembresiaService.update).toHaveBeenCalledWith(tipoMembresia);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoMembresia>>();
      const tipoMembresia = new TipoMembresia();
      jest.spyOn(tipoMembresiaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoMembresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoMembresia }));
      saveSubject.complete();

      // THEN
      expect(tipoMembresiaService.create).toHaveBeenCalledWith(tipoMembresia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoMembresia>>();
      const tipoMembresia = { id: 123 };
      jest.spyOn(tipoMembresiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoMembresia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoMembresiaService.update).toHaveBeenCalledWith(tipoMembresia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
