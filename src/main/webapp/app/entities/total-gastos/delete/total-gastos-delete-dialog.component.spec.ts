jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TotalGastosService } from '../service/total-gastos.service';

import { TotalGastosDeleteDialogComponent } from './total-gastos-delete-dialog.component';

describe('TotalGastos Management Delete Component', () => {
  let comp: TotalGastosDeleteDialogComponent;
  let fixture: ComponentFixture<TotalGastosDeleteDialogComponent>;
  let service: TotalGastosService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TotalGastosDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TotalGastosDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TotalGastosDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TotalGastosService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
