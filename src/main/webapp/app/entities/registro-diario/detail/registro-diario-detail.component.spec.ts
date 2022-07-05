import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RegistroDiarioDetailComponent } from './registro-diario-detail.component';

describe('RegistroDiario Management Detail Component', () => {
  let comp: RegistroDiarioDetailComponent;
  let fixture: ComponentFixture<RegistroDiarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistroDiarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ registroDiario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RegistroDiarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RegistroDiarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load registroDiario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.registroDiario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
