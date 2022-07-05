import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoMembresiaDetailComponent } from './tipo-membresia-detail.component';

describe('TipoMembresia Management Detail Component', () => {
  let comp: TipoMembresiaDetailComponent;
  let fixture: ComponentFixture<TipoMembresiaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoMembresiaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoMembresia: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoMembresiaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoMembresiaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoMembresia on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoMembresia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
