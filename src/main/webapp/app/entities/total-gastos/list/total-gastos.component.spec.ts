import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TotalGastosService } from '../service/total-gastos.service';

import { TotalGastosComponent } from './total-gastos.component';

describe('TotalGastos Management Component', () => {
  let comp: TotalGastosComponent;
  let fixture: ComponentFixture<TotalGastosComponent>;
  let service: TotalGastosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TotalGastosComponent],
    })
      .overrideTemplate(TotalGastosComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TotalGastosComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TotalGastosService);

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
    expect(comp.totalGastos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
