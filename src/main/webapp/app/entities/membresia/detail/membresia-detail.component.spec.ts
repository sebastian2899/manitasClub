import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MembresiaDetailComponent } from './membresia-detail.component';

describe('Membresia Management Detail Component', () => {
  let comp: MembresiaDetailComponent;
  let fixture: ComponentFixture<MembresiaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MembresiaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ membresia: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MembresiaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MembresiaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load membresia on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.membresia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
