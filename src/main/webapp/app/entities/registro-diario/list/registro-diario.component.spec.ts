import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RegistroDiarioService } from '../service/registro-diario.service';

import { RegistroDiarioComponent } from './registro-diario.component';

describe('RegistroDiario Management Component', () => {
  let comp: RegistroDiarioComponent;
  let fixture: ComponentFixture<RegistroDiarioComponent>;
  let service: RegistroDiarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RegistroDiarioComponent],
    })
      .overrideTemplate(RegistroDiarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroDiarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RegistroDiarioService);

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
    expect(comp.registroDiarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
