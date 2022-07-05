import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AcudienteDetailComponent } from './acudiente-detail.component';

describe('Acudiente Management Detail Component', () => {
  let comp: AcudienteDetailComponent;
  let fixture: ComponentFixture<AcudienteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AcudienteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ acudiente: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AcudienteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AcudienteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load acudiente on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.acudiente).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
