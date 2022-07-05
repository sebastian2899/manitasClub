import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TotalGastosDetailComponent } from './total-gastos-detail.component';

describe('TotalGastos Management Detail Component', () => {
  let comp: TotalGastosDetailComponent;
  let fixture: ComponentFixture<TotalGastosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TotalGastosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ totalGastos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TotalGastosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TotalGastosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load totalGastos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.totalGastos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
