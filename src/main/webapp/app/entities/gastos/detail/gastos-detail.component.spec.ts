import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GastosDetailComponent } from './gastos-detail.component';

describe('Gastos Management Detail Component', () => {
  let comp: GastosDetailComponent;
  let fixture: ComponentFixture<GastosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GastosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gastos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GastosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GastosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gastos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gastos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
