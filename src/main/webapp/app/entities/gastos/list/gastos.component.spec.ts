import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GastosService } from '../service/gastos.service';

import { GastosComponent } from './gastos.component';

describe('Gastos Management Component', () => {
  let comp: GastosComponent;
  let fixture: ComponentFixture<GastosComponent>;
  let service: GastosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GastosComponent],
    })
      .overrideTemplate(GastosComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GastosComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GastosService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.gastos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
