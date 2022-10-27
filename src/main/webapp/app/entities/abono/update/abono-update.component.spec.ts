import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AbonoService } from '../service/abono.service';
import { IAbono, Abono } from '../abono.model';

import { AbonoUpdateComponent } from './abono-update.component';

describe('Abono Management Update Component', () => {
  let comp: AbonoUpdateComponent;
  let fixture: ComponentFixture<AbonoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let abonoService: AbonoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AbonoUpdateComponent],
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
      .overrideTemplate(AbonoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AbonoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    abonoService = TestBed.inject(AbonoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const abono: IAbono = { id: 456 };

      activatedRoute.data = of({ abono });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(abono));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abono>>();
      const abono = { id: 123 };
      jest.spyOn(abonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: abono }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(abonoService.update).toHaveBeenCalledWith(abono);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abono>>();
      const abono = new Abono();
      jest.spyOn(abonoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: abono }));
      saveSubject.complete();

      // THEN
      expect(abonoService.create).toHaveBeenCalledWith(abono);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abono>>();
      const abono = { id: 123 };
      jest.spyOn(abonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(abonoService.update).toHaveBeenCalledWith(abono);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
